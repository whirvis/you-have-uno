package csci4490.uno.game;

class WildCard extends Card{
	
	public WildCard(String value)
	{
		super();
		this.value = value;
		setScoreValue(50);
	}
	@Override
	public Boolean matchCard(Card card) {
		
		if (card.getColor().equals(this.getColor()))
		{
			return true;
		}
		return false;
	}
	
	public void setColor(String color)
	{
		this.color = color;
	}
	@Override
	public String applyCardEffect() {
		// TODO Auto-generated method stub
		return "WILD";
	}

}
