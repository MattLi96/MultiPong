package game.state;

import java.awt.geom.Point2D;

import game.state.internalState.InternalBall;

public class Ball {
	private final InternalBall ball;

	public Ball(InternalBall ball) {
		this.ball = ball;
	}

	public Point2D.Double getLocation() {
		return ball.getLocation();
	}

	public int getRadius() {
		return ball.getRadius();
	}
}
