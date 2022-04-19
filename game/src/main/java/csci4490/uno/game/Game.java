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
	private ArrayList<Integer>tournamentScoresRunning;
	private Player currentPlayer;
	private String playColor;
	private Card faceUp;
	
	public Game(int numPlayers, int num_games)
	{
		numberOfPlayers = numPlayers;
		numberOfGames = num_games;
		players = new ArrayList<Player>();
		playerScores = new ArrayList<Integer>();
		tournamentScoresRunning = new ArrayList<Integer>(0);
		deck = new Deck(numberOfPlayers);
		deck.dealCards();
		
		for (int i = 0; i < numberOfPlayers; i++)
		{
			players.add(new Player(deck.getHands().get(i), deck));
		}
		
		
	}
	
	public void start()
	{
		faceUp = deck.getFaceUp();
		currentPlayer = players.get(0);
		
		
	}
	
	
	public String playTurn(Card c)
	{
		return null;
	}
	
	public void setWildPlayColor(String color)
	{
		playColor = color;
	}
	
	public String getWildPlayColor()
	{
		return playColor;
	}
	
	private void setCardEffect(String effect)
	{
		effect = effect;
	}
	
	
	private void reverseDirection()
	{
		
	}
	
	private void rotateToNextPlayer()
	{
		
	}
	
	private void skipNextPlayer()
	{
		
	}
	
	
	public ArrayList<Integer> getCurrentScores()
	{
		for (int i = 0; i < numberOfPlayers; i++)
		{
			playerScores.add(i, players.get(i).getScore());
		}
		return playerScores;
	}
	
	public ArrayList<Integer> getTournamentScores()
	{
		return tournamentScoresRunning;
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
