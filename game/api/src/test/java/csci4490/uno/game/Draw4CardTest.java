package csci4490.uno.game;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Draw4CardTest {
	
	private String red = "RED";
	private String blue = "BLUE";
	private String nine = "9";
	private String zero = "0";
	String wild = "WILD";
	String drawFour = "WILD_DRAW_FOUR";
	Card red1 = new RegularCard(red, nine);
	Card blue0 = new RegularCard(blue, zero);
	Card blue1 = new RegularCard(blue,nine);
	Card red0 = new RegularCard(red,zero);
	Card draw4 = new Draw4Card();


	@Test
	void testApplyCardEffect() {
		assertTrue(draw4.applyCardEffect().equals(drawFour));
	}

	@Test
	void testSetColor() {
		draw4.setColor(blue);
		assertTrue(draw4.getColor().equals(blue));
	}

	@Test
	void testMatchCard() {
		draw4.setColor(blue);
		assertFalse(draw4.matchCard(red0));
	}
	
	@Test
	void testMatchCard1() {
		draw4.setColor(null);
		assertTrue(draw4.matchCard(draw4));
	}
	
	@Test
	void testMatchCard2() {
		draw4.setColor(null);
		assertTrue(blue0.matchCard(draw4));
	}



	@Test
	void testGetValue() {
		assertTrue(draw4.getValue().equals(wild));
	}

	@Test
	void testGetScoreValue() {
		assertTrue(draw4.getScoreValue() == 50);
	}

}
