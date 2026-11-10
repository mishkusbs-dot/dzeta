package dzeta.client.gui;

import dzeta.client.module.Category;
import dzeta.client.module.Module;
import dzeta.client.module.ModuleManager;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public class ClickGuiScreen extends Screen {
	private static final int W = 340, H = 206, COL_W = 155, ROW_H = 22, GAP = 4, TAB_W = 62;
	private static Category lastTab = Category.VISUALS;

	private Category tab = lastTab;
	private float scroll, targetScroll;
	private long lastNanos = System.nanoTime();

	public ClickGuiScreen() {
		super(Component.literal("DZeta"));
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	private int px() { return (width - W) / 2; }
	private int py() { return (height - H) / 2; }
	private int top() { return py() + 32; }
	private int bottom() { return py() + H - 8; }

	@Override
	public void render(GuiGraphics g, int mx, int my, float pt) {
		long now = System.nanoTime();
		float dt = Math.min(0.1f, (now - lastNanos) / 1e9f);
		lastNanos = now;

		g.fill(0, 0, width, height, 0x66000000);
		int x = px(), y = py();
		Draw.round(g, x - 1, y - 1, W + 2, H + 2, 8, 0xFF2C2C2C);
		Draw.round(g, x, y, W, H, 7, 0xFF151515);

		// header: window dot, tabs, logo
		Draw.round(g, x + 11, y + 11, 8, 8, 4, 0xFFFF5F57);
		Category[] cats = Category.values();
		int tabsW = cats.length * TAB_W;
		int tx = x + (W - tabsW) / 2;
		Draw.round(g, tx - 2, y + 6, tabsW + 4, 18, 6, 0xFF202020);
		for (int i = 0; i < cats.length; i++) {
			int bx = tx + i * TAB_W;
			boolean sel = cats[i] == tab;
			if (sel) Draw.round(g, bx, y + 8, TAB_W, 14, 5, 0xFF343434);
			int lw = font.width(cats[i].label);
			g.drawString(font, cats[i].label, bx + (TAB_W - lw) / 2, y + 12, sel ? 0xFFFFFFFF : 0xFF8E8E8E, false);
		}
		Draw.logo(g, Draw.LOGO_24, x + W - 34, y + 3, 24);

		// module grid
		List<Module> mods = ModuleManager.of(tab);
		int rows = (mods.size() + 1) / 2;
		int contentH = rows * (ROW_H + GAP) - GAP;
		int visH = bottom() - top();
		float maxScroll = Math.max(0, contentH - visH);
		targetScroll = Math.max(0, Math.min(maxScroll, targetScroll));
		scroll += (targetScroll - scroll) * Math.min(1f, dt * 16f);

		g.enableScissor(x + 6, top(), x + W - 6, bottom());
		for (int i = 0; i < mods.size(); i++) {
			Module m = mods.get(i);
			int bx = x + 12 + (i % 2) * (COL_W + 6);
			int by = top() + (i / 2) * (ROW_H + GAP) - Math.round(scroll);
			if (by + ROW_H < top() || by > bottom()) continue;

			boolean hover = mx >= bx && mx < bx + COL_W && my >= Math.max(by, top()) && my < Math.min(by + ROW_H, bottom())
				&& mx >= x && mx < x + W;
			Draw.round(g, bx, by, COL_W, ROW_H, 5, hover ? 0xFF2E2E2E : 0xFF262626);
			g.drawString(font, m.name, bx + 9, by + 7, 0xFFE8E8E8, false);

			float target = m.isEnabled() ? 1f : 0f;
			m.anim += (target - m.anim) * Math.min(1f, dt * 18f);
			if (Math.abs(target - m.anim) < 0.01f) m.anim = target;
			int tw = 24, th = 12;
			int tx2 = bx + COL_W - 9 - tw, ty = by + (ROW_H - th) / 2;
			Draw.round(g, tx2, ty, tw, th, 6, Draw.lerpColor(0xFF474747, 0xFF3B82F6, m.anim));
			int kx = Math.round(tx2 + 2 + (tw - th) * m.anim);
			Draw.round(g, kx, ty + 2, 8, 8, 4, 0xFFFFFFFF);
		}
		g.disableScissor();

		if (maxScroll > 0) {
			int barH = Math.max(14, (int) (visH * (visH / (float) contentH)));
			int barY = top() + (int) ((visH - barH) * (scroll / maxScroll));
			Draw.round(g, x + W - 7, barY, 3, barH, 1, 0xFF4A4A4A);
		}
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
		double mx = event.x(), my = event.y();
		int x = px(), y = py();
		if (event.button() == 0) {
			Category[] cats = Category.values();
			int tx = x + (W - cats.length * TAB_W) / 2;
			if (my >= y + 8 && my < y + 22) {
				for (int i = 0; i < cats.length; i++) {
					if (mx >= tx + i * TAB_W && mx < tx + (i + 1) * TAB_W) {
						tab = lastTab = cats[i];
						scroll = targetScroll = 0;
						return true;
					}
				}
			}
			if (my >= top() && my < bottom()) {
				List<Module> mods = ModuleManager.of(tab);
				for (int i = 0; i < mods.size(); i++) {
					int bx = x + 12 + (i % 2) * (COL_W + 6);
					int by = top() + (i / 2) * (ROW_H + GAP) - Math.round(scroll);
					if (mx >= bx && mx < bx + COL_W && my >= by && my < by + ROW_H) {
						ModuleManager.toggle(mods.get(i));
						return true;
					}
				}
			}
		}
		return super.mouseClicked(event, doubleClick);
	}

	@Override
	public boolean mouseScrolled(double mx, double my, double scrollX, double scrollY) {
		targetScroll -= (float) scrollY * 24f;
		return true;
	}

	@Override
	public boolean keyPressed(KeyEvent event) {
		if (event.key() == GLFW.GLFW_KEY_RIGHT_SHIFT) {
			onClose();
			return true;
		}
		return super.keyPressed(event);
	}

	@Override
	public void onClose() {
		ModuleManager.save();
		super.onClose();
	}
}
