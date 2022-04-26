package csci4490.uno.game;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class DeckTest {
	
	Game game = new Game(3);
	Deck deck = new Deck(3,game);
	ArrayList<Hand> hands = deck.getHands();
	Player player = new Player(hands.get(0), deck);

	@Test
	void setUp()
	{
		assertTrue(deck.getPlayDeck().size() == 107);
	}
	
	@Test
	void testPlayergetHand()
	{
		Player p = new Player(hands.get(0),deck);
		assertTrue(p.getHand().equals(deck.getHands().get(0)));
	}
	
	@Test
	void testPlayerDrawCardFromDeck()
	{
		Card c = deck.getTopCard();
		Card c2 = player.drawCardFromDeck();
		
		assertTrue(c.equals(c2));
	}
	
	@Test
	void testDealCards() {
		
		deck.dealCards();
		assertTrue(deck.getPlayDeck().size() == 86);
		
	}

	@Test
	void testDiscard() {
		Deck deck = new Deck(3,game);
		deck.discard(deck.getPlayDeck().get(0));
		assertTrue(deck.getDiscards().size() == 2);
	}

	@Test
	void testRemoveCard() {
		Deck deck = new Deck(3,game);
		deck.removeCard(deck.getPlayDeck().get(0));
		assertTrue(deck.getPlayDeck().size() == 106);
	}

	@Test
	void testAddToHand() {
		Deck deck = new Deck(3,game);
		try {
			deck.addToHand(player);
		} catch (IllegalMoveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(player.getHand().getNumCards() == 1);
	}


	@Test
	void testGetDiscards() {
		Deck deck = new Deck(3,game);
		int size_1 = deck.getDiscards().size();
		for (int i = 0; i < deck.getPlayDeck().size(); i++)
		{
			Card c = deck.getPlayDeck().get(i);
			deck.discard(c);
		}
		int size_2 = deck.getDiscards().size();
		
		assertTrue(size_1 == 1 && size_2 == 108);
	}

	@Test
	void testGetPlayDeck() {
		assertTrue(deck.getHands().size() == 3);
	}
	
	@Test
	void testFlipCard()
	{
		Card c = deck.getPlayDeck().get(0);
		deck.flipCard();
		assertTrue(c.equals(deck.getDiscards().get(deck.getDiscards().size()-1)));
	}

}