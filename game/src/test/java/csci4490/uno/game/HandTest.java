package csci4490.uno.game;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.intThat;

import org.junit.jupiter.api.Test;

class HandTest {
	
	private String red = "RED";
	private String blue = "BLUE";
	private String nine = "9";
	private String zero = "0";
	String none = "NONE";
	Card red1 = new RegularCard(red, nine);
	Card blue0 = new RegularCard(blue, zero);
	Card blue1 = new RegularCard(blue,nine);
	Card red0 = new RegularCard(red,zero);
	Card wildCard = new WildCard();
	Deck deck = new Deck(4);

	@Test
	void testAddCard() {
		
		Hand h = new Hand(deck);
		int hand_size = h.getNumCards();
		h.addCard(red1);
		int hand_size_added = h.getNumCards();
		
		assertTrue(hand_size == 0 && hand_size_added == 1);
	}

	@Test
	void testRemoveCard() {
		
		Hand h = new Hand(deck);
		
		h.addCard(red1);
		int hand_size_added = h.getNumCards();
		h.removeCard(red1);
		int hand_size_removed = h.getNumCards();
		assertTrue(hand_size_removed == 0 && hand_size_added == 1);
		
	}

	@Test
	void testDiscard() {
		
		Hand h = new Hand(deck);
		
		h.addCard(red1);
		int hand_size_added = h.getNumCards();
		h.discard(red1);
		int hand_size_removed = h.getNumCards();
		assertTrue(hand_size_removed == 0 && hand_size_added == 1);
	}

	@Test
	void testGetScore() {
		Hand h = new Hand(deck);
		h.addCard(red1);
		h.addCard(blue0);
		h.addCard(wildCard);
		h.addCard(blue1);
		
		int score = h.getScore();
		assertTrue(score == 68);
	}

	@Test
	void testGetNumCards() {
		Hand h = new Hand(deck);
		
		int empty = h.getNumCards();
		h.addCard(red1);
		h.addCard(blue0);
		h.addCard(wildCard);
		h.addCard(blue1);
		
		int numCards = h.getNumCards();
		assertTrue(empty == 0 && numCards == 4);
	}

	@Test
	void testGetCards() {
		assertNotNull(deck.getHands());
	}

}
