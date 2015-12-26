package game.state;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import game.state.internalState.*;

public class Polygon {
	private final ArrayList<Side> sides;
	
	public Polygon(InternalPolygon polygon) {
		sides = new ArrayList<Side>();
		for(InternalSide s : polygon.getSides()){
			sides.add(new Side(s));
		}
	}

    /**
     * @return side at the given position.
     */
    public Side getSide(int num) {
        return sides.get(num);
    }
    
    /**
     * @return a copy of the full array of sides. Note this does not copy the actual side, so you can still adjust those.
     */
    public List<Side> getSides(){
    	return Collections.unmodifiableList(sides);
    }
	
}
