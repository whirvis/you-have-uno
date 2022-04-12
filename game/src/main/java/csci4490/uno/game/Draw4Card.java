package csci4490.uno.game;

public class Draw4Card extends WildCard {
	
	public Draw4Card(String value) {
		super(value);
		
	}

	public String applyCardEffect()
	{
		String drawFour = "WILD_DRAW_FOUR";
		
		return drawFour;
		
	}
	
	public void setColor(String color)
	{
		this.color = color;
	}

}
 