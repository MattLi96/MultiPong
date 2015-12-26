package game.pong;

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
public class Pong {
	/*
	 * List of all players including those that have been eliminated or joined after the game in progress.
	 * Basically think of this as the players that will be included in the next game.
	 */
	private HashSet<String> players;
	
	// Container for the state and it's corresponding iState.
	private class PongState{
		final InternalState iState;
		final State state;
		
		PongState(InternalState internalState){
			iState = internalState;
			state = new State(iState);
		}
	}
	
	private PongState pState; //The state of the current game
	
	/**
	 * Initializes the Pong game. Does not start the game.
	 */
	public Pong(){
		players = new HashSet<String>();
		pState = new PongState(
				new InternalState(
						new InternalPolygon(players), 
						new InternalBall(), 
						new InternalControlState(players), 
						new InternalScore(players)));
		pState.iState.start(); pState.iState.finish();
	}
	
	public State getState(){
		return pState.state;
	}
	
	/**
	 * Starts the given pong game. Does nothing if the game has already been started.
	 * @param lives the number of lives each player starts with
	 */
	public synchronized void start(int lives){
		if(pState.iState.started() && !pState.iState.finished()){ return; } //Game is already playing, let it continue to play.
		
		//create a new gamestate
		InternalPolygon f = new InternalPolygon(players);
		InternalBall b = new InternalBall();
		InternalControlState c = new InternalControlState(players);
		InternalScore s = new InternalScore(players, lives);
		pState = new PongState(new InternalState(f, b, c, s));
		
		//Actually start the game
		pState.iState.start();
		new Thread(new Gameloop(pState.iState)).start();
	}
	
	/**
	 * Starts the given pong game. Does nothing if the game has already been started. Starts with default lives
	 */
	public synchronized void start(){
		this.start(Constants.DEFAULT_LIVES);
	}
	
	/**
	 * Ends the pong game. Does not immediately start again. Does not remove the players. No winner is decided if you terminate this way
	 */
	public synchronized void end(){
		pState.iState.finish();
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
		
		if(pState.iState.started() && !pState.iState.finished()){
			pState.iState.getPolygon().removePlayer(player);
		}
	}
	
	/**
	 * Sets the player's paddle to move to the left. Paddle will continue moving left until moveNone is called with the player.
	 * @param player The player name
	 */
	public void moveLeft(String player){
		pState.iState.getControls().setLeft(player);
	}
	
	/**
	 * Same as moveLeft, except moves the paddle to the right
	 */
	public void moveRight(String player){
		pState.iState.getControls().setRight(player);
	}
	
	/**
	 * Sets the player's paddle to not move
	 */
	public void moveNone(String player){
		pState.iState.getControls().setNone(player);
	}
}
