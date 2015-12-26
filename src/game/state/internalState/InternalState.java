package game.state.internalState;

import game.state.State;

/**
 * Container for all the various aspects of a pong gamestate. This should be threadsafe
 * 
 * Once the class is created you cannot change the reference to the various objects (you can change their values).
 * Therefore you must create a new state for each game.
 * 
 * Classes within this package are internal states, which is what the game actually uses to play.
 * 
 * @author Matthew
 */
public class InternalState {
	//All of these fields are threadsafe (and the objects they represent
	private final InternalBall ball;
	private final InternalScore score;
	private final InternalPolygon poly;
	private final InternalControlState control;
	
	private volatile boolean started;
	private volatile boolean finished;
	
	public InternalState(InternalPolygon field, InternalBall ball, InternalControlState control, InternalScore score){
		this.poly = field;
		this.ball = ball;
		this.control = control;
		this.score = score;
		
		started = false;
		finished = false;
	}
	
	public InternalPolygon getPolygon(){
		return poly;
	}
	
	public InternalBall getBall(){
		return ball;
	}
	
	public InternalControlState getControls(){
		return control;
	}
	
	public InternalScore getScores(){
		return score;
	}
	
	public boolean started(){
		return started;
	}
	
	/**
	 * Sets started to true
	 */
	public void start(){
		started = true;
	}
	
	public boolean finished(){
		return finished;
	}
	
	/**
	 * Sets finished to true
	 */
	public void finish(){
		finished = true;
	}
}
