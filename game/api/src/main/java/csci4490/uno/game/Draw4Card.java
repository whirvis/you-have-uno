package csci4490.uno.game;

public class Draw4Card extends WildCard {
	
	public Draw4Card(Game g) {
		super(g);
		this.value = "WILD_4";
		
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
 