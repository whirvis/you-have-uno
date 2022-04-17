package csci4490.uno.game;

class WildCard extends Card{
	
	public WildCard()
	{
		this.value = "WILD";
		setScoreValue(50);
	}
	@Override
	public Boolean matchCard(Card card) {
		
		//if (card.getColor().equals(this.getColor()))
		//{
			return true;
		//}
		//return false;
	}
	@Override 
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
