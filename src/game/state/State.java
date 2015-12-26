package game.state;

/**
 * Container for all the various aspects of a pong gamestate. This should be threadsafe
 * 
 * Once the class is created you cannot change the reference to the various objects (you can change their values).
 * Therefore you must create a new state for each game.
 * 
 * @author Matthew
 */
public class State {
	//All of these fields are threadsafe (and the objects they represent
	private final Ball ball;
	private final Score score;
	private final Polygon field;
	private final ControlState control;

	private volatile boolean started;
	private volatile boolean finished;
	
	public State(Polygon field, Ball ball, ControlState control, Score score){
		this.field = field;
		this.ball = ball;
		this.control = control;
		this.score = score;
		
		started = false;
		finished = false;
	}
	
	public Polygon getField(){
		return field;
	}
	
	public Ball getBall(){
		return ball;
	}
	
	public ControlState getControls(){
		return control;
	}
	
	public Score getScores(){
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
