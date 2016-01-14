package bundle.response;

/**
 * Response to a GameRequest, generally just success
 * 
 * @author Matthew
 */
public class GameResponse extends Response {
	public GameResponse(boolean success) {
		super(success);
	}
}
