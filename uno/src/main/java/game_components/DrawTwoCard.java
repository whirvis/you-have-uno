package game_components;

public class DrawTwoCard extends SpecialCard {

	public DrawTwoCard(String color, String value) {
		super(color, value);
		
	}

	@Override
	public String applyCardEffect() {
		String drawTwo = "DRAW_TWO";
		return drawTwo;
	}

}
