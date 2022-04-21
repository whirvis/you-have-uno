package csci4490.uno.game;

import java.util.ArrayList;

public interface UnoInterface 
{
	/*Start the game, get the initial state relevant to the UI*/
	public State init();
	
	/*Play a turn on behalf of player n*/
	public void playTurn(TurnData data) throws IllegalMoveException;
	
	/*Get the state relevant for updating the UI, contains a flag for end of game*/
	public State getState();
	
	/*Get player scores*/
	public ArrayList<Integer>getScores();

}




