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
	Card red1 = new RegularCard(red, nine);
	Card blue0 = new RegularCard(blue, zero);
	Card blue1 = new RegularCard(blue,nine);
	Card red0 = new RegularCard(red,zero);
	Card wildCard = new WildCard();
	
	
	

	@Test
	void testMatchCard() {
		assertTrue(wildCard.matchCard(red1));
	}
	
	@Test
	void testMatchCard1() {
		wildCard.setColor(blue);
		assertFalse(wildCard.matchCard(red0));
	}
	
	void testMatchCard2() {
		assertTrue(wildCard.matchCard(wildCard));
	}
	
	@Test
	void testSetColor() {
		wildCard.setColor(blue);
		assertTrue(wildCard.getColor().equals(blue));
	}

	
	void testMatchCard3() {
		wildCard.setColor(blue);
		assertFalse(wildCard.matchCard(red1));
	}
	
	void testMatchCard4() {
		wildCard.setColor(blue);
		assertTrue(wildCard.matchCard(blue0));
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
