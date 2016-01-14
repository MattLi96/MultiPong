package game.state;

import game.state.internalState.InternalState;

/**
 * This is the state of the game at a given moment. States are not immutable,
 * however they are unmodifiable. Essentially they only expose the "get"
 * functions of their respective internal component.
 * 
 * These are focused on only providing the necessary information for the GUI
 * 
 * @author Matthew
 */
public class State {
	private final Ball ball;
	private final Score score;
	private final Polygon poly;

	public State(InternalState state) {
		ball = new Ball(state.getBall());
		poly = new Polygon(state.getPolygon());
		score = new Score(state.getScores());
	}

	public Ball getBall() {
		return ball;
	}

	public Score getScore() {
		return score;
	}

	public Polygon getPolygon() {
		return poly;
	}
}
