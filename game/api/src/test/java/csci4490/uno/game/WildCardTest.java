package csci4490.uno.game;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class WildCardTest {
	
	private String red = "RED";
	private String blue = "BLUE";
	private String nine = "9";
	private String zero = "0";
	private String wild = "WILD";
	String none = "NONE";
	Game game = new Game(4);
	Card red1 = new RegularCard(red, nine);
	Card blue0 = new RegularCard(blue, zero);
	Card blue1 = new RegularCard(blue,nine);
	Card red0 = new RegularCard(red,zero);
	Card wildCard = new WildCard(game);
	
	
	

	@Test
	void testMatchCard() {
		game.setWildPlayColor(red);
		assertTrue(wildCard.matchCard(red1));
	}
	
	
	
	void testMatchCard2() {
		assertTrue(wildCard.matchCard(wildCard));
	}
	
	@Test
	void testMatchCard3() {
		game.setWildPlayColor(blue);
		assertFalse(wildCard.matchCard(red1));
	}
	

	@Test
	void testGetValue() {
		assertTrue(wildCard.getValue().equals(wild));
	}

	@Test
	void testGetScoreValue() {
		assertTrue(wildCard.getScoreValue() == 50);
	}

}
