package dzeta.client.module;

import dzeta.client.module.impl.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public final class ModuleManager {
	private static final List<Module> ALL = new ArrayList<>();
	private static final Module[] EMPTY = new Module[0];
	private static Module[] enabledCache = EMPTY;
	private static Path file;

	public static InfoPanel info;

	private ModuleManager() {}

	public static void init() {
		file = FabricLoader.getInstance().getConfigDir().resolve("dzeta.properties");

		// Visuals
		ALL.add(new Fullbright());
		ALL.add(new ArmorHud());
		ALL.add(new PotionList());
		ALL.add(new TargetHud());
		// HUD
		ALL.add(new Watermark());
		info = new InfoPanel();
		ALL.add(info.fps);
		ALL.add(info.coords);
		ALL.add(info.ping);
		ALL.add(info.speed);
		// Utilities
		ALL.add(new AutoEat());
		ALL.add(new AutoLeave());
		ALL.add(new AutoRespawn());
		ALL.add(new AutoSprint());
		ALL.add(new ElytraSwap());
		ALL.add(new Cooldowns());
		ALL.add(new FpsBoost());

		Properties p = new Properties();
		try (InputStream in = Files.newInputStream(file)) {
			p.load(in);
		} catch (Exception ignored) {
			// first launch: defaults
			p.setProperty("Watermark", "true");
			p.setProperty("FPS", "true");
			p.setProperty("Coordinates", "true");
			p.setProperty("Cooldowns", "true");
		}
		for (Module m : ALL) m.load(p);
		rebuildCache();
	}

	public static List<Module> all() {
		return ALL;
	}

	public static List<Module> of(Category c) {
		List<Module> out = new ArrayList<>();
		for (Module m : ALL) if (m.category == c) out.add(m);
		return out;
	}

	/** Must be called after enabling/disabling modules. */
	public static void rebuildCache() {
		List<Module> en = new ArrayList<>();
		for (Module m : ALL) if (m.isEnabled()) en.add(m);
		enabledCache = en.toArray(EMPTY);
	}

	public static void toggle(Module m) {
		m.toggle();
		rebuildCache();
	}

	public static void tick(Minecraft mc) {
		if (mc.options == null) return;
		for (Module m : ALL) {
			if (m.pendingEnable) {
				m.pendingEnable = false;
				if (m.isEnabled()) m.onEnable();
			}
		}
		if (mc.player == null || mc.level == null) return;
		for (Module m : enabledCache) m.tick(mc);
	}

	public static void renderHud(GuiGraphics g) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.player == null || mc.options.hideGui) return;
		for (Module m : enabledCache) m.renderHud(g, mc);
		info.render(g, mc);
	}

	public static void save() {
		Properties p = new Properties();
		for (Module m : ALL) m.save(p);
		try (OutputStream out = Files.newOutputStream(file)) {
			p.store(out, "DZeta config");
		} catch (Exception ignored) {
		}
	}
}
