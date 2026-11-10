package dzeta.client.module.impl;

import dzeta.client.module.Category;
import dzeta.client.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/** Press the Elytra Swap key (default R) to swap between chestplate and elytra. */
public class ElytraSwap extends Module {
	public ElytraSwap() {
		super("Elytra Swap", Category.UTILITIES);
	}

	private static boolean isChestplate(ItemStack s) {
		if (s.isEmpty() || s.is(Items.ELYTRA)) return false;
		var eq = s.get(net.minecraft.core.component.DataComponents.EQUIPPABLE);
		return eq != null && eq.slot() == EquipmentSlot.CHEST;
	}

	public void swap(Minecraft mc) {
		var p = mc.player;
		if (p == null || mc.gameMode == null) return;
		boolean wearingElytra = p.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA);
		int src = -1;
		for (int i = 9; i < 45; i++) {
			ItemStack s = p.inventoryMenu.getSlot(i).getItem();
			if (wearingElytra ? isChestplate(s) : s.is(Items.ELYTRA)) {
				src = i;
				break;
			}
		}
		if (src < 0) return;
		int id = p.inventoryMenu.containerId;
		mc.gameMode.handleInventoryMouseClick(id, src, 0, ClickType.PICKUP, p);
		mc.gameMode.handleInventoryMouseClick(id, 6, 0, ClickType.PICKUP, p);
		mc.gameMode.handleInventoryMouseClick(id, src, 0, ClickType.PICKUP, p);
	}
}
