package servlet;

import java.util.HashMap;
import java.util.Map;

/**
 * Essentially a class for converting IDs to users.
 * 
 * May use this later to help keep track of what users have which permissions.
 * 
 * @author Matthew
 */
public class Authenticator {
	private Map<Integer, User> idToUser;
	private int nextID; // The ID for the next user

	public Authenticator() {
		idToUser = new HashMap<Integer, User>();
		nextID = 1;
	}

	/**
	 * Create a new session id and user profile
	 * 
	 * @return the newID for the session
	 */
	public int login() {
		int id = nextID++;
		idToUser.put(id, new User(id));
		return id;
	}

	/**
	 * 
	 * @param id
	 * @return the user or null if the id does not correspond to a user
	 */
	public User getUser(int id) {
		User ret = idToUser.get(id);
		return ret;
	}
}
