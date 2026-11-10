package dzeta.client.module.impl;

import dzeta.client.module.Category;
import dzeta.client.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

/** Disconnects when health drops to 3 hearts or lower, then turns itself off. */
public class AutoLeave extends Module {
	public AutoLeave() {
		super("Auto Leave", Category.UTILITIES);
	}

	@Override
	public void tick(Minecraft mc) {
		var p = mc.player;
		if (p.isAlive() && p.getHealth() <= 6.0f && mc.getConnection() != null) {
			setEnabled(false);
			mc.getConnection().getConnection().disconnect(Component.literal("DZeta: Auto Leave (low health)"));
		}
	}
}
