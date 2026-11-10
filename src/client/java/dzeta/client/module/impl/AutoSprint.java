package dzeta.client.module.impl;

import dzeta.client.module.Category;
import dzeta.client.module.Module;
import net.minecraft.client.Minecraft;

public class AutoSprint extends Module {
	public AutoSprint() {
		super("Auto Sprint", Category.UTILITIES);
	}

	@Override
	public void tick(Minecraft mc) {
		var p = mc.player;
		if (mc.screen == null && mc.options.keyUp.isDown() && !p.isShiftKeyDown() && !p.isUsingItem()
			&& !p.horizontalCollision && p.getFoodData().getFoodLevel() > 6) {
			p.setSprinting(true);
		}
	}
}
