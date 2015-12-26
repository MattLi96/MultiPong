package game.actions;

import java.awt.geom.Point2D;
import java.util.Timer;
import java.util.TimerTask;

import game.state.internalState.*;
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

	private final RunBarrier barrier;

	public Gameloop(InternalState s) {
		state = s;
		ball = s.getBall();
		score = s.getScores();
		poly = s.getPolygon();
		control = s.getControls();

		// Use a barrier with a timer for timing this loop
		barrier = new RunBarrier();
	}

	public void run() {
		// Setup timer here
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				barrier.runNotify();
			}
		}, 0, 30); // Run every 30 milliseconds

		// Gamelogic
		while (!state.finished()) {
			// Wait for appropriate time to go
			barrier.await();

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
			Point2D nextLoc = MovementUtilities.getNextLocation(ball);
			MovementUtilities.CollisionInfo cInfo = MovementUtilities
					.checkCollision(poly, ball, nextLoc);
			if (cInfo == null) { // No collision
				ball.setLocation(nextLoc);
			} else { // There is a collision
				ball.setDirection(MovementUtilities.bounceDirection(
						cInfo.side(), ball.getDirection()));
				ball.setLocation(MovementUtilities.bounceLocation(cInfo.wall(),
						cInfo.trajectory()));
			}

			// Ball outside field, we need to score
			if (!poly.contains(ball.getLocation())) {
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
					ball.setLocation(InternalBall.DEFAULT_LOCATION);
					ball.setDirection(Math.random() * (2 * Math.PI));
					new Thread(new BallStart(ball, 1000)).start();
				}
			}
		}

		// Given game has ended, cleanup
		timer.cancel();
	}

	/**
	 * Slightly modified barrier for the run method
	 */
	private class RunBarrier {
		private boolean wait;

		public RunBarrier() {
			wait = true;
		}

		public synchronized void await() {
			while (wait) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			wait = true;
		}

		public synchronized void runNotify() {
			wait = false;
			notifyAll();
		}
	}
}