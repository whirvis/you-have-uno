package csci4490.uno.game;

import java.util.ArrayList;
import java.util.Collections;

public class Deck 
{
	private ArrayList<Card> playDeck;
	private ArrayList<Card> discards;
	ArrayList<Hand> hands;
	int numHands;
	Card faceUp;
	Game game;
	
	public Deck(int numHands, Game game)
	{
		final String red = "RED";
		final String blue = "BLUE";
		final String green = "GREEN";
		final String yellow = "YELLOW";
		final String drawTwo = "DRAW_TWO";
		final String skip = "SKIP";
		final String reverse = "REVERSE";
		final String wild = "WILD";
		final String zero = "0";
		final String one = "1";
		final String two = "2";
		final String three = "3";
		final String four = "4";
		final String five = "5";
		final String six = "6";
		final String seven = "7";
		final String eight = "8";
		final String nine = "9";
		playDeck = new ArrayList<Card>();
		discards = new ArrayList<Card>();
		this.game = game;
		
		hands = new ArrayList<Hand>();
		this.numHands = numHands;

		for (int i = 0; i < numHands; i++)
		{
			hands.add(new Hand(this));
		}

		playDeck.add(new RegularCard(red, zero));
		playDeck.add(new RegularCard(blue,zero));
		playDeck.add(new RegularCard(green,zero));
		playDeck.add(new RegularCard(yellow, zero));

		
		for (int i = 0; i < 4; i++)
		{
			playDeck.add(new Draw4Card(game));
			playDeck.add(new WildCard(game));
		}
		for (int i = 0; i < 2; i++)
		{
			playDeck.add(new DrawTwoCard(red, drawTwo));
			playDeck.add(new DrawTwoCard(blue, drawTwo));
			playDeck.add(new DrawTwoCard(green, drawTwo));
			playDeck.add(new DrawTwoCard(yellow, drawTwo));
			
			playDeck.add(new SkipCard(red));
			playDeck.add(new SkipCard(blue));
			playDeck.add(new SkipCard(green));
			playDeck.add(new SkipCard(yellow));
			
			playDeck.add(new ReverseCard(red));
			playDeck.add(new ReverseCard(blue));
			playDeck.add(new ReverseCard(green));
			playDeck.add(new ReverseCard(yellow));
			
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
		
		
		//Collections.shuffle(playDeck);
		flipCard();
		
	}
	
	public void dealCards()
	{
		for (int i = 0; i < 7; i++)
		{
			for (int j = 0; j < numHands; j++)
			{
				try {
					hands.get(j).addCard(playDeck.get(0));
				} catch (IllegalMoveException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				playDeck.remove(0);
			
				
			}
		}	
		
	}
	
	public void flipCard()
	{
		Card c = playDeck.get(0);
		playDeck.remove(c);
		discard(c);
		game.setCardEffect(c.applyCardEffect());
	}
	
	public void discard(Card c)
	{
		
		discards.add(c);
		faceUp = c;
		game.setCardEffect(c.applyCardEffect());
		
		
	}
	
	public void removeCard(Card c)
	{
		playDeck.remove(c);
	}
	
	public void addToHand(Player p) throws IllegalMoveException
	{
		if (this.isEmpty() == true)
		{
			shuffle();
			
		}

		Card c = getTopCard();
		try {
			p.getHand().addCard(c);
		} catch (IllegalMoveException e) {
			// TODO Auto-generated catch block
			throw e;
		}
		removeCard(c);
		//flipCard();
		
		
	
	}
	
	public Boolean isEmpty()
	{
		if (playDeck.size() == 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public Card getTopCard()
	{
		if (isEmpty())
		{
			shuffle();
		}
		return playDeck.get(0);
	}
	
	
	public void shuffle()
	{
		playDeck = discards;
		discards = new ArrayList<Card>();
		Collections.shuffle(playDeck);
	}
	
	public Card getFaceUp()
	{
		return faceUp;
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
