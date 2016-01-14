package game.actions;

import java.awt.geom.Point2D;
import java.util.Timer;
import java.util.TimerTask;

import utilities.TimerBarrier;
import game.state.internalState.*;
import game.utilities.Constants;
import game.utilities.MovementUtilities;
import game.utilities.PolygonUtilities;

/**
 * The main gameloop of a pong game
 * 
 * @author Matthew
 */
public class Gameloop implements Runnable {
	private InternalState state;

	// Some of the state fields, copied for ease of use
	private final InternalBall ball;
	private final InternalScore score;
	private final InternalPolygon poly;
	private final InternalControlState control;

	private final TimerBarrier barrier;

	// Rate of 30 milliseconds
	private final int rate = 30;

	// A double for ball speed, this is for acceleration
	private double accelSpeed;

	public Gameloop(InternalState s) {
		state = s;
		ball = s.getBall();
		score = s.getScores();
		poly = s.getPolygon();
		control = s.getControls();

		// Use a barrier with a timer for timing this loop
		barrier = new TimerBarrier(rate);
	}

	public void run() {
		// Setup timer here
		barrier.start();

		startBall(); // setup the ball
		// Gamelogic
		while (!state.finished()) {
			// Wait for appropriate time to go
			barrier.await();

			// Ball acceleration
			if (ball.getSpeed() != Constants.DEFAULT_MAX_SPEED) {
				accelSpeed += rate * Constants.DEFAULT_ACCELERATION / 1000;
				ball.setSpeed(Math.min((int) Math.round(accelSpeed),
						Constants.DEFAULT_MAX_SPEED));
			}

			// Paddle movement
			for (InternalSide s : poly.getSides()) {
				if (s.isPlayer()) {
					switch (control.getMove(s.getPlayer())) {
					case LEFT:
						s.moveLeft();
						break;
					case RIGHT:
						s.moveRight();
						break;
					default: // do nothing
					}

				}
			}

			// Ball movement
			Point2D.Double nextLoc = MovementUtilities.getNextLocation(ball);
			MovementUtilities.CollisionInfo cInfo = MovementUtilities
					.checkCollision(poly, ball, nextLoc);
			if (cInfo == null) { // No collision
				ball.setLocation(nextLoc);
			} else { // There is a collision
				double randomAdjustment = Math.random()
						* Constants.RANDOM_BOUNCE_ADJUSTMENT
						- (Constants.RANDOM_BOUNCE_ADJUSTMENT / 2);
				ball.setDirection(MovementUtilities.bounceDirection(
						cInfo.side(), ball.getDirection())
						+ randomAdjustment);
				ball.setLocation(MovementUtilities.bounceLocation(cInfo.wall(),
						cInfo.trajectory()));
			}

			// Ball outside field, we need to score
			if (!poly.bufferContains(ball.getLocation())) {
				String loser = PolygonUtilities.closestPlayer(poly,
						ball.getLocation());
				
				score.decreaseLife(loser, 1);

				if (score.getLives(loser) <= 0) {
					poly.removePlayer(loser);
				}

				if (score.checkWinner()) {
					state.finish();
				} else {
					// Reset the ball
					startBall();
				}
			}
		}

		// Given game has ended, cleanup
		barrier.stop();
	}

	/**
	 * Resets the ball to default location and starts it moving
	 */
	private void startBall() {
		accelSpeed = 0;
		ball.setSpeed(0);
		ball.setLocation(InternalBall.DEFAULT_LOCATION);
		ball.setDirection(Math.random() * (2 * Math.PI));
	}
}
