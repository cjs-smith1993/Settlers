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
	private static UserManager instance;
	private int playerIDCount = 0;

	private UserManager() {
		users = new ArrayList<ServerUser>();
	}

	public static UserManager getInstance() {
		if (instance == null) {
			instance = new UserManager();
		}
		
		return instance;
	}

	/**
	 * 
	 * @param username
	 * @param password
	 * @return True If 
	 */
	private boolean canRegisterUser(String username, String password) {
		if (!validateUserCredentials(username, password) || checkUserExistence(username)) {
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
		if (this.canRegisterUser(username, password)) {
			users.add(new ServerUser(new ModelUser(username, playerIDCount), password));
			playerIDCount++;
		}
		else {
			String message = "A user with the desired username already exists";
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION, message);
		}
	}
	
	/**
	 * Authenticates the user at login.
	 * @param username
	 * @param password
	 * @return isAuthenticated
	 */
	public boolean authenticateUser(String username, String password) {
		for(ServerUser user : users) {
			if (user.getModelUser().getName().equals(username) 
					&& user.getModelUser().getName().equals(password)) {
				return true;
			}
		}
		
		return false;
	}

	/**
	 * Authenticates the user from API call.
	 * @param username
	 * @param password
	 * @return isAuthenticated
	 */
	public boolean authenticateUser(int id, String username, String password) {
		for(ServerUser user : users) {
			if (user.getModelUser().getName().equals(username) 
					&& user.getModelUser().getName().equals(password)
					&& user.getModelUser().getUserId() == id) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean validateUserCredentials(String username, String password) {
		if (username.equals("") || password.equals("")) {
			return false;
		}

		if (username.length() < 3 || password.length() < 5) {
			return false;
		}
		
		return true;
	}
	
	private boolean checkUserExistence(String username) {
		for (ServerUser user : users) {
			if (user.getModelUser().getName().equals(username)) {
				return true;
			}
		}
		
		return false;
	}
}
