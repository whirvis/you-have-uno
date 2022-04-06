package game_components;

public class SkipCard extends SpecialCard {

	public SkipCard(String color, String value) {
		super(color, value);
		
	}

	@Override
	public String applyCardEffect() {
		String skip = "SKIP";
		return skip;
	}

}
