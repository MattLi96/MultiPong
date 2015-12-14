package pong;

import graphics.Graphics;
import java.awt.event.*;
import java.util.concurrent.TimeUnit;
import javax.swing.*;
import javax.swing.Timer;

import communicator.*;
import serializable.*;
import utilities.*;

public class Pong {

	private static Ball ball; // game ball
	private static Polygon polygon; // n-sided polygon
	private Communicator comm = null;
	private Score score = null;
	private int side = -1;
	private Graphics graphics = null;
	
	private boolean ready = false; // only useful if a server
	private Timer ballAction = null;
	
	/**
	 * Defult pong game square window size.
	 */
	public static final int DEFAULT_SIZE = 1000;

	/**
	 * Create an n-sided Pong game.
	 * 
	 * @param n
	 *            number of polygon sides
	 */
	public Pong() {
		this(8, null);
	}

	public Pong(Communicator comm) {
		this(8, comm);
	}

	public Pong(int n, Communicator comm) {
		this.comm = comm;
		// ball.changeDirection(Math.PI * 1 / 9); // CONSISTENT DIRECTION
		polygon = new Polygon(n);
		ball = new Ball(comm);
		score = new Score(polygon);
		score.setPlaying(true);

		if (comm instanceof Client) {
			initClient();
		} else {
			System.out.println("Probably a server");
		}
	}

	public void nowReady() {
		ready = true;
	}

	public void initServer() {
		while (!ready) { // wait until server says ready
			try {
				TimeUnit.NANOSECONDS.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		if(comm instanceof Server){
			((Server) comm).setPong(this);
			comm.sendObject("testing");
			((Server) comm).cleanUp();
		}
		int sides = (((Server) comm).getNumClients() + 1) * 2;
		
		polygon = new Polygon(sides); // creating the polygon
		
		for (int i = 0; i < polygon.getSides().length; i += 2) {
			polygon.setPlayer(i, "PLAYER" + (i / 2 + 1));
		}
		
		polygon.setPlayer(0, Constants.name);
		
		score = new Score(polygon); // setting score
		score.setPlaying(true);

		comm.sendObject(polygon);
		comm.sendObject(score);
		side = 0;
		graphics = new Graphics(this, side);

		ball = new Ball(comm); // begin ball movement
		new Thread(new BallPause(ball, 1000)).start();

		ballAction = new Timer(36, new BallAction());
		ballAction.start();

		for (int i = 0; i < sides / 2 - 1; i++) { // begin reading from players
			((Server) comm).sendObject(new Integer((i + 1) * 2), i);
			new Thread(new RunServer((Server) comm, i, this)).start();
		}
	}

	private void initClient() {
		comm.sendObject(Constants.name); //send so it can print on serverUI
		while(side == -1){
			Object o = ((Client) comm).getNextObject();
			if (o instanceof Integer) {
				side = (Integer) o;
			}
		}
		
		graphics = new Graphics(this, side);
		new Thread(new RunClient((Client) comm, this)).start();
	}

	public static int getClosestPlayer() {
		double minDist = polygon.getSide(0).ptLineDist(ball.getLocation());
		int side = 0;

		for (int i = 0; i < polygon.getSides().length; i++) {
			if (polygon.getSide(i) instanceof Player) {
				if (polygon.getSide(i).ptLineDist(ball.getLocation()) < minDist) {
					minDist = polygon.getSide(i).ptLineDist(ball.getLocation());
					side = i;
				}
			}
		}
		return side;
	}

	public static int getClosestSide() {
		double minDist = polygon.getSide(0).ptLineDist(ball.getLocation());
		int side = 0;

		for (int i = 0; i < polygon.getSides().length; i++)
			if (polygon.getSide(i).ptLineDist(ball.getLocation()) < minDist) {
				minDist = polygon.getSide(i).ptLineDist(ball.getLocation());
				side = i;
			}

		return side;
	}
	
	public Graphics getGraphics(){
		return graphics;
	}

	public static Polygon getPolygon() {
		return polygon;
	}

	public void setPolygon(Polygon p) {
		polygon = p;
	}

	public Ball getBall() {
		return ball;
	}

	public void setBall(Ball b) {
		ball = b;
	}

	public Communicator getCommunicator() {
		return comm;
	}

	public int getSide() {
		return side;
	}

	public void setSide(int side) {
		this.side = side;
	}

	public Score getScore() {
		return score;
	}

	public void setScore(Score score) {
		this.score = score;
	}
	
//	public int getWinner(){
//		return winner;
//	}
//	
//	public boolean isPlaying(){
//		return playing;
//	}
	
	public void killPlayer(int playerNumber){
		if(polygon.getSide(playerNumber) instanceof Player){
			((Player) polygon.getSide(playerNumber)).killPlayer();
		}
		score.changeScore(playerNumber / 2, 0);
	}
	
	public void endGame(){
		score.setPlaying(false);
		if(ballAction != null){
			ballAction.stop();
		}
		comm.sendObject("break");
	}
	
	public void resetConnections(){
		((Server) comm).reset();
		comm.sendObject(score);
		comm.sendObject(polygon);
	}

	class BallAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			if (ball.getState() == true) {
				move();
			}
		}
		

		
		/**
		 * Updates ball direction and moves ball. Updates score if there is a goal.
		 *
		 */
		private void move() {
			// Update ball position.
			// RED_FLAG: this is too much Ball code in Pong

			polygon.checkCollision(ball);
			ball.move();
			
			// Check for scoring.
			if (!polygon.contains(ball.getLocation())) {
				ball.stop();

				score.loseLife(getClosestPlayer() / 2);
				score.printScore();
				
				if (score.getLives(getClosestPlayer() / 2) <= 0)
					killPlayer(getClosestPlayer());		
					
				int[] lives = score.getLives();
				
				for(int i = 0; i<lives.length; i++){
					if(lives[i] > 0){
						score.setPlaying(!score.isPlaying()); 
						if(score.isPlaying()){
							break;
						}
						score.setWinner(i);		
					}
				}
				
				if(!score.isPlaying()){
					//graphics.paintLeaderName(winner);
					System.out.println("The Winner is Player " + (score.getWinner() + 1) + "!!!");
					endGame();
				}
				

				if (comm instanceof Server) {
					resetConnections();
				}

				ball = new Ball(comm);
				new Thread(new BallPause(ball, 1000)).start();
			}
		}
	}
}