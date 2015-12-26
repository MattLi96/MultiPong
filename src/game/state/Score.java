package game.state;

import java.util.Map;

import game.state.internalState.InternalScore;

public class Score {
	private final InternalScore score;
	
	public Score(InternalScore score){
		this.score = score;
	}
	
	public String getWinner(){
		return score.getWinner();
	}
	
	public Map<String, Integer> getLives() {
		return score.getLives();
	}
}
