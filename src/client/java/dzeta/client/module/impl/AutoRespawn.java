package dzeta.client.module.impl;

import dzeta.client.module.Category;
import dzeta.client.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DeathScreen;

public class AutoRespawn extends Module {
	private int wait;

	public AutoRespawn() {
		super("Auto Respawn", Category.UTILITIES);
	}

	@Override
	public void tick(Minecraft mc) {
		if (mc.screen instanceof DeathScreen) {
			if (++wait >= 10) {
				mc.player.respawn();
				wait = 0;
			}
		} else {
			wait = 0;
		}
	}
}
