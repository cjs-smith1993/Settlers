package server.core;

import java.util.Collection;

import shared.definitions.CatanExceptionType;
import shared.model.CatanException;

/**
 * Manages the collection of all users
 */
public class UserManager {
	@SuppressWarnings("unused")
	private Collection<ServerUser> users;
	private UserManager instance;

	private UserManager() {

	}

	public UserManager getInstance() {
		if (this.instance == null) {
			this.instance = new UserManager();
		}
		return this.instance;
	}

	/**
	 * Returns whether a new user with the desired properties can be created
	 *
	 * @param username
	 *            the desired username
	 * @param password
	 *            the desired password
	 * @return true if a new user with the desired properties can be created
	 */
	public boolean canRegisterUser(String username, String password) {
		return false;
	}

	/**
	 * Registers a user with the desired username and password
	 *
	 * @param username
	 * @param password
	 * @throws CatanException
	 *             if a user cannot be created with the desired properties
	 */
	public void registerUser(String username, String password) throws CatanException {
		if (this.canRegisterUser(username, password)) {

		}
		else {
			String message = "A user with the desired username already exists";
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION, message);
		}
	}

	/**
	 * Authenticates the user with the given username and password
	 *
	 * @param username
	 * @param password
	 */
	public void authenticateUser(String username, String password) {

	}
}
