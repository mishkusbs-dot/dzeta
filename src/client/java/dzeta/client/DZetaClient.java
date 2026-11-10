package dzeta.client;

import dzeta.DZetaMod;
import dzeta.client.gui.ClickGuiScreen;
import dzeta.client.module.Module;
import dzeta.client.module.ModuleManager;
import dzeta.client.module.impl.ElytraSwap;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class DZetaClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ModuleManager.init();

		KeyMapping menuKey = KeyBindingHelper.registerKeyBinding(
			new KeyMapping("key.dzeta.menu", GLFW.GLFW_KEY_RIGHT_SHIFT, KeyMapping.Category.MISC));
		KeyMapping swapKey = KeyBindingHelper.registerKeyBinding(
			new KeyMapping("key.dzeta.elytra_swap", GLFW.GLFW_KEY_R, KeyMapping.Category.MISC));

		ClientTickEvents.END_CLIENT_TICK.register(mc -> {
			while (menuKey.consumeClick()) {
				if (mc.screen == null) mc.setScreen(new ClickGuiScreen());
			}
			while (swapKey.consumeClick()) {
				if (mc.screen != null) continue;
				for (Module m : ModuleManager.all()) {
					if (m instanceof ElytraSwap es && es.isEnabled()) es.swap(mc);
				}
			}
			ModuleManager.tick(mc);
		});

		HudElementRegistry.addLast(DZetaMod.id("hud"), (graphics, delta) -> ModuleManager.renderHud(graphics));
		ClientLifecycleEvents.CLIENT_STOPPING.register(mc -> ModuleManager.save());
	}
}
