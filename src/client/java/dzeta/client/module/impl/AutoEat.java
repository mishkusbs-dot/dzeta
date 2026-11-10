package dzeta.client.module.impl;

import dzeta.client.module.Category;
import dzeta.client.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class AutoEat extends Module {
	private boolean eating, sawUsing;
	private int prevSlot = -1, ticks;

	public AutoEat() {
		super("Auto Eat", Category.UTILITIES);
	}

	private static boolean goodFood(ItemStack s) {
		return !s.isEmpty() && s.has(DataComponents.FOOD)
			&& !s.is(Items.GOLDEN_APPLE) && !s.is(Items.ENCHANTED_GOLDEN_APPLE) && !s.is(Items.CHORUS_FRUIT)
			&& !s.is(Items.ROTTEN_FLESH) && !s.is(Items.SPIDER_EYE) && !s.is(Items.POISONOUS_POTATO)
			&& !s.is(Items.PUFFERFISH);
	}

	@Override
	public void tick(Minecraft mc) {
		var p = mc.player;
		if (eating) {
			ticks++;
			if (mc.screen != null || ticks > 80 || (sawUsing && !p.isUsingItem())) {
				stop(mc);
			} else {
				if (p.isUsingItem()) sawUsing = true;
				mc.options.keyUse.setDown(true);
			}
			return;
		}
		if (mc.screen != null || p.getFoodData().getFoodLevel() > 14 || !p.canEat(false)) return;
		for (int i = 0; i < 9; i++) {
			if (goodFood(p.getInventory().getItem(i))) {
				prevSlot = p.getInventory().getSelectedSlot();
				p.getInventory().setSelectedSlot(i);
				eating = true;
				sawUsing = false;
				ticks = 0;
				mc.options.keyUse.setDown(true);
				return;
			}
		}
	}

	private void stop(Minecraft mc) {
		eating = false;
		mc.options.keyUse.setDown(false);
		if (prevSlot >= 0 && mc.player != null) mc.player.getInventory().setSelectedSlot(prevSlot);
		prevSlot = -1;
	}

	@Override
	protected void onDisable() {
		if (eating) stop(Minecraft.getInstance());
	}
}
