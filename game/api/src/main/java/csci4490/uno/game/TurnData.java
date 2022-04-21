package csci4490.uno.game;


/*Used as an input parameter for Game playTurn() method. */
public class TurnData 
{
	/*Acceptable player actions*/
	private String drawCard = "DRAW_CARD";
	private String playCard = "PLAY_CARD";
	private String setColor = "SET_COLOR";
	private String red = "RED";
	private String blue = "BLUE";
	private String green = "GREEN";
	private String yellow = "YELLOW";
	private String action;
	private String color;
	
	/*Card to be played*/
	Card card;
	
	
	/*Set this if the player attempts to draw a card when nothing else can be played.*/
	public void setActionDrawCard()
	{
		action = drawCard;
	}
	/*Set this if the player attempts to play a card.*/
	public void setActionPlayCard()
	{
		action = playCard;
	}
	/*Set this if the player is changing the game play color.*/
	public void setActionSetColor()
	{
		action = setColor;
	}

	public String getAction()
	{
		return action;
	}
	
	/*Set the new play color for wild card*/
	public void setBlue()
	{
		color = blue;
	}
	
	public void setRed()
	{
		color = red;
	}
	
	public void setGreen()
	{
		color = green;
	}
	
	public void setYellow()
	{
		color = yellow;
	}
	
	/*Get the new color for wild card*/
	public String getColor()
	{
		return color;
	}
	
	/*Set this with the card the player is attempting to play*/
	public void setCard(Card c)
	{
		card = c;
	}
	/*Get the card the player is attempting to play*/
	public Card getCard()
	{
		return card;
	}
	
	
}
