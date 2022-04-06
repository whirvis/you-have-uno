package game_components;

import java.util.ArrayList;



public class Hand 
{
	private ArrayList<Card> cards;
	
	public Hand()
	{
		cards = null;
	}
	
	public void addCard(Card c)
	{
		cards.add(c);
	}
		
	public void removeCard(Card c)
	{
		boolean found = false;
		int i = 0;
		while (found == false && i < cards.size())
		{
			if (cards.get(i).equals(c))
			{
				cards.remove(i);
				found = true;
			}
			
			i++;
			
		}
		
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
