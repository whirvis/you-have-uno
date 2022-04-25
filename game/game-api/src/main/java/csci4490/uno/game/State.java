package csci4490.uno.game;

import java.util.ArrayList;

/*This class packs up relevant information about the state of the game for updating the UI 
 * and tracking player turns.*/
public class State 
{
	private Hand playerHand;
	private Card faceUp;
	private int nextPlayerIndex;
	private Boolean playerSetWildColor;
	private Boolean gameOver = false;
	private ArrayList<Hand> hands;
	private Boolean deckTurnedOver = false;

	
	public State(ArrayList<Hand> hands)
	{
		this.hands = hands;
		playerSetWildColor = false;
	}
	
	/*updated hand of the player whose turn just ended*/
	public void setPlayerHand(Hand h)
	{
		playerHand = h;
	}
	/*updated face up card at the end of this turn*/
	public void setFaceUp(Card c)
	{
		faceUp = c;
	}
	/*index of next player*/
	public void setNextPlayer(int p)
	{
		nextPlayerIndex = p;
	}
	/*setting this flag to true means the game has been won*/
	public void setGameOver()
	{
		gameOver = true;
	}
	
	public Boolean getDeckTurnedOver()
	{
		return deckTurnedOver;
	}
	
	/*if playerSetWildColor is true, the UI should prompt the player to choose the game play color.
	 * Triggered when a wild card is played*/
	public void setColorTrue()
	{
		playerSetWildColor = true;
	}
	
	/*if deck has been used up and shuffled, update UI*/
	public void setDeckTurnedOver(Boolean b)
	{
		deckTurnedOver = b;
	}
	
	
	public void setColorFalse()
	{
		playerSetWildColor = false;
	}
	
	public Boolean getPlayerSetColor()
	{
		return playerSetWildColor;
	}
	
	/*return updated hand of the current player*/
	public Hand getUpdatedPlayerHand()
	{
		return playerHand;
	}
	/*return player number of next player*/
	public int getNextPlayerIndex()
	{
		return nextPlayerIndex;
	}
	
	/*return new face up card*/
	public Card getFaceUp()
	{
		return faceUp;
	}
	
	/*True indicates that the game has been won*/
	public Boolean getGameOver()
	{
		return gameOver;
	}
	
	public ArrayList<Hand> getAllHands()
	{
		return hands;
	}
	
	
	
	

}
