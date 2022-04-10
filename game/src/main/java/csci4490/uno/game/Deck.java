package csci4490.uno.game;

import java.util.ArrayList;

public class Deck 
{
	private ArrayList<Card> playDeck;
	private ArrayList<Card> discards;
	private Hand hand1;
	private Hand hand2;
	private Hand hand3;
	private Hand hand4;
	
	public Deck()
	{
		playDeck = new ArrayList<Card>();
		discards = null;
		
	}
	
	public void dealCards()
	{
		for (int i = 0; i < 7; i++)
		{
			hand1.addCard(playDeck.get(0));
			playDeck.remove(0);
			hand2.addCard(playDeck.get(0));
			playDeck.remove(0);
			hand3.addCard(playDeck.get(0));
			playDeck.remove(0);
			hand4.addCard(playDeck.get(0));
			playDeck.remove(0);
		}	
		
	}
	
	public void discard(Card c)
	{
		discards.add(c);
	}
	
	public void shuffle()
	{
		
	}

}
