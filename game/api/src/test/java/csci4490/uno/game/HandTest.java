package csci4490.uno.game;

import static org.junit.jupiter.api.Assertions.*;

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
	Game game = new Game(3);
	Card wildCard = new WildCard(game);
	Deck deck = new Deck(4,game);

	@Test
	void testAddCard() {
		
		Hand h = new Hand(deck);
		int hand_size = h.getNumCards();
		try {
			h.addCard(red1);
		} catch (IllegalMoveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int hand_size_added = h.getNumCards();
		
		assertTrue(hand_size == 0 && hand_size_added == 1);
	}

	@Test
	void testRemoveCard() {
		
		Hand h = new Hand(deck);
		
		try {
			h.addCard(red1);
		} catch (IllegalMoveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int hand_size_added = h.getNumCards();
		h.removeCard(red1);
		int hand_size_removed = h.getNumCards();
		assertTrue(hand_size_removed == 0 && hand_size_added == 1);
		
	}

	@Test
	void testDiscard() {
		
		Hand h = new Hand(deck);
		
		try {
			h.addCard(red1);
		} catch (IllegalMoveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int hand_size_added = h.getNumCards();
		h.discard(red1);
		int hand_size_removed = h.getNumCards();
		assertTrue(hand_size_removed == 0 && hand_size_added == 1);
	}

	@Test
	void testGetScore() {
		Hand h = new Hand(deck);
		try {
			h.addCard(red1);
		} catch (IllegalMoveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			h.addCard(blue0);
		} catch (IllegalMoveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			h.addCard(wildCard);
		} catch (IllegalMoveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			h.addCard(blue1);
		} catch (IllegalMoveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int score = h.getScore();
		assertTrue(score == 68);
	}

	@Test
	void testGetNumCards() {
		Hand h = new Hand(deck);
		
		int empty = h.getNumCards();
		try {
			h.addCard(red1);
		} catch (IllegalMoveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			h.addCard(blue0);
		} catch (IllegalMoveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			h.addCard(wildCard);
		} catch (IllegalMoveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			h.addCard(blue1);
		} catch (IllegalMoveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int numCards = h.getNumCards();
		assertTrue(empty == 0 && numCards == 4);
	}

	@Test
	void testGetCards() {
		assertNotNull(deck.getHands());
	}

}
