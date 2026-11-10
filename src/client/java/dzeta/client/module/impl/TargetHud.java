package dzeta.client.module.impl;

import dzeta.client.gui.Draw;
import dzeta.client.module.Category;
import dzeta.client.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;

public class TargetHud extends Module {
	public TargetHud() {
		super("Target HUD", Category.VISUALS);
	}

	@Override
	public void renderHud(GuiGraphics g, Minecraft mc) {
		if (!(mc.hitResult instanceof EntityHitResult hit) || !(hit.getEntity() instanceof LivingEntity le)) return;
		String name = le.getName().getString();
		float hp = le.getHealth(), max = Math.max(1f, le.getMaxHealth());
		int w = Math.max(90, mc.font.width(name) + 12);
		int x = g.guiWidth() / 2 + 16, y = g.guiHeight() / 2 + 16;
		Draw.round(g, x, y, w, 26, 4, 0xB0141414);
		g.drawString(mc.font, name, x + 6, y + 5, 0xFFFFFFFF, false);
		int bw = w - 12;
		g.fill(x + 6, y + 17, x + 6 + bw, y + 20, 0xFF333333);
		float f = Math.min(1f, hp / max);
		g.fill(x + 6, y + 17, x + 6 + (int) (bw * f), y + 20, Draw.lerpColor(0xFFE5484D, 0xFF3FB950, f));
	}
}
