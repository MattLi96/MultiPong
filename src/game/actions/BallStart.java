package game.actions;

import game.state.Ball;
import game.utilities.Constants;

public class BallStart implements Runnable {
	private final Ball ball;
	private final int startSleep;
	
	/**
	 * @param startSleep Sleep before ball starts moving, measured in milisec
	 */
	public BallStart(Ball ball, int startSleep){
		this.ball = ball;
		this.startSleep = startSleep;
	}
	
	@Override
	public void run() {
		try {
			Thread.sleep(startSleep);
			for(int speed = 10; speed < Constants.DEFAULT_MAX_SPEED; speed++){
				ball.setSpeed(speed);
				Thread.sleep(Math.round((1000/Constants.DEFAULT_ACCELERATION)));
			}
			ball.setSpeed(Constants.DEFAULT_MAX_SPEED);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
