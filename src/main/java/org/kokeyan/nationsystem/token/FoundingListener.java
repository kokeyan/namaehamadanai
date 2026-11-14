package org.kokeyan.nationsystem.token;

import dev.tacyeet.warringnations.api.WnEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

/**
 * Warring Nations の建国イベントを購読し、建国者にトークンを渡す。
 * 承認制を切っていれば建国した瞬間に NationCreated が発火する(承認制のときは承認された後)。
 */
public final class FoundingListener {

    private FoundingListener() {}

    public static void register() {
        WnEvents.subscribe(WnEvents.NationCreated.class, event -> {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server == null) return;
            // 発火スレッドが分からないため、必ずサーバースレッドで処理する
            server.execute(() -> {
                ServerPlayer leader = server.getPlayerList().getPlayer(event.leader());
                if (leader != null) {
                    TokenService.grantOnFounding(leader);
                }
            });
        });
    }
}
