package dzeta.client.module;

import java.util.Properties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public abstract class Module {
	public final String name;
	public final Category category;
	private boolean enabled;
	boolean pendingEnable;
	/** 0..1, used by the menu for the toggle animation */
	public float anim;

	protected Module(String name, Category category) {
		this.name = name;
		this.category = category;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean value) {
		if (value == enabled) return;
		enabled = value;
		if (value) onEnable(); else onDisable();
		ModuleManager.rebuildCache();
	}

	public void toggle() {
		setEnabled(!enabled);
	}

	protected void onEnable() {}
	protected void onDisable() {}

	/** Called every client tick while in a world. */
	public void tick(Minecraft mc) {}

	/** Called every frame by the HUD layer (only when enabled). */
	public void renderHud(GuiGraphics g, Minecraft mc) {}

	public void save(Properties p) {
		p.setProperty(name, Boolean.toString(enabled));
	}

	public void load(Properties p) {
		if (Boolean.parseBoolean(p.getProperty(name, "false"))) {
			enabled = true;
			pendingEnable = true;
		}
	}
}
