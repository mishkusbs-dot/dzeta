package dzeta.client.module.impl;

import dzeta.client.gui.Draw;
import dzeta.client.module.Category;
import dzeta.client.module.Module;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.world.phys.Vec3;

/** FPS / Coordinates / Ping / Speed lines drawn in one compact panel. Text is cached and refreshed a few times per second. */
public final class InfoPanel {
	public final Line fps = new Line("FPS");
	public final Line coords = new Line("Coordinates");
	public final Line ping = new Line("Ping");
	public final Line speed = new Line("Speed");
	private final List<Line> active = new ArrayList<>(4);
	private int ticks;

	public static final class Line extends Module {
		String text = "";

		Line(String name) {
			super(name, Category.HUD);
		}
	}

	public void render(GuiGraphics g, Minecraft mc) {
		active.clear();
		if (fps.isEnabled()) active.add(fps);
		if (coords.isEnabled()) active.add(coords);
		if (ping.isEnabled()) active.add(ping);
		if (speed.isEnabled()) active.add(speed);
		if (active.isEmpty()) return;

		// refresh text ~4 times a second
		if (ticks++ % 5 == 0 || fps.text.isEmpty()) refresh(mc);

		int w = 0;
		for (Line l : active) w = Math.max(w, mc.font.width(l.text));
		int x = 4;
		int y = Watermark.isOn() ? 30 : 4;
		int h = active.size() * 11 + 5;
		Draw.round(g, x, y, w + 10, h, 4, 0xB0141414);
		g.fill(x + 2, y + 3, x + 4, y + h - 3, 0xFF3B82F6);
		int ty = y + 4;
		for (Line l : active) {
			g.drawString(mc.font, l.text, x + 7, ty, 0xFFEAEAEA, false);
			ty += 11;
		}
	}

	private void refresh(Minecraft mc) {
		var p = mc.player;
		if (p == null) return;
		fps.text = "FPS " + mc.getFps();
		coords.text = "XYZ " + p.getBlockX() + " " + p.getBlockY() + " " + p.getBlockZ();
		int latency = 0;
		if (mc.getConnection() != null) {
			PlayerInfo info = mc.getConnection().getPlayerInfo(p.getUUID());
			if (info != null) latency = info.getLatency();
		}
		ping.text = "Ping " + latency + " ms";
		Vec3 v = p.getDeltaMovement();
		double bps = Math.sqrt(v.x * v.x + v.z * v.z) * 20.0;
		speed.text = String.format("Speed %.1f b/s", bps);
	}
}
