package bundle.response;

/**
 * A generic response bundle
 * 
 * @author Matthew
 */
public class Response {
	/**
	 * If the request was a success or not. Used for things such as add player
	 */
	public final boolean success;

	/**
	 * An error message that can be used to describe issues if the request was
	 * not a success.
	 */
	public final String errorMessage;

	public Response(boolean success) {
		this(success, "");
	}

	public Response(boolean success, String errorMessage) {
		this.success = success;
		this.errorMessage = errorMessage;
	}
}
