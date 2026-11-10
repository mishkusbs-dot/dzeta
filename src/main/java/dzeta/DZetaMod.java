package dzeta;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;

public class DZetaMod implements ModInitializer {
	public static final String MOD_ID = "dzeta";

	@Override
	public void onInitialize() {
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
