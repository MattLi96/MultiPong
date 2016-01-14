package game;

import java.util.*;

import game.actions.Gameloop;
import game.state.State;
import game.state.internalState.*;
import game.utilities.Constants;

/**
 * Primary pong game class. Interactions with the game go through this class. An
 * instance of pong represents one pong game. Note that all players in the game
 * must have a unique name. This class is threadsafe
 * 
 * @author Matthew
 */
public class PongImpl implements Pong {
	/*
	 * List of all players including those that have been eliminated or joined
	 * after the game in progress. Basically think of this as the players that
	 * will be included in the next game.
	 */
	private HashSet<String> players;

	// Container for the state and it's corresponding iState.
	private class PongState {
		final InternalState iState;
		final State state;

		PongState(InternalState internalState) {
			iState = internalState;
			state = new State(iState);
		}
	}

	private volatile PongState pState; // The state of the current game

	/**
	 * Initializes the Pong game. Does not start the game.
	 */
	public PongImpl() {
		HashSet<String> temp = new HashSet<String>();
		temp.add("temp player 1");
		temp.add("temp player 2");
		pState = new PongState(new InternalState(new InternalPolygon(temp),
				new InternalBall(), new InternalControlState(temp),
				new InternalScore(temp)));
		pState.iState.start();
		pState.iState.finish();

		players = new HashSet<String>();
	}

	public State getState() {
		return pState.state;
	}

	public synchronized boolean start(int lives) {
		if (pState.iState.started() && !pState.iState.finished()) {
			return false;
		} // Game is already playing, let it continue to play.
		if (players.size() < 2) {
			return false;
		} // not enough players

		// create a new gamestate
		InternalPolygon f = new InternalPolygon(players);
		InternalBall b = new InternalBall();
		InternalControlState c = new InternalControlState(players);
		InternalScore s = new InternalScore(players, lives);
		pState = new PongState(new InternalState(f, b, c, s));

		// Actually start the game
		pState.iState.start();
		new Thread(new Gameloop(pState.iState)).start();
		return true;
	}

	public synchronized boolean start() {
		return this.start(Constants.DEFAULT_LIVES);
	}

	public synchronized void end() {
		pState.iState.finish();
	}

	public synchronized boolean addPlayer(String player) {
		if (players.contains(player) || "".equals(player)) {
			return false;
		}
		players.add(player);
		return true;
	}

	public synchronized void removePlayer(String player) {
		players.remove(player);

		if (pState.iState.started() && !pState.iState.finished()) {
			pState.iState.getPolygon().removePlayer(player);
		}
	}

	public void moveLeft(String player) {
		pState.iState.getControls().setLeft(player);
	}

	public void moveRight(String player) {
		pState.iState.getControls().setRight(player);
	}

	public void moveNone(String player) {
		pState.iState.getControls().setNone(player);
	}
}
