package clientBackend.model;

import java.util.Collection;

/**
 * Manages the collection of all users
 */
public class UserManager {
	private Collection<User> users;
	
	/**
	 * Returns whether a new user with the desired properties can be created
	 * @param username the desired username
	 * @param password the desired password
	 * @return true if a new user with the desired properties can be created
	 */
	public boolean canRegisterUser(String username, String password) {
		return false;
	}
	
	/**
	 * Registers a user with the desired username and password
	 * @param username
	 * @param password
	 * @throws CatanException if a user cannot be created with the desired
	 * properties
	 */
	public void registerUser(String username, String password) throws CatanException {
		throw new CatanException();
	}
	
	/**
	 * Authenticates the user with the given username and password
	 * @param username
	 * @param password
	 */
	public void authenticateUser(String username, String password) {
		
	}
}
