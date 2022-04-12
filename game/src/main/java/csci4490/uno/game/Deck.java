package csci4490.uno.game;

import java.util.ArrayList;
import java.util.Collections;

public class Deck 
{
	private ArrayList<Card> playDeck;
	private ArrayList<Card> discards;
	ArrayList<Hand> hands;
	private Hand hand1;
	private Hand hand2;
	private Hand hand3;
	private Hand hand4;
	private String red = "RED";
	private String blue = "BLUE";
	private String green = "GREEN";
	private String yellow = "YELLOW";
	private String drawTwo = "DRAW_TWO";
	private String skip = "SKIP";
	private String reverse = "REVERSE";
	private String wild = "WILD";
	private String zero = "0";
	private String one = "1";
	private String two = "2";
	private String three = "3";
	private String four = "4";
	private String five = "5";
	private String six = "6";
	private String seven = "7";
	private String eight = "8";
	private String nine = "9";
	
	
	public Deck()
	{
		playDeck = new ArrayList<Card>();
		discards = null;
		
		hands = new ArrayList<Hand>();
		hand1 = new Hand(this);
		hand2 = new Hand(this);
		hand3 = new Hand(this);
		hand4 = new Hand(this);
		hands.add(0,hand1);
		hands.add(1,hand2);
		hands.add(2,hand3);
		hands.add(3,hand4);
		
		playDeck.add(new RegularCard(red, zero));
		playDeck.add(new RegularCard(blue,zero));
		playDeck.add(new RegularCard(green,zero));
		playDeck.add(new RegularCard(yellow, zero));
		
		
		
		
		
		for (int i = 0; i < 4; i++)
		{
			playDeck.add(new Draw4Card(wild));
			playDeck.add(new WildCard(wild));
		}
		for (int i = 0; i < 2; i++)
		{
			playDeck.add(new DrawTwoCard(red, drawTwo));
			playDeck.add(new DrawTwoCard(blue, drawTwo));
			playDeck.add(new DrawTwoCard(green, drawTwo));
			playDeck.add(new DrawTwoCard(yellow, drawTwo));
			
			playDeck.add(new SkipCard(red, skip));
			playDeck.add(new SkipCard(blue, skip));
			playDeck.add(new SkipCard(green, skip));
			playDeck.add(new SkipCard(yellow, skip));
			
			playDeck.add(new ReverseCard(red, reverse));
			playDeck.add(new ReverseCard(blue, reverse));
			playDeck.add(new ReverseCard(green, reverse));
			playDeck.add(new ReverseCard(yellow, reverse));
			
			playDeck.add(new RegularCard(yellow, one));
			playDeck.add(new RegularCard(yellow, two));
			playDeck.add(new RegularCard(yellow, three));
			playDeck.add(new RegularCard(yellow, four));
			playDeck.add(new RegularCard(yellow, five));
			playDeck.add(new RegularCard(yellow, six));
			playDeck.add(new RegularCard(yellow, seven));
			playDeck.add(new RegularCard(yellow, eight));
			playDeck.add(new RegularCard(yellow, nine));
			
			playDeck.add(new RegularCard(red, one));
			playDeck.add(new RegularCard(red, two));
			playDeck.add(new RegularCard(red, three));
			playDeck.add(new RegularCard(red, four));
			playDeck.add(new RegularCard(red, five));
			playDeck.add(new RegularCard(red, six));
			playDeck.add(new RegularCard(red, seven));
			playDeck.add(new RegularCard(red, eight));
			playDeck.add(new RegularCard(red, nine));
			
			playDeck.add(new RegularCard(blue, one));
			playDeck.add(new RegularCard(blue, two));
			playDeck.add(new RegularCard(blue, three));
			playDeck.add(new RegularCard(blue, four));
			playDeck.add(new RegularCard(blue, five));
			playDeck.add(new RegularCard(blue, six));
			playDeck.add(new RegularCard(blue, seven));
			playDeck.add(new RegularCard(blue, eight));
			playDeck.add(new RegularCard(blue, nine));
			
			playDeck.add(new RegularCard(green, one));
			playDeck.add(new RegularCard(green, two));
			playDeck.add(new RegularCard(green, three));
			playDeck.add(new RegularCard(green, four));
			playDeck.add(new RegularCard(green, five));
			playDeck.add(new RegularCard(green, six));
			playDeck.add(new RegularCard(green, seven));
			playDeck.add(new RegularCard(green, eight));
			playDeck.add(new RegularCard(green, nine));
			
			
			
		}
		
		shuffle();
		
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
	
	public Card addToHand()
	{
		Card toReturn = playDeck.get(0);
		playDeck.remove(0);
		return toReturn;
		
	}
	
	public Boolean isEmpty()
	{
		if (playDeck.isEmpty())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
	public void shuffle()
	{
		//playDeck = discards;
		//discards = null;
		Collections.shuffle(playDeck);
	}
	
	public ArrayList<Hand> getHands()
	{
		
		
		return hands;
	}
	
	public ArrayList<Card> getDiscards()
	{
		return discards;
	}
	
	public ArrayList<Card> getPlayDeck()
	{
		return playDeck;
	}

}
