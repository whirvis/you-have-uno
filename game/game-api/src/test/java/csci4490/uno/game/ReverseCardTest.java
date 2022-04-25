package csci4490.uno.game;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ReverseCardTest {

	
	private String red = "RED";
	private String blue = "BLUE";
	private String nine = "9";
	private String zero = "0";
	String none = "NONE";
	Game game = new Game(2);
	Card red1 = new RegularCard(red, nine);
	Card blue0 = new RegularCard(blue, zero);
	Card blue1 = new RegularCard(blue,nine);
	Card red0 = new RegularCard(red,zero);
	Card wildCard = new WildCard(game);
	Card revRed = new ReverseCard(red);
	Card revBlue = new ReverseCard(blue);
	
	@Test
	void testApplyCardEffect() {
		assertTrue(revRed.applyCardEffect().equals("REVERSE"));
	}

	@Test
	void testMatchCard() {
		assertTrue(revRed.matchCard(red1));
	}
	
	@Test
	void testMatchCard1() {
		assertTrue(red1.matchCard(revRed));
	}
	
	@Test
	void testMatchCard2() {
		assertTrue(revRed.matchCard(revRed));
	}
	
	@Test
	void testMatchCard3() {
		assertTrue(revRed.matchCard(revBlue));
	}
	
	@Test
	void testMatchCard4() {
		assertFalse(revRed.matchCard(blue1));
	}
	
	@Test
	void testMatchCard5() {
		assertFalse(blue1.matchCard(revRed));
	}

	@Test
	void testMatchCard6() {
		assertTrue(blue1.matchCard(wildCard));
	}
	@Test
	void testMatchCard7() {
		game.setWildPlayColor(blue);
		assertFalse(wildCard.matchCard(revRed));
	}
	@Test
	void testGetValue6() {
		assertTrue(revRed.getValue().equals("REVERSE"));
	}
	
	@Test
	void testGetValue7() {
		assertFalse(revRed.getValue().equals("SKIP"));
	}

	@Test
	void testGetScoreValue() {
		assertTrue(revRed.getScoreValue() == 20);
	}

}
