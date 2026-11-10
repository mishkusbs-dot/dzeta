package dzeta.client.module.impl;

import dzeta.client.module.Category;
import dzeta.client.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class Cooldowns extends Module {
	private static final ItemStack[] TRACKED = {
		new ItemStack(Items.ENDER_PEARL), new ItemStack(Items.CHORUS_FRUIT),
		new ItemStack(Items.GOLDEN_APPLE), new ItemStack(Items.ENCHANTED_GOLDEN_APPLE),
		new ItemStack(Items.SHIELD), new ItemStack(Items.WIND_CHARGE),
		new ItemStack(Items.FIREWORK_ROCKET), new ItemStack(Items.TRIDENT)
	};

	public Cooldowns() {
		super("Cooldowns", Category.UTILITIES);
	}

	@Override
	public void renderHud(GuiGraphics g, Minecraft mc) {
		int x = g.guiWidth() / 2 - 30;
		int y = g.guiHeight() / 2 + 34;
		for (ItemStack s : TRACKED) {
			float left = mc.player.getCooldowns().getCooldownPercent(s, 0f);
			if (left <= 0f) continue;
			g.renderItem(s, x, y);
			g.fill(x + 20, y + 6, x + 60, y + 10, 0xB0141414);
			g.fill(x + 21, y + 7, x + 21 + (int) (38 * left), y + 9, 0xFF3B82F6);
			y += 17;
		}
	}
}
