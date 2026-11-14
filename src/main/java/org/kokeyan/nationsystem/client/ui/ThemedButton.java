package org.kokeyan.nationsystem.client.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

/** 黒地に青枠のフラットなボタン。ホバー時は枠が水色に光る。 */
public class ThemedButton extends Button {

    public static final int COLOR_BG = 0xF00A0E14;
    public static final int COLOR_BG_HOVER = 0xF0102436;
    public static final int COLOR_BORDER = 0xFF2E6F9E;
    public static final int COLOR_BORDER_HOVER = 0xFF4FD8E8;
    public static final int COLOR_TEXT = 0xFFBFEFFF;
    public static final int COLOR_TEXT_DISABLED = 0xFF6A7A80;

    public ThemedButton(int x, int y, int width, int height, Component message, OnPress onPress) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
    }

    @Override
    protected void renderWidget(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        boolean hovered = this.isHoveredOrFocused();
        int bg = hovered && this.active ? COLOR_BG_HOVER : COLOR_BG;
        int border = hovered && this.active ? COLOR_BORDER_HOVER : COLOR_BORDER;
        int text = this.active ? COLOR_TEXT : COLOR_TEXT_DISABLED;

        int x0 = getX(), y0 = getY(), x1 = x0 + getWidth(), y1 = y0 + getHeight();
        g.fill(x0, y0, x1, y1, bg);
        g.fill(x0, y0, x1, y0 + 1, border);
        g.fill(x0, y1 - 1, x1, y1, border);
        g.fill(x0, y0, x0 + 1, y1, border);
        g.fill(x1 - 1, y0, x1, y1, border);

        Font font = Minecraft.getInstance().font;
        g.drawCenteredString(font, getMessage(), x0 + getWidth() / 2, y0 + (getHeight() - 8) / 2, text);
    }
}
