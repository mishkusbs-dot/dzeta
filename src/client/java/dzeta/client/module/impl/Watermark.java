package dzeta.client.module.impl;

import dzeta.client.gui.Draw;
import dzeta.client.module.Category;
import dzeta.client.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class Watermark extends Module {
	private static Watermark instance;

	public Watermark() {
		super("Watermark", Category.HUD);
		instance = this;
	}

	public static boolean isOn() {
		return instance != null && instance.isEnabled();
	}

	@Override
	public void renderHud(GuiGraphics g, Minecraft mc) {
		int w = mc.font.width("DZeta") + 30;
		Draw.round(g, 4, 4, w, 22, 5, 0xB0141414);
		Draw.logo(g, Draw.LOGO_16, 9, 7, 16);
		g.drawString(mc.font, "DZeta", 29, 11, 0xFFFFFFFF, false);
	}
}
