package org.kokeyan.nationsystem.token;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.kokeyan.nationsystem.NationSystem;
import org.kokeyan.nationsystem.config.NationConfig;
import org.kokeyan.nationsystem.network.VeinEntry;

import java.util.ArrayList;
import java.util.List;

/** 鉱脈トークンの判定・付与・使用(すべてサーバー側で動く) */
public final class TokenService {

    /** 「このプレイヤーには付与済み」を記録するプレイヤーデータのキー */
    public static final String GRANTED_TAG = NationSystem.MOD_ID + "_vein_token_granted";

    private TokenService() {}

    /** configのIDから実際のアイテムを引く。未登録・不正なIDならnull */
    public static Item tokenItem() {
        ResourceLocation id = ResourceLocation.tryParse(NationConfig.TOKEN_ITEM.get());
        if (id == null) return null;
        return BuiltInRegistries.ITEM.getOptional(id).orElse(null);
    }

    public static boolean isToken(ItemStack stack) {
        Item token = tokenItem();
        return token != null && !stack.isEmpty() && stack.is(token);
    }

    /** configの鉱脈リスト(「ID|表示名」)を解析する */
    public static List<VeinEntry> veinEntries() {
        List<VeinEntry> out = new ArrayList<>();
        for (String raw : NationConfig.VEINS.get()) {
            String[] parts = raw.split("\\|", 2);
            String id = parts[0].trim();
            if (id.isEmpty()) continue;
            String label = parts.length > 1 && !parts[1].isBlank() ? parts[1].trim() : id;
            out.add(new VeinEntry(id, label));
        }
        return out;
    }

    /** 建国イベントから呼ばれる。設定に応じてトークンを渡す */
    public static void grantOnFounding(ServerPlayer player) {
        if (!NationConfig.GRANT_ON_FOUNDING.get()) return;

        Item token = tokenItem();
        if (token == null) {
            NationSystem.LOGGER.warn("tokenItem '{}' がレジストリに存在しません。KubeJSの登録とconfigのIDを確認してください。",
                    NationConfig.TOKEN_ITEM.get());
            return;
        }

        CompoundTag data = player.getPersistentData();
        if (NationConfig.ONCE_PER_PLAYER.get() && data.getBoolean(GRANTED_TAG)) {
            return;
        }

        ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(token, NationConfig.TOKENS_PER_NATION.get()));
        data.putBoolean(GRANTED_TAG, true);
        player.sendSystemMessage(Component.literal("§a建国おめでとうございます! 鉱脈トークンを受け取りました。右クリックで鉱脈を選べます。"));
    }

    /** 選択された鉱脈を生成する。成功したときだけトークンを1個消費する */
    public static void useToken(ServerPlayer player, int index) {
        List<VeinEntry> veins = veinEntries();
        if (index < 0 || index >= veins.size()) return;

        ItemStack token = findToken(player);
        if (token == null) {
            player.sendSystemMessage(Component.literal("§c鉱脈トークンを持っていません。"));
            return;
        }

        MinecraftServer server = player.getServer();
        if (server == null) return;

        VeinEntry vein = veins.get(index);
        String command = NationConfig.COMMAND_TEMPLATE.get()
                .replace("{vein}", vein.id())
                .replace("{multiplier}", String.valueOf(NationConfig.MULTIPLIER.get().floatValue()));

        // プレイヤーの位置・ディメンションのまま、権限だけサーバー(OP4)にする
        CommandSourceStack source = player.createCommandSourceStack().withPermission(4);

        int result;
        try {
            result = server.getCommands().getDispatcher().execute(command, source);
        } catch (CommandSyntaxException e) {
            NationSystem.LOGGER.warn("鉱脈生成コマンドが失敗しました: /{} ({})", command, e.getMessage());
            player.sendSystemMessage(Component.literal("§c鉱脈の生成に失敗しました。トークンは消費されていません。(" + e.getMessage() + ")"));
            return;
        }

        // COEは成功時に1、レシピが鉱脈でない場合は0を返す
        if (result > 0) {
            token.shrink(1);
            player.sendSystemMessage(Component.literal("§a" + vein.label() + "を生成しました。"));
        } else {
            player.sendSystemMessage(Component.literal("§c鉱脈の生成に失敗しました。トークンは消費されていません。"));
        }
    }

    private static ItemStack findToken(ServerPlayer player) {
        Inventory inv = player.getInventory();
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (isToken(stack)) return stack;
        }
        return null;
    }
}
