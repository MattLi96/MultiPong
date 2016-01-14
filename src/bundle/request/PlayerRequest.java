package bundle.request;

/**
 * This type of request has to do with adding or removing players
 * @author Matthew
 */
public class PlayerRequest implements Request {
	public static enum PlayerRequestType {ADD, REMOVE}
	
	public final String player;
	public final PlayerRequestType type;
	
	public PlayerRequest(String player, PlayerRequestType type){
		this.player = player;
		this.type = type;
	}
}
