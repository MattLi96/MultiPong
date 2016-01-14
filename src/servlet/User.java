package servlet;

/**
 * Class for storing information about a user. Might be useful later for
 * permissions. For now it just stores the id of the user.
 * 
 * @author Matthew
 */
public class User {
	private int id;

	public User(int id) {
		this.id = id;
	}

	public int getID() {
		return id;
	}
}
