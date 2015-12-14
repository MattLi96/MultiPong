package serializable;

import java.io.Serializable;
import java.util.Arrays;

import utilities.Constants;

public class Score implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9117177017232278926L;
	private int[] lives;
	private boolean playing;
	private int winner;
	private Polygon polygon;
	
	public Score(Polygon polygon){
		lives = new int[polygon.getNumSides()/2];
		Arrays.fill(lives, Constants.startingLives);
		this.polygon = polygon;
	}
	
	public int getNumPlayers(){
		return lives.length;
	}
	
	
	public void loseLife(int player){
		lives[player] -= 1;
	}
	
	public int getLives(int player){
		return lives[player];
	}
	
	public boolean isPlaying(){
		return playing;
	}
	
	public void setPlaying(boolean b){
		playing = b;
	}
	
	public int getWinner(){
		return winner;
	}
	
	public void setWinner(int player){
		winner = player;
	}
	
	public int[] getLives()
	{
		return lives;
	}
	public void printScore(){
		System.out.println("Scores");
		for(int i = 0; i < getNumPlayers(); i++){
			System.out.println("player " + (i +1) + " : " + lives[i]);
		}
		System.out.println();
	}
	
	public void changeScore(int player, int newScore){
		lives[player] = newScore;
	}
	
	public boolean isAlive(int player){
		return (lives[player] > 0);
	}
	
	public String stringScore(){
		String score = "";
		for(int i = 0; i < getNumPlayers(); i++){
			if(i < getNumPlayers()-1){
				score += polygon.getSide(i*2).getName() + " : " + lives[i] + " | ";
			} else {
				score += polygon.getSide(i*2).getName() + " : " + lives[i];
			}
		}
		return score;
	}
}