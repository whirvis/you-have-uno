package game_components;

abstract class Card {
	
	protected String color;
	protected String value;
	
	public Card()
	{
		color = null;
		value = null;
	}
	
	public String getColor()
	{
		return color;
	}
	
	public String getValue()
	{
		return value;
	}
	
	abstract public Boolean matchCard(Card card);
	
	

	
	
	

}
