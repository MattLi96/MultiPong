package game.state.internalState;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import game.utilities.*;

/**
 * The game field, contains a bunch of sides.
 * 
 * @author Matthew
 */
public class InternalPolygon extends java.awt.Polygon { //Extends awt polygon for convenience
	/**
	 * No warning!
	 */
	private static final long serialVersionUID = 1950486100618058392L;
	private ConcurrentHashMap<String, Integer> playerToSide; //map player to side number
	private final ArrayList<InternalSide> sides; //the array representing the sides of a polygon don't change
	
	/**
	 * Default radius of the polygon
	 */
	public static final int RADIUS = 1000;
	
	/**
     * Constructs a regular polygon in an absolute frame of reference.
     * Also constructs a hidden, smaller polygon used for collision detection.
     * 
     * @param players The names of the players to use in this game. Also indicates 
     * 		how many sides the polygon will have.
     */
    public InternalPolygon(Set<String> players) {
    	this(players, players.size()*2);
    }
    
    /**
     * Helper constructor since repeatedly calculating numSides is wasteful
     * @param players Same as other constructor
     * @param numSides players.size()*2
     */
    private InternalPolygon(Set<String> players, int numSides){
    	super(PolygonUtilities.calculateX(InternalPolygon.RADIUS, numSides), 
    			PolygonUtilities.calculateY(InternalPolygon.RADIUS, numSides), 
    			numSides);
    	playerToSide = new ConcurrentHashMap<String, Integer>();
    	
    	//Create the sides. Players are on the even sides
    	String[] aPlayers = players.toArray(new String[0]);
        sides = new ArrayList<InternalSide>();
        for (int i = 0; i < numSides - 1; i++) {
        	String player = "";
        	if(i%2 == 0){ //Put the players on the even sides
        		player = aPlayers[i/2];
        		playerToSide.put(player, i);
        	}
            sides.add(new InternalSide(xpoints[i], ypoints[i], xpoints[i + 1], ypoints[i + 1], i, player));
        }
        //The last side must be odd, so no player
        sides.add(new InternalSide(xpoints[numSides - 1], ypoints[numSides - 1], 
        		xpoints[0], ypoints[0], numSides - 1, ""));
    }
    
    /**
     * @return side at the given position.
     */
    public InternalSide getSide(int num) {
        return sides.get(num);
    }
    
    /**
     * @return a copy of the full array of sides. Note this does not copy the actual side, so you can still adjust those.
     */
    public List<InternalSide> getSides(){
    	return Collections.unmodifiableList(sides);
    }
    
    /**
     * @return the number of sides
     */
    public int getNumSides(){
    	return sides.size();
    }

    /**
     * @param player the player name
     * @return the side of the given player. If the player does not have a side, returns -1
     */
    public int getPlayerSide(String player){
    	Integer ret = playerToSide.get(player);
    	return ret==null? -1 : ret;
    }
    
    /**
     * @param player the player to remove
     */
    public void removePlayer(String player){
    	int side = getPlayerSide(player);
    	if(side == -1) return; //we are done
    	
    	sides.get(side).removePlayer();
    	playerToSide.remove(player);
    }
}
