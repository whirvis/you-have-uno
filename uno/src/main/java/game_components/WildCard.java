package game_components;

abstract class WildCard extends Card{

	
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

}
