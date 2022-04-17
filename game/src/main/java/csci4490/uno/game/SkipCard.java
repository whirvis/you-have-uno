package csci4490.uno.game;

public class SkipCard extends SpecialCard {

	public SkipCard(String color, String value) {
		super(color, value);
		
		
		
	}

	@Override
	public String applyCardEffect() {
		String skip = "SKIP";
		return skip;
	}

	@Override
	protected void setColor(String color) {
		// TODO Auto-generated method stub
		
	}

}
