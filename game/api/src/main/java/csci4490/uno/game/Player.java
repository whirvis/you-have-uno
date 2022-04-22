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
	
	public void playCard(Card c) throws IllegalMoveException
	{
		
		if (deck.getFaceUp().matchCard(c) == true && (hand.getCards().contains(c)))
		{
			hand.discard(c);
		}
		else
		{
			throw new IllegalMoveException("CARD DOES NOT MATCH");
		}
		
	}
	
	public Card drawCardFromDeck() 
	{
		Card c = deck.getTopCard();
		try {
			deck.addToHand(this);
		} catch (IllegalMoveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return c;
		
	}
	
	public Hand getHand()
	{
		return hand;
	}
	
	
}
