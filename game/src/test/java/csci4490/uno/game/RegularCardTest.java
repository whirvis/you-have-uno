package csci4490.uno.game;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegularCardTest {

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
	void testMatchCard1() {
		assertTrue(red1.matchCard(blue1));
	}
	
	@Test
	void testMatchCard2() {
		assertTrue(red1.matchCard(red0));
	}
	
	@Test
	void testMatchCard3() {
		assertTrue(red1.matchCard(wildCard));
	}
	
	@Test
	void testMatchCard4() {
		assertFalse(red1.matchCard(blue0));
	}
	
	@Test
	void testMatchCard5() {
		assertFalse(blue0.matchCard(red1));
	}

	@Test
	void testApplyCardEffect() {
		assertTrue(blue0.applyCardEffect().equals(none));
	}
	
	
	@Test
	void testGetColor() {
		assertTrue(blue0.getColor().equals(blue));
	}
	
	@Test
	void testGetColor2() {
		assertTrue(red1.getColor().equals(red));
	}


	@Test
	void testGetScoreValue() {
		assertTrue(red1.getScoreValue() == 9);
	}

	
}
