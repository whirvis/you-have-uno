package csci4490.uno.game;

public class Player 
{
	private Hand hand;
	private int score;
	
	public Player(Hand hand)
	{
		this.hand = hand;
		score = 0;
		 
	}
	
	public int getScore()
	{
		score = hand.getScore();
		return score;
	}
	
	
}
