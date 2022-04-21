package csci4490.uno.game;

import java.util.ArrayList;


public class Game implements UnoInterface
{
	private Deck deck;
	private ArrayList<Player>players;
	private ArrayList<Integer>playerScores;
	private int numberOfPlayers;
	private String effect;
	private Player currentPlayer;
	private String playColor;
	private int playerIndex;
	private String direction;
	private String left = "LEFT";
	private String right = "RIGHT";
	private State state;
	private String drawCard = "DRAW_CARD";
	private String playCard = "PLAY_CARD";
	private String setColor = "SET_COLOR";
	private String draw2 = "DRAW_TWO";
	private String draw4 = "WILD_DRAW_FOUR";
	private String skip = "SKIP";
	private String reverse = "REVERSE";
	private String wild = "WILD";
	
	public Game(int numPlayers)
	{
		
		numberOfPlayers = numPlayers;
		players = new ArrayList<Player>();
		playerScores = new ArrayList<Integer>();
		deck = new Deck(numberOfPlayers, this);
		direction = left;
		playerIndex = 0;
		state = new State(deck.getHands());
		
		for (int i = 0; i < numberOfPlayers; i++)
		{
			players.add(new Player(deck.getHands().get(i), deck));
		}	
		
	}
	
	/*Start a game of Uno, return initial state of the board.*/
	public State init()
	{
		deck.dealCards();
		deck.flipCard();
		currentPlayer = players.get(playerIndex);
		
		state.setFaceUp(deck.getFaceUp());
		state.setNextPlayer(playerIndex);
		return state;
	}
	/*return hands of all players*/
	public ArrayList<Hand> getHands()
	{
		return deck.getHands();
	}
	
	/*Make a turn on behalf of the current player. */
	public void playTurn(TurnData data) throws IllegalMoveException
	{ 
		String move = data.getAction();
		
		
		//add two cards to the player's hand and move to the next player
		if (effect.equals(draw2))
		{
			currentPlayer.drawCardFromDeck();
			currentPlayer.drawCardFromDeck();
		}
		//add four cards to the player's hand and move to the next player
		else if (effect.equals(draw4))
		{
			currentPlayer.drawCardFromDeck();
			currentPlayer.drawCardFromDeck();
			currentPlayer.drawCardFromDeck();
			currentPlayer.drawCardFromDeck();
		}
		//set the wild game play color to user's chosen color
		else if (move.equals(setColor))
		{
			
			setWildPlayColor(data.getColor());
			
		}
		//play the card and throw an error if it's unplayable
		else if (move.equals(playCard))
		{
			//TO DO: check if anything in hand can be played
			//CHECK FOR OTHER CARDS THAT CAN BE PLAYED IF CARD IS DRAW4
			try
			{
				currentPlayer.playCard(data.getCard());
				setCardEffect(data.getCard().applyCardEffect()); //card effects include draw4, draw2, skip, reverse
				//if the card is a wild card, set flag for player to set the color
				if (data.getCard().toString().equals("WILD_4") || data.getCard().toString().equals(wild))
				{
					state.setColorTrue();
				}
			}
			catch(IllegalMoveException e)
			{
				throw e;
			}
			
			
		}
		//draw a face down card from the deck
		else if (move.equals(drawCard))
		{
			currentPlayer.drawCardFromDeck();
		}
		
		//put new hand in state
		state.setPlayerHand(currentPlayer.getHand());
		
		//if the deck is empty, flip it and shuffle, tell UI that it has been flipped
		if (deck.isEmpty() == true)
		{
			deck.shuffle();
			state.setDeckTurnedOver(true);
		}
		else
		{
			state.setDeckTurnedOver(false);
		}
		
		state.setNextPlayer(getNextPlayer());
		
		//set face up card
		state.setFaceUp(deck.getFaceUp());
		
		if (checkForWinner() == true)
		{
			state.setGameOver();
		}
		
		
	}
	
	/*Return the current state of the game relevant to the UI*/
	public State getState()
	{
		return state;
	}
	
	/*This gets the index of the next player for the state*/
	public int getNextPlayer()
	{
		if (effect.equals("REVERSE"))
		{
			reverseDirection();
			rotateToNextPlayer();
		}
		
		else if (effect.equals("SKIP"))
		{
			skipNextPlayer();
		}
		
		else
		{
			rotateToNextPlayer();
		}
		return playerIndex;
	}
	
	
	/*Set the new play color when wild card has been played*/
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
	/*Effects include: NONE, DRAW2, WILD, WILD DRAW 4, SKIP, REVERSE*/
	private void setCardEffect(String effect)
	{
		this.effect = effect;
	}
	
	/*This reverses the play direction of the game*/
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
	/*This selects the next player using the index generated in getPlayerIndex if nobody is being skipped*/
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
	/*This selects the next player if a player is to be skipped*/
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
	
	
	
	/*Return an arraylist of current player scores*/
	public ArrayList<Integer> getScores()
	{
		for (int i = 0; i < numberOfPlayers; i++)
		{
			playerScores.add(i, players.get(i).getScore());
		}
		return playerScores;
	}
	
	/*Checks current player hand for empty, empty means player wins*/
	private Boolean checkForWinner()
	{
		if (currentPlayer.getHand().getNumCards() == 0)
		{
			return true;
		}
		return false;
	}
	

	
	public ArrayList<Player> getPlayers()
	{
		return players;
	}
	
	public Deck getDeck()
	{
		return deck;
	}

	
	

	
	

}
