package csci4490.uno.game;

abstract class SpecialCard extends RegularCard{

	public SpecialCard(String color, String value) {
		super(color, value);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Boolean matchCard(Card card) {

		if (card.getColor().equals(this.getColor()) || card.getValue() == this.getValue())
		{
			return true;
		}
		
		return false;
	}
	
	abstract public String applyCardEffect();
	
}
