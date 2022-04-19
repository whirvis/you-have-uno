package csci4490.uno.game;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SkipCardTest {

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
	Card revRed = new SkipCard(red);
	Card revBlue = new SkipCard(blue);
	Card reverseRed = new ReverseCard(red);
	
	
	@Test
	void testApplyCardEffect() {
		assertTrue(revRed.applyCardEffect().equals("SKIP"));
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
		assertTrue(revRed.matchCard(reverseRed));
	}
	
	@Test
	void testMatchCard7() {
		assertTrue(reverseRed.matchCard(revRed));
	}
	
	@Test
	void testMatchCard8() {
		assertTrue(revRed.matchCard(revBlue));
	}
	
	@Test
	void testMatchCard9() {
		assertFalse(revRed.matchCard(blue1));
	}
	
	@Test
	void testMatchCard10() {
		assertTrue(revRed.matchCard(wildCard));
	}
	
	
	@Test
	void testGetValue() {
		assertFalse(revRed.getValue().equals("REVERSE"));
	}
	
	@Test
	void testGetValue7() {
		assertTrue(revRed.getValue().equals("SKIP"));
	}

	@Test
	void testGetScoreValue() {
		assertTrue(revRed.getScoreValue() == 20);
	}

}
