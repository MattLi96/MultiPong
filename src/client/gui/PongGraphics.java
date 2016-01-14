package client.gui;

import game.Pong;
import game.state.Ball;
import game.state.Polygon;
import game.state.Score;
import game.state.Side;
import game.state.State;
import game.utilities.Constants;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Map;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;
import javafx.util.Duration;

/**
 * Used for drawing out the pong game onto the GUI
 * 
 * @author Matthew
 */
public class PongGraphics {
	// Constants
	public static final double DEFAULT_WORLD_FPS = 30;
	public static final double DEFAULT_SCORE_FPS = 2;

	private WorldCanvas worldCanvas;

	// The Score
	private Label winner;
	private GridPane livesGrid;

	private boolean started;

	//Action Controller really only for one method, getPongState()
	private ActionController ac;
	
	public PongGraphics(ActionController ac, StackPane world, Label winner,
			GridPane livesGrid) {
		// Setup the fields
		this.ac = ac;
		this.winner = winner;
		this.livesGrid = livesGrid;
		started = false;

		// Create a canvas for the world
		worldCanvas = new WorldCanvas();
		world.getChildren().add(worldCanvas);

		// Bind canvas size to stack pane size.
		worldCanvas.widthProperty().bind(world.widthProperty());
		worldCanvas.heightProperty().bind(world.heightProperty());
	}

	/**
	 * Start drawing using default values
	 */
	public void start() {
		if (started)
			return;

		start(DEFAULT_WORLD_FPS, DEFAULT_SCORE_FPS);
	}

	/**
	 * Start drawing using the given values
	 * 
	 * @param worldFPS
	 *            frames per second to draw the score's polygon
	 * @param scoreFPS
	 *            frames per second to draw the score's lives and winner
	 */
	public void start(double worldFPS, double scoreFPS) {
		if (started)
			return;

		setupWorld(worldFPS);
		setupScore(scoreFPS);
		started = true;
	}

	/**
	 * Rotates by one side
	 */
	public void rotate() {
		worldCanvas.rotate();
	}

	/**
	 * Rotate your username to the bottom
	 * 
	 * @param username
	 *            the username to rotate
	 */
	public void rotate(String username) {
		worldCanvas.rotate(username);
	}

	/**
	 * Draw the world
	 */
	private void drawWorld() {
		worldCanvas.draw();
	}

	/**
	 * Draws the score in the scorebox
	 */
	private void drawScore() {
		State s = ac.getPongState();
		Score score = s.getScore();

		winner.setText("Winner: " + score.getWinner());

		// Clear the lives
		livesGrid.getChildren().clear();
		Map<String, Integer> lives = score.getLives();
		int i = 0; // used to determine the row we should be writing on
		for (String user : lives.keySet()) {
			Label name = new Label(user);
			Label num_lives = new Label("" + lives.get(user));

			livesGrid.add(name, 0, i);
			livesGrid.add(num_lives, 1, i);
			i++;
		}
	}

	/**
	 * Helper for setting up the world update thread
	 */
	private void setupWorld(double fps) {
		final Duration oneFrameDurration = Duration.millis(1000 / fps);

		@SuppressWarnings({ "unchecked", "rawtypes" })
		final KeyFrame oneFrame = new KeyFrame(oneFrameDurration,
				new EventHandler() {
					@Override
					public void handle(Event arg0) {
						drawWorld();
					}
				}); // oneFrame

		final Timeline timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.getKeyFrames().add(oneFrame);
		timeline.play();
	}

	/**
	 * Helper for setting up the score update thread
	 */
	private void setupScore(double fps) {
		final Duration oneFrameDurration = Duration.millis(1000 / fps);

		@SuppressWarnings({ "unchecked", "rawtypes" })
		final KeyFrame oneFrame = new KeyFrame(oneFrameDurration,
				new EventHandler() {
					@Override
					public void handle(Event arg0) {
						drawScore();
					}
				}); // oneFrame

		final Timeline timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.getKeyFrames().add(oneFrame);
		timeline.play();
	}

	/**
	 * A resizable canvas for drawing our world
	 * 
	 * @author Matthew
	 */
	private class WorldCanvas extends Canvas {
		// The side to rotate to the top, starts with 0
		private int rotateSide;
		private final double text_shift = 50;

