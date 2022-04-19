package csci4490.uno.game;

abstract class Card {
	
	protected String color;
	protected String value;
	int score_value;
	
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
	
	public int getScoreValue()
	{
		return score_value;
	}
	
	void setScoreValue(int points)
	{
		score_value = points;
	}
	
	
	abstract public Boolean matchCard(Card card);
	abstract public String applyCardEffect();
	
	public String toString()
	{
		return color + ", " + value + " ," + applyCardEffect();
	}

	protected abstract void setColor(String color);
	
	

	
	
	

}
