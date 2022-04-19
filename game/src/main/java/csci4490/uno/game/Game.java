package csci4490.uno.game;

import java.util.ArrayList;

public class Game 
{
	private Deck deck;
	private ArrayList<Player>players;
	private ArrayList<Integer>playerScores;
	private int numberOfPlayers;
	private int numberOfGames;
	private String effect;
	private Player currentPlayer;
	private String playColor;
	private Card faceUp;
	private int playerIndex;
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
	
	private Boolean setWildPlayColor(String color)
	{
		if (color.equals("GREEN") || color.equals("BLUE") || color.equals("RED") || color.equals("YELLOW"))
		{
			playColor = color;
			return true;
		}
		
		return false;
	}
	
	private String getWildPlayColor()
	{
		return playColor;
	}
	
	private void setCardEffect(String effect)
	{
		this.effect = effect;
	}
	
	
	private void reverseDirection()
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
	
	private void rotateToNextPlayer()
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
	
	private void skipNextPlayer()
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
	
	
	private ArrayList<Integer> getCurrentScores()
	{
		for (int i = 0; i < numberOfPlayers; i++)
		{
			playerScores.add(i, players.get(i).getScore());
		}
		return playerScores;
	}
		
	private Boolean checkForWinner()
	{
		if (currentPlayer.getHand().getNumCards() == 0)
		{
			return true;
		}
		return false;
	}
	

	
	

}
