package dzeta.client.gui;

import dzeta.DZetaMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

public final class Draw {
	public static final Identifier LOGO_16 = DZetaMod.id("textures/gui/logo_16.png");
	public static final Identifier LOGO_24 = DZetaMod.id("textures/gui/logo_24.png");
	public static final Identifier LOGO_32 = DZetaMod.id("textures/gui/logo_32.png");

	private Draw() {}

	/** Filled rounded rectangle built from a few fill() calls (cheap, no textures). */
	public static void round(GuiGraphics g, int x, int y, int w, int h, int r, int color) {
		r = Math.min(r, Math.min(w, h) / 2);
		if (r <= 0) {
			g.fill(x, y, x + w, y + h, color);
			return;
		}
		for (int i = 0; i < r; i++) {
			double dy = r - i - 0.5;
			int inset = (int) Math.round(r - Math.sqrt(r * r - dy * dy));
			g.fill(x + inset, y + i, x + w - inset, y + i + 1, color);
			g.fill(x + inset, y + h - 1 - i, x + w - inset, y + h - i, color);
		}
		g.fill(x, y + r, x + w, y + h - r, color);
	}

	public static void logo(GuiGraphics g, Identifier tex, int x, int y, int size) {
		g.blit(RenderPipelines.GUI_TEXTURED, tex, x, y, 0f, 0f, size, size, size, size);
	}

	public static int lerpColor(int a, int b, float t) {
		int ar = a >> 16 & 255, ag = a >> 8 & 255, ab = a & 255;
		int br = b >> 16 & 255, bg = b >> 8 & 255, bb = b & 255;
		int r = (int) (ar + (br - ar) * t), gg = (int) (ag + (bg - ag) * t), bl = (int) (ab + (bb - ab) * t);
		return 0xFF000000 | r << 16 | gg << 8 | bl;
	}
}
