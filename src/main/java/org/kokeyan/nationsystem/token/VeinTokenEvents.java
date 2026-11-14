package org.kokeyan.nationsystem.token;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.kokeyan.nationsystem.NationSystem;
import org.kokeyan.nationsystem.network.OpenVeinSelectPayload;

@EventBusSubscriber(modid = NationSystem.MOD_ID)
public final class VeinTokenEvents {

    private VeinTokenEvents() {}

    /** トークンを右クリック → 鉱脈選択画面を開く(KubeJS側にスクリプトは不要) */
    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickItem event) {
        if (event.getLevel().isClientSide()) return;
        if (event.getHand() != InteractionHand.MAIN_HAND) return;
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!TokenService.isToken(event.getItemStack())) return;

        PacketDistributor.sendToPlayer(player, new OpenVeinSelectPayload(TokenService.veinEntries()));
    }

    /** 死亡・ディメンション移動でプレイヤーが作り直されても「付与済み」を引き継ぐ */
    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        if (event.getOriginal().getPersistentData().getBoolean(TokenService.GRANTED_TAG)) {
            event.getEntity().getPersistentData().putBoolean(TokenService.GRANTED_TAG, true);
        }
    }
}
