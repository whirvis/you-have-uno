package csci4490.uno.game;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PlayerTest {
	
	Deck deck = new Deck(2);
	Hand hand = deck.getHands().get(0);
	Player player = new Player(hand, deck);
	
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
	

	@Test
	void testGetScore() {
		
		int score_empty = player.getScore();
		hand.addCard(wildCard);
		hand.addCard(blue0);
		hand.addCard(red1);
		
		int score_added = player.getScore();
		assertTrue(score_empty == 0 && score_added == 59);
	
	}

	

}
