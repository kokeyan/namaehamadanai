package org.kokeyan.nationsystem.client.ui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;
import org.kokeyan.nationsystem.network.SelectVeinPayload;
import org.kokeyan.nationsystem.network.VeinEntry;

import java.util.List;

/** 鉱脈トークンを右クリックしたときに開く、鉱脈の選択画面 */
public class VeinSelectScreen extends Screen {

    private static final int TITLE_H = 20;
    private static final int PAD = 16;
    private static final int BTN_W = 200;
    private static final int BTN_H = 22;
    private static final int GAP = 6;

    private final List<VeinEntry> veins;
    private int wx, wy, ww, wh;

    public VeinSelectScreen(List<VeinEntry> veins) {
        super(Component.literal("鉱脈を選択"));
        this.veins = veins;
    }

    @Override
    protected void init() {
        int listH = veins.size() * (BTN_H + GAP);
        ww = BTN_W + PAD * 2;
        wh = TITLE_H + PAD + 24 + listH + BTN_H + PAD;
        wx = (this.width - ww) / 2;
        wy = (this.height - wh) / 2;

        int x = wx + PAD;
        int y = wy + TITLE_H + PAD + 24;

        for (int i = 0; i < veins.size(); i++) {
            final int index = i;
            this.addRenderableWidget(new ThemedButton(x, y, BTN_W, BTN_H,
                    Component.literal(veins.get(i).label()), b -> select(index)));
            y += BTN_H + GAP;
        }
        this.addRenderableWidget(new ThemedButton(x, y, BTN_W, BTN_H, Component.literal("キャンセル"), b -> this.onClose()));
    }

    private void select(int index) {
        PacketDistributor.sendToServer(new SelectVeinPayload(index));
        this.onClose();
    }

    /** 標準の背景(ぼかし)の代わりに、暗幕とウィンドウ枠を描く */
    @Override
    public void renderBackground(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        g.fill(0, 0, this.width, this.height, 0xB0000000);
        g.fill(wx, wy, wx + ww, wy + wh, 0xF00A0E14);
        border(g, wx, wy, ww, wh, 0xFF2E6F9E);
        g.fill(wx, wy, wx + ww, wy + TITLE_H, 0xFF12324A);
        border(g, wx, wy, ww, TITLE_H, 0xFF4FD8E8);
        g.drawCenteredString(this.font, this.title, wx + ww / 2, wy + 6, ThemedButton.COLOR_TEXT);
        g.drawCenteredString(this.font, Component.literal("トークン1個を消費して、足元のチャンクに生成します"),
                wx + ww / 2, wy + TITLE_H + PAD, ThemedButton.COLOR_TEXT_DISABLED);
    }

    private static void border(GuiGraphics g, int x, int y, int w, int h, int color) {
        g.fill(x, y, x + w, y + 1, color);
        g.fill(x, y + h - 1, x + w, y + h, color);
        g.fill(x, y, x + 1, y + h, color);
        g.fill(x + w - 1, y, x + w, y + h, color);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
