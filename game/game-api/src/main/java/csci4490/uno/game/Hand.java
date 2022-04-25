package csci4490.uno.game;

import java.util.ArrayList;



public class Hand 
{
	private ArrayList<Card> cards;
	private Deck playDeck;
	private int score;
	
	public Hand(Deck playDeck)
	{
		cards = new ArrayList<Card>();
		this.playDeck = playDeck;
		score = 0;
	}
	
	public void addCard(Card c) throws IllegalMoveException
	{
		if (cards.size() > 15)
		{
			throw new IllegalMoveException("hand is full");
		}
		cards.add(c);
		score += c.getScoreValue();

	}
	
	
		
	public Boolean removeCard(Card c)
	{
		if (cards.contains(c))
		{
			cards.remove(c);
			score -= c.getScoreValue();
			return true;
			
		}
		
		return false;
		
		
	}
	
	public void discard(Card c)
	{
		//if (cards.contains(c))
		//{
			removeCard(c);
			playDeck.discard(c);
			//return true;
			
		//}
		
		//return false;
		
		
	}
	
	public Boolean isEmpty()
	{
		if (cards.isEmpty())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public int getScore()
	{
		
		return score;
	}
	
	public int getNumCards()
	{
		return cards.size();
	}
	public ArrayList<Card> getCards()
	{
		return cards;
	}
}
