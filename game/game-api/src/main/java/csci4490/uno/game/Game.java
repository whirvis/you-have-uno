package csci4490.uno.game;

import java.util.ArrayList;


public class Game implements UnoInterface
{
	private Deck deck;
	private ArrayList<Player>players;
	private ArrayList<Integer>playerScores;
	private int numberOfPlayers;
	private String effect = "NONE";
	private Player currentPlayer;
	private String playColor = "NONE";
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
	private String move;
	
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
		effect = deck.getFaceUp().applyCardEffect();
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
	
		if(data.getAction().equals(setColor))
		{
			setWildPlayColor(data.getColor());
			state.setPlayerHand(currentPlayer.getHand());
			state.setNextPlayer(getNextPlayer());
			if (deck.isEmpty())
			{
				deck.shuffle();
				state.setDeckTurnedOver(true);
			}
			else
			{
				state.setDeckTurnedOver(false);
			}
			state.setFaceUp(deck.getFaceUp());
			setCardEffect(deck.getFaceUp().applyCardEffect());
		}
		else if (effect.equals(draw2))
		{
			currentPlayer.drawCardFromDeck();
			currentPlayer.drawCardFromDeck();
			
			setCardEffect("NONE");
			state.setNextPlayer(getNextPlayer());
			state.setPlayerHand(currentPlayer.getHand());
			if (deck.isEmpty())
			{
				deck.shuffle();
				state.setDeckTurnedOver(true);
			}
			else
			{
				state.setDeckTurnedOver(false);
			}
			
			state.setFaceUp(deck.getFaceUp());
		}
		else if (effect.equals(draw4))
		{
			currentPlayer.drawCardFromDeck();
			currentPlayer.drawCardFromDeck();
			currentPlayer.drawCardFromDeck();
			currentPlayer.drawCardFromDeck();
			
			setCardEffect("NONE");
			state.setNextPlayer(getNextPlayer());
			state.setPlayerHand(currentPlayer.getHand());
			if (deck.isEmpty())
			{
				deck.shuffle();
				state.setDeckTurnedOver(true);
			}
			else
			{
				state.setDeckTurnedOver(false);
			}
			
			state.setFaceUp(deck.getFaceUp());
			
		}
		
		else if (data.getAction().equals(drawCard))
		{
			for (int i = 0; i < currentPlayer.getHand().getNumCards(); i++)
			{
				if (deck.getFaceUp().matchCard(currentPlayer.getHand().getCards().get(i)))
				{
					throw new IllegalMoveException("Illegal Draw: Card(s) in hand can be played.");
				}
			}
			Card c = currentPlayer.drawCardFromDeck();
			
			currentPlayer.playCard(c);
			state.setPlayerHand(currentPlayer.getHand());
			state.setNextPlayer(getNextPlayer());
			if (deck.isEmpty())
			{
				deck.shuffle();
				state.setDeckTurnedOver(true);
			}
			else
			{
				state.setDeckTurnedOver(false);
			}
			
			state.setFaceUp(deck.getFaceUp());
			
		}
		else if (data.getAction().equals(playCard))
		{
			
			if (data.getCard().toString().equals("WILD_4"))
			{
				for (int i = 0; i < currentPlayer.getHand().getNumCards(); i++)
				{
					if (deck.getFaceUp().matchCard(currentPlayer.getHand().getCards().get(i)) && (!data.getCard().toString().equals("WILD_4"))) //&& (!deck.getFaceUp().toString().equals("WILD")) ))
					{
						throw new IllegalMoveException("Cannot Play Draw 4: Other card(s) can be played.");
					}
				}
			}
			if (data.getCard().toString().equals(wild) || data.getCard().toString().equals("WILD_4"))
			{
				currentPlayer.playCard(data.getCard());
				state.setColorTrue();
				state.setPlayerHand(currentPlayer.getHand());
				state.setNextPlayer(playerIndex);


			}
			else
			{
				currentPlayer.playCard(data.getCard());
				state.setColorFalse();
				state.setPlayerHand(currentPlayer.getHand());
				state.setNextPlayer(getNextPlayer());
				//setCardEffect(data.getCard().applyCardEffect());
				
			}
			
			//play card
			
			if (deck.isEmpty())
			{
				deck.shuffle();
				state.setDeckTurnedOver(true);
			}
			else
			{
				state.setDeckTurnedOver(false);
			}
			
			state.setFaceUp(deck.getFaceUp());
			
			
		}
		
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
	private int getNextPlayer()
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
		if (color.equals("GREEN") || color.equals("BLUE") || color.equals("RED") || color.equals("YELLOW") || color.equals("NONE"))
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
	public void setCardEffect(String effect)
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
	

	
	/*
	 * private ArrayList<Player> getPlayers() { return players; }
	 * 
	 * private Deck getDeck() { return deck; }
	 */

	
	

	
	

}
