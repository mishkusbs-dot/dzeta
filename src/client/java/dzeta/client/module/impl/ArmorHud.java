package dzeta.client.module.impl;

import dzeta.client.module.Category;
import dzeta.client.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public class ArmorHud extends Module {
	private static final EquipmentSlot[] SLOTS = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

	public ArmorHud() {
		super("Armor HUD", Category.VISUALS);
	}

	@Override
	public void renderHud(GuiGraphics g, Minecraft mc) {
		int x = g.guiWidth() - 22;
		int y = g.guiHeight() / 2 - 36;
		for (EquipmentSlot slot : SLOTS) {
			ItemStack s = mc.player.getItemBySlot(slot);
			if (!s.isEmpty()) {
				g.renderItem(s, x, y);
				g.renderItemDecorations(mc.font, s, x, y);
			}
			y += 18;
		}
	}
}
