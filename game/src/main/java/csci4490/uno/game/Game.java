package csci4490.uno.game;

import java.util.ArrayList;

public class Game 
{
	public Deck deck;
	public ArrayList<Player>players;
	private ArrayList<Integer>playerScores;
	private int numberOfPlayers;
	private int numberOfGames;
	private String effect;
	private Player currentPlayer;
	private String playColor;
	private Card faceUp;
	public int playerIndex;
	private String direction;
	private String left = "LEFT";
	private String right = "RIGHT";
	
	
	public Game(int numPlayers)
	{
		numberOfPlayers = numPlayers;
		players = new ArrayList<Player>();
		playerScores = new ArrayList<Integer>();
		deck = new Deck(numberOfPlayers);
		deck.dealCards();
		direction = left;
		playerIndex = 0;
		
		for (int i = 0; i < numberOfPlayers; i++)
		{
			players.add(new Player(deck.getHands().get(i), deck));
		}
		
		
	}
	
	public void start()
	{
		deck.flipCard();
		faceUp = deck.getFaceUp();
		currentPlayer = players.get(playerIndex);
	
		
	}
	
	
	public String playTurn(Card c)
	{
		//TO DO
		return null;
	}
	
	public Boolean setWildPlayColor(String color)
	{
		if (color.equals("GREEN") || color.equals("BLUE") || color.equals("RED") || color.equals("YELLOW"))
		{
			playColor = color;
			return true;
		}
		
		return false;
	}
	
	public String getWildPlayColor()
	{
		return playColor;
	}
	
	public void setCardEffect(String effect)
	{
		this.effect = effect;
	}
	
	
	public void reverseDirection()
	{
		if (direction.equals(left))
		{
			direction = right;
		}
		else
		{
			direction = left;
		}
	}
	
	public void rotateToNextPlayer()
	{
		
		if (direction.equals(left))
		{
			playerIndex = (playerIndex + 1)% numberOfPlayers;
		}
		else
		{
			playerIndex = (playerIndex + (numberOfPlayers -1)) % numberOfPlayers;
		}
		
		
		currentPlayer = players.get(playerIndex);
		
	}
	
	public void skipNextPlayer()
	{
		if(direction.equals(left))
		{
			playerIndex = (playerIndex + 2)% numberOfPlayers;
		}
		else
		{
			playerIndex = (playerIndex + (numberOfPlayers-2) % numberOfPlayers);
		}
		
		currentPlayer = players.get(playerIndex);
		
	}
	
	
	public ArrayList<Integer> getCurrentScores()
	{
		for (int i = 0; i < numberOfPlayers; i++)
		{
			playerScores.add(i, players.get(i).getScore());
		}
		return playerScores;
	}
		
	public Boolean checkForWinner()
	{
		if (currentPlayer.getHand().getNumCards() == 0)
		{
			return true;
		}
		return false;
	}
	

	
	

}
