package csci4490.uno.game;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class DeckTest {
	
	
	Deck deck = new Deck(3);
	ArrayList<Hand> hands = deck.getHands();
	Player player = new Player(hands.get(0), deck);

	@Test
	void setUp()
	{
		assertTrue(deck.getPlayDeck().size() == 108);
	}
	
	@Test
	void testDealCards() {
		
		deck.dealCards();
		assertTrue(deck.getPlayDeck().size() == 87);
		
	}

	@Test
	void testDiscard() {
		Deck deck = new Deck(3);
		deck.discard(deck.getPlayDeck().get(0));
		assertTrue(deck.getDiscards().size() == 1);
	}

	@Test
	void testRemoveCard() {
		Deck deck = new Deck(3);
		deck.removeCard(deck.getPlayDeck().get(0));
		assertTrue(deck.getPlayDeck().size() == 107);
	}

	@Test
	void testAddToHand() {
		Deck deck = new Deck(3);
		deck.addToHand(player);
		assertTrue(player.getHand().getNumCards() == 1 && deck.getPlayDeck().size() == 107);
	}


	@Test
	void testGetHands() {
		assertTrue(deck.getHands().size() == 3);
	}

	@Test
	void testGetDiscards() {
		Deck deck = new Deck(3);
		int size_1 = deck.getDiscards().size();
		for (int i = 0; i < deck.getPlayDeck().size(); i++)
		{
			Card c = deck.getPlayDeck().get(i);
			deck.discard(c);
		}
		int size_2 = deck.getDiscards().size();
		
		assertTrue(size_1 == 0 && size_2 == 108);
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
