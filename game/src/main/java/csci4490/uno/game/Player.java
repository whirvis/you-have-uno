package csci4490.uno.game;

public class Player 
{
	private Hand hand;
	private int score;
	private Deck deck;
	
	public Player(Hand hand, Deck deck)
	{
		this.hand = hand;
		this.deck = deck;
		score = 0;
		 
	}
	
	public int getScore()
	{
		score = hand.getScore();
		return score;
	}
	
	public Boolean playCard(Card c)
	{
		
		if (deck.getFaceUp().matchCard(c) == true && hand.getCards().contains(c))
		{
			hand.discard(c);
			return true;
		}
		
		return false;
		
		
	}
	
	public void drawCardFromDeck()
	{
		deck.addToHand(this);
	}
	
	public Hand getHand()
	{
		return hand;
	}
	
	
}
