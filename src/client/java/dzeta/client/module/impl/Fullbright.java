package dzeta.client.module.impl;

import dzeta.client.module.Category;
import dzeta.client.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class Fullbright extends Module {
	public Fullbright() {
		super("Fullbright", Category.VISUALS);
	}

	@Override
	public void tick(Minecraft mc) {
		var p = mc.player;
		if (!p.hasEffect(MobEffects.NIGHT_VISION)) {
			p.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 100000, 0, false, false, false));
		}
	}

	@Override
	protected void onDisable() {
		var p = Minecraft.getInstance().player;
		if (p != null) p.removeEffect(MobEffects.NIGHT_VISION);
	}
}
