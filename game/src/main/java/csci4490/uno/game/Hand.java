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
	
	public void addCard(Card c)
	{
		cards.add(c);
		score += c.getScoreValue();
	}
	
	
		
	public void removeCard(Card c)
	{
		cards.remove(c);
		score -= c.getScoreValue();
		
	}
	
	public void discard(Card c)
	{
		removeCard(c);
		playDeck.discard(c);
		
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
