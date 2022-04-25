package csci4490.uno.game;

class WildCard extends Card{
	Game game;
	public WildCard(Game g)
	{
		this.color = "NONE";
		this.value = "WILD";
		setScoreValue(50);
		game = g;
	}
	@Override
	public Boolean matchCard(Card card) {
		
		if (card.getColor().equals(game.getWildPlayColor()) || card.toString().equals("WILD") || card.toString().equals("WILD_4"))
		{
			return true;
		}
		return false;
	}
	@Override 
	public void setColor(String color)
	{
		this.color = color;
	}
	@Override
	public String applyCardEffect() {
		
		return "WILD";
	}
	
	public String toString()
	{
		return value;
	}
	
}
