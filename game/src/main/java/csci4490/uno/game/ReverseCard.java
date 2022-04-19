package csci4490.uno.game;

public class ReverseCard extends SpecialCard {

	public ReverseCard(String color) {
		super(color, "REVERSE");
		
	}

	@Override
	public String applyCardEffect() {
		String reverse = "REVERSE";
		return reverse;
	}

	@Override
	protected void setColor(String color) {
		// TODO Auto-generated method stub
		
	}

}
