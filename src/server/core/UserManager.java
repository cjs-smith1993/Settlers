package server.core;

import java.util.ArrayList;
import java.util.Collection;

import shared.definitions.CatanExceptionType;
import shared.model.CatanException;
import shared.model.ModelUser;

/**
 * Manages the collection of all users
 */
public class UserManager {
	private Collection<ServerUser> users;
	private UserManager instance;
	private int playerIDCount = 0;

	private UserManager() {
		users = new ArrayList<ServerUser>();
	}

	public UserManager getInstance() {
		if (this.instance == null) {
			this.instance = new UserManager();
		}
		
		return this.instance;
	}

	/**
	 * 
	 * @param username
	 * @param password
	 * @return True If 
	 */
	public boolean validateUserCredentials(String username, String password) {
		if (!validateUsername(username, password) || checkUserExistence(username)) {
			return false;
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
		if (this.validateUserCredentials(username, password) && !this.checkUserExistence(username)) {
			users.add(new ServerUser(new ModelUser(username, playerIDCount), password));
			playerIDCount++;
		}
		else {
			String message = "A user with the desired username already exists";
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION, message);
		}
	}

	/**
	 * Authenticates the user with the given username and password
	 * @param username
	 * @param password
	 * @return isAuthenticated
	 */
	public boolean authenticateUser(String username, String password) {
		if (!validateUserCredentials(username, password)) {
			return false;
		}
		
		for(ServerUser user : users) {
			if (user.getModelUser().getName().equals(username) && user.getModelUser().getName().equals(password)) {
				return true;
			}
		}
		
		return true;
	}
	
	public boolean validateUsername(String username, String password) {
		if (username.equals("") || password.equals("")) {
			return false;
		}

		if (username.length() < 3 || password.length() < 5) {
			return false;
		}
		
		return true;
	}
	
	public boolean checkUserExistence(String username) {
		for (ServerUser user : users) {
			if (user.getModelUser().getName().equals(username)) {
				return true;
			}
		}
		
		return false;
	}
}
