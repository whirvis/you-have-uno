package game_components;

public class RegularCard extends Card{

	public RegularCard(String color, String value) {
		
		this.color = color;
		this.value = value;
	}

	@Override
	public Boolean matchCard(Card card) {
		
		if (card.getColor().equals(this.getColor()) || card.getValue() == this.getValue())
		{
			return true;
		}
		
		return false;
		
	}


}
