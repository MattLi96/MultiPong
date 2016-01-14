package bundle.request;

/**
 * This type of request has to do with adding or removing players
 * @author Matthew
 */
public class MoveRequest implements Request {
	public static enum Type {LEFT, RIGHT, NONE}
	
	public final String player;
	public final Type type;
	
	public MoveRequest(String player, Type type){
		this.player = player;
		this.type = type;
	}
}
