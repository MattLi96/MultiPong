package game.pong;

import java.util.*;

import game.actions.Gameloop;
import game.state.*;
import game.utilities.Constants;

/**
 * Primary pong game class. Interactions with the game go through this class.
 * An instance of pong represents one pong game. Note that all players in the
 * game must have a unique name.
 * 
 * @author Matthew
 */
public class Pong {
	/*
	 * List of all players including those that have been eliminated or joined after the game in progress.
	 * Basically think of this as the players that will be included in the next game.
	 */
	private HashSet<String> players;
	private State state; //The state of the current game
	
	
	/**
	 * Initializes the Pong game. Does not start the game.
	 */
	public Pong(){
		players = new HashSet<String>();
		state = new State(new Polygon(players), new Ball(), new ControlState(players), new Score(players));
		state.start(); state.finish();
	}
	
	/**
	 * Starts the given pong game. Does nothing if the game has already been started.
	 * @param lives the number of lives each player starts with
	 */
	public synchronized void start(int lives){
		if(state.started() && !state.finished()){ return; } //Game is already playing, let it continue to play.
		
		//create a new gamestate
		Polygon f = new Polygon(players);
		Ball b = new Ball();
		ControlState c = new ControlState(players);
		Score s = new Score(players, lives);
		state = new State(f, b, c, s);
		
		//Actually start the game
		state.start();
		new Thread(new Gameloop(state)).start();
	}
	
	/**
	 * Starts the given pong game. Does nothing if the game has already been started. Starts with default lives
	 */
	public synchronized void start(){
		this.start(Constants.DEFAULT_LIVES);
	}
	
	/**
	 * Ends the pong game. Does not immediately start again. Does not remove the players
	 */
	public synchronized void end(){
		state.finish();
	}
	
	/**
	 * Tries to add the player. Will not add the player if there is a duplicate. Does not add the player
	 * to the current game if one is going on. However, the player will be in the next game
	 * 
	 * @param player the name of the player. Cannot be empty string, "".
	 * @return True if successfully adds the player. Else false.
	 */
	public synchronized boolean addPlayer(String player){
		if(players.contains(player) || "".equals(player)){ return false; }
		players.add(player);
		return true;
	}
	
	/**
	 * @param player Player to remove
	 */
	public synchronized void removePlayer(String player){
		players.remove(player);
		
		if(state.started() && !state.finished()){
			state.getField().removePlayer(player);
		}
	}
	
	/**
	 * Sets the player's paddle to move to the left. Paddle will continue moving left until moveNone is called with the player.
	 * @param player The player name
	 */
	public synchronized void moveLeft(String player){
		state.getControls().setLeft(player);
	}
	
	/**
	 * Same as moveLeft, except moves the paddle to the right
	 */
	public synchronized void moveRight(String player){
		state.getControls().setRight(player);
	}
	
	/**
	 * Sets the player's paddle to not move
	 */
	public synchronized void moveNone(String player){
		state.getControls().setNone(player);
	}
}
