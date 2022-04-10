package csci4490.uno.game;

public class ReverseCard extends SpecialCard {

	public ReverseCard(String color, String value) {
		super(color, value);
		
	}

	@Override
	public String applyCardEffect() {
		String reverse = "REVERSE";
		return reverse;
	}

}
