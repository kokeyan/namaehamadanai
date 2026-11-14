package org.kokeyan.nationsystem.client;

import net.minecraft.client.Minecraft;
import org.kokeyan.nationsystem.client.ui.VeinSelectScreen;
import org.kokeyan.nationsystem.network.VeinEntry;

import java.util.List;

/** クライアント専用の処理の入口。共通コードからは必ず dist 判定を通してから呼ぶこと。 */
public final class ClientHooks {

    private ClientHooks() {}

    public static void openVeinSelect(List<VeinEntry> veins) {
        Minecraft.getInstance().setScreen(new VeinSelectScreen(veins));
    }
}
