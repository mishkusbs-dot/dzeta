package dzeta.client.module.impl;

import dzeta.client.gui.Draw;
import dzeta.client.module.Category;
import dzeta.client.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.effect.MobEffectInstance;

public class PotionList extends Module {
	public PotionList() {
		super("Potion List", Category.VISUALS);
	}

	@Override
	public void renderHud(GuiGraphics g, Minecraft mc) {
		int y = 4;
		for (MobEffectInstance e : mc.player.getActiveEffects()) {
			if (!e.isVisible()) continue;
			String name = e.getEffect().value().getDisplayName().getString();
			if (e.getAmplifier() > 0) name += " " + (e.getAmplifier() + 1);
			String time;
			if (e.isInfiniteDuration()) {
				time = "inf";
			} else {
				int sec = e.getDuration() / 20;
				time = String.format("%d:%02d", sec / 60, sec % 60);
			}
			String line = name + "  " + time;
			int w = mc.font.width(line) + 10;
			int x = g.guiWidth() - w - 28;
			Draw.round(g, x, y, w, 12, 3, 0xB0141414);
			g.drawString(mc.font, line, x + 5, y + 2, 0xFFEAEAEA, false);
			y += 14;
		}
	}
}
