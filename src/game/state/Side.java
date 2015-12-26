package game.state;

import java.awt.geom.Line2D;

import game.state.internalState.InternalSide;


public class Side {
	private final InternalSide side;
	
	public Side(InternalSide side) {
		this.side = side;
	}
	
    /**
     * @return The line representing this side's paddle (or the whole side if there is no paddle)
     */
    public Line2D.Double paddleLocation() {
    	return side.paddleLocation();	
    }
	
	/**
	 * @return the side number of this side
	 */
    public int getSideNum() {
        return side.getSideNum();
    }
    
    /**
     * @return the player of this side or empty string ("") if this is not a player.
     */
    public String getPlayer(){
    	return side.getPlayer();
    }
    
    /**
     * @return if this side is a player or not
     */
    public boolean isPlayer(){
    	return side.isPlayer();
    }

}
