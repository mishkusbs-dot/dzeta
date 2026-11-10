package dzeta.client.module.impl;

import dzeta.client.module.Category;
import dzeta.client.module.Module;
import java.util.Properties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;

/** Lowers a few heavy video options while enabled and restores the originals when disabled (also after a restart). */
public class FpsBoost extends Module {
	private int[] orig; // renderDistance, simulationDistance, biomeBlend, entityDistance*100

	public FpsBoost() {
		super("FPS Boost", Category.UTILITIES);
	}

	@Override
	protected void onEnable() {
		Options o = Minecraft.getInstance().options;
		if (orig == null) {
			orig = new int[] {
				o.renderDistance().get(), o.simulationDistance().get(),
				o.biomeBlendRadius().get(), (int) Math.round(o.entityDistanceScaling().get() * 100)
			};
		}
		o.renderDistance().set(Math.min(orig[0], 8));
		o.simulationDistance().set(Math.min(orig[1], 6));
		o.biomeBlendRadius().set(Math.min(orig[2], 1));
		o.entityDistanceScaling().set(Math.min(orig[3] / 100.0, 0.75));
	}

	@Override
	protected void onDisable() {
		if (orig == null) return;
		Options o = Minecraft.getInstance().options;
		o.renderDistance().set(orig[0]);
		o.simulationDistance().set(orig[1]);
		o.biomeBlendRadius().set(orig[2]);
		o.entityDistanceScaling().set(orig[3] / 100.0);
		orig = null;
	}

	@Override
	public void save(Properties p) {
		super.save(p);
		if (orig != null) p.setProperty(name + ".orig", orig[0] + "," + orig[1] + "," + orig[2] + "," + orig[3]);
	}

	@Override
	public void load(Properties p) {
		super.load(p);
		String s = p.getProperty(name + ".orig");
		if (s != null && isEnabled()) {
			try {
				String[] a = s.split(",");
				orig = new int[] {Integer.parseInt(a[0]), Integer.parseInt(a[1]), Integer.parseInt(a[2]), Integer.parseInt(a[3])};
			} catch (Exception ignored) {
				orig = null;
			}
		}
	}
}
