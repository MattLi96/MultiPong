package bundle.response;

public class LoginResponse extends Response {
	/**
	 * The id that should be included in all other requests from this client.
	 */
	public final int id;
	
	public LoginResponse(int id){
		super(true);
		this.id = id;
	}
}
