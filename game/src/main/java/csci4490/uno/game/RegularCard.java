package csci4490.uno.game;

public class RegularCard extends Card{

	public RegularCard(String color, String value) {
		
		this.color = color;
		this.value = value;
		score_value = Integer.parseInt(value);
	}

	@Override
	public Boolean matchCard(Card card) {
		
		if (card.getColor().equals(this.getColor()) || card.getValue() == this.getValue() || card.getValue().equals("WILD")) 
		{
			return true;
		}
		
		return false;
		
	}

	@Override
	public String applyCardEffect() {

		return "NONE";
	}


}
