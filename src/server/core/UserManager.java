package server.core;

import java.util.ArrayList;
import java.util.Collection;

import shared.definitions.CatanExceptionType;
import shared.model.CatanException;

/**
 * Manages the collection of all users
 */
public class UserManager {
	private Collection<ServerUser> users;
	private static UserManager instance;

	private UserManager() {
		this.users = new ArrayList<ServerUser>();
	}

	public static UserManager getInstance() {
		if (instance == null) {
			instance = new UserManager();
		}

		return instance;
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
		if (username.equals("") || password.equals("")) {
			return false;
		}

		if (username.length() < 3 || password.length() < 5) {
			return false;
		}

		for (ServerUser user : this.users) {
			if (user.getModelUser().getName().equals(username)) {
				return false;
			}
		}

		return true;
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
