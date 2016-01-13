package client.gui;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Map;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import game.Pong;
import game.state.*;
import game.utilities.Constants;

/**
 * Used for drawing out the pong game onto the GUI
 * 
 * @author Matthew
 */
public class PongGraphics {
	// Constants
	public static final double DEFAULT_WORLD_FPS = 30;
	public static final double DEFAULT_SCORE_FPS = 2;

	// The world (for drawing the game playing
	private StackPane world;

	private WorldCanvas worldCanvas;

	// The Score
	private Label winner;
	private GridPane livesGrid;

	// The pong game
	private Pong pong;

	private boolean started;

	public PongGraphics(Pong pong, StackPane world, Label winner,
			GridPane livesGrid) {
		// Setup the fields
		this.pong = pong;
		this.world = world;
		this.winner = winner;
		this.livesGrid = livesGrid;
		started = false;

		//Create a canvas for the world
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
	 * Draw the world
	 */
	private void drawWorld() {
		worldCanvas.draw();
	}

	/**
	 * Draws the score in the scorebox
	 */
	private void drawScore() {
		State s = pong.getState();
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
	 * @author Matthew
	 */
    private class WorldCanvas extends Canvas {
        public WorldCanvas() {
            // Redraw canvas when size changes.
            widthProperty().addListener(evt -> draw());
            heightProperty().addListener(evt -> draw());
            
            //Center all text
            GraphicsContext gc = getGraphicsContext2D();
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setFont(new Font(50));
        }
        
        /**
         * Borrowed this rotate method from stack overflow
         * 
         * Sets the transform for the GraphicsContext to rotate around a pivot point.
         *
         * @param gc the graphics context the transform to applied to.
         * @param angle the angle of rotation.
         * @param px the x pivot co-ordinate for the rotation (in canvas co-ordinates).
         * @param py the y pivot co-ordinate for the rotation (in canvas co-ordinates).
         */
        private void rotate(GraphicsContext gc, double angle, double px, double py) {
            Rotate r = new Rotate(angle, px, py);
            gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
        }
 
        private synchronized void draw() {
        	//Setup
            double width = getWidth();
            double height = getHeight();
            double min_dim = Math.min(width, height);
            double scale = min_dim / Constants.POLYGON_RADIUS / 1.8;
            GraphicsContext gc = getGraphicsContext2D();
            gc.save(); //save the default graphics so I can restore it at the end
            gc.clearRect(0, 0, width, height); //Clear the previous drawing
            
            //Objects we will need
            State state = pong.getState();
            Polygon poly = state.getPolygon();
            Ball ball = state.getBall();
            		
            //Transformations
            Affine transform = new Affine();
            transform.appendTranslation(width/2, height/2);
            transform.appendScale(scale, -scale);
            transform.appendRotation(180); //TODO Figure out the rotation later
            gc.setTransform(transform);
         
            
            gc.setLineWidth(3);
            
            //draw the polygon
            for(Side s: poly.getSides()){ //draw all the sides
            	Line2D.Double l = s.paddleLocation();
            	gc.strokeLine(l.x1, l.y1, l.x2, l.y2);

            	if(s.isPlayer()){
            		l = s.getLine();
            		
            		//TODO fix this text stuff
            		double cx = (l.x1 + l.x2) / 2;
            		double cy = (l.y1 + l.y2) / 2;
            		gc.strokeText(s.getPlayer(), cx, cy);
            		
            	}
            }
            
            //draw the ball
            double radius = ball.getRadius();
            Point2D loc = ball.getLocation();
            gc.fillOval(loc.getX() - radius / 2, loc.getY() - radius / 2, radius, radius);
            
            //restore the graphics
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
