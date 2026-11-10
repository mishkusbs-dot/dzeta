package dzeta.client.module;

public enum Category {
	VISUALS("Visuals"),
	HUD("HUD"),
	UTILITIES("Utilities");

	public final String label;

	Category(String label) {
		this.label = label;
	}
}
