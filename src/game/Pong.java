package game;

import java.util.*;

import game.actions.Gameloop;
import game.state.State;
import game.state.internalState.*;
import game.utilities.Constants;

/**
 * Primary pong game class. Interactions with the game go through this class.
 * An instance of pong represents one pong game. Note that all players in the
 * game must have a unique name.
 * 
 * @author Matthew
 */
public interface Pong {

	/**
	 * @return A state that represents the current pong state
	 */
	public State getState();
	
	/**
	 * Starts the given pong game. Does nothing if the game has already been started.
	 * @param lives the number of lives each player starts with
	 */
	public void start(int lives);
	
	/**
	 * Starts the given pong game. Does nothing if the game has already been started. Starts with default lives
	 */
	public void start();
	
	/**
	 * Ends the pong game. Does not immediately start again. Does not remove the players. No winner is decided if you terminate this way
	 */
	public void end(); 
	
	/**
	 * Tries to add the player. Will not add the player if there is a duplicate. Does not add the player
	 * to the current game if one is going on. However, the player will be in the next game
	 * 
	 * @param player the name of the player. Cannot be empty string, "".
	 * @return True if successfully adds the player. Else false.
	 */
	public boolean addPlayer(String player);
	
	/**
	 * @param player Player to remove
	 */
	public void removePlayer(String player);
	
	/**
	 * Sets the player's paddle to move to the left. Paddle will continue moving left until moveNone is called with the player.
	 * @param player The player name
	 */
	public void moveLeft(String player);
	
	/**
	 * Same as moveLeft, except moves the paddle to the right
	 */
	public void moveRight(String player);
	
	/**
	 * Sets the player's paddle to not move
	 */
	public void moveNone(String player);
}