		public WorldCanvas() {
			// Redraw canvas when size changes.
			widthProperty().addListener(evt -> draw());
			heightProperty().addListener(evt -> draw());

			// Center all text
			GraphicsContext gc = getGraphicsContext2D();
			gc.setTextAlign(TextAlignment.CENTER);
			gc.setTextBaseline(VPos.CENTER);
			gc.setFont(new Font(50));
			rotateSide = 0;

			// Set line width
			gc.setLineWidth(5);
		}

		/**
		 * Rotates by one side
		 */
		private synchronized void rotate() {
			rotateSide += 1;
			rotateSide %= ac.getPongState().getPolygon().getSides().size();
		}

		/**
		 * Rotate the given username to the bottom
		 */
		private synchronized void rotate(String username) {
			int sideNum = -1;
			List<Side> sides = ac.getPongState().getPolygon().getSides();
			for (Side s : sides) {
				if (s.isPlayer() && s.getPlayer().equals(username)) {
					sideNum = s.getSideNum();
				}
			}

			// Rotate opposite side to the top
			rotateSide = sideNum
					+ sides.size() / 2;

			// Rotate mod for convenience
			rotateSide %= sides.size();
		}

		/**
		 * Draw the actual world
		 */
		private synchronized void draw() {
			// Setup
			double width = getWidth();
			double height = getHeight();
			double min_dim = Math.min(width, height);
			double scale = min_dim / Constants.POLYGON_RADIUS / 2;
			GraphicsContext gc = getGraphicsContext2D();
			gc.save(); // save the default graphics so I can restore it at the
						// end
			gc.clearRect(0, 0, width, height); // Clear the previous drawing

			// Objects we will need
			State state = ac.getPongState();
			Polygon poly = state.getPolygon();
			Ball ball = state.getBall();

			// Transformations
			double rotation = 360 - 360 / poly.getSides().size() * rotateSide;
			Affine transform = new Affine();
			transform.appendTranslation(width / 2, height / 2);
			transform.appendScale(scale, -scale);
			transform.appendRotation(rotation);
			gc.setTransform(transform);

			// draw the polygon
			for (Side s : poly.getSides()) { // draw all the sides
				Line2D.Double l = s.paddleLocation();
				gc.strokeLine(l.x1, l.y1, l.x2, l.y2);

				// For writing name
				if (s.isPlayer()) {
					l = s.getLine();

					double angle = Math.atan((l.y1 - l.y2) / (l.x1 - l.x2));
					double degAngle = Math.toDegrees(angle);

					double cx = (l.x1 + l.x2) / 2;
					double cy = -(l.y1 + l.y2) / 2; // Negative fix due to scaling

					// Shift out a little extra
					cx += Math.signum(cx)
							* Math.abs(text_shift
									* Math.cos(angle + Math.PI / 2));
					cy += Math.signum(cy)
							* Math.abs(text_shift
									* Math.sin(angle + Math.PI / 2));

					// Transform
					Affine temp = new Affine(transform);
					temp.appendScale(1, -1); // If not, the words are backwards
					temp.appendRotation(-degAngle, cx, cy);
					double totalAngle = degAngle + rotation;
					if (totalAngle > 90 && totalAngle <= 270) {
						temp.appendRotation(180, cx, cy); // Flip text for easier reading
					}
					if((totalAngle == 90 && cy > 0) || (totalAngle == 270 && cy < 0)){
						temp.appendRotation(180, cx, cy); // Handles edge case for text flipping
					}
					gc.setTransform(temp);

					// Actually write the text
					gc.fillText(s.getPlayer(), cx, cy);

					// Undo the transform
					gc.setTransform(transform);
				}
			}

			// draw the ball
			double radius = ball.getRadius();
			Point2D loc = ball.getLocation();
			gc.fillOval(loc.getX() - radius / 2, loc.getY() - radius / 2,
					radius, radius);

			// restore the graphics
			gc.restore();
		}

		@Override
		public boolean isResizable() {
			return true;
		}

		@Override
		public double prefWidth(double height) {
			return getWidth();
		}

		@Override
		public double prefHeight(double width) {
			return getHeight();
		}
	}
}
