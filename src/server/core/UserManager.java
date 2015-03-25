package server.core;

import java.util.ArrayList;
import java.util.Collection;

import client.serverCommunication.ServerException;
import shared.definitions.CatanExceptionType;
import shared.definitions.ServerExceptionType;
import shared.model.CatanException;
import shared.model.ModelUser;

/**
 * Manages the collection of all users
 */
public class UserManager {
	private Collection<ServerUser> users;
	private static UserManager instance;
	private int playerIDCount = 0;

	protected UserManager() {
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
	public int registerUser(String username, String password) throws CatanException {
		if (this.canRegisterUser(username, password)) {
			users.add(new ServerUser(new ModelUser(username, playerIDCount), password));
			playerIDCount++;
			return playerIDCount-1;
		}
		else {
			String message = "A user with the desired username already exists";
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION, message);
		}
	}
	
	/**
	 * Authenticates the user at LOGIN.
	 * @param username
	 * @param password
	 * @return isAuthenticated
	 */
	public boolean authenticateUser(String username, String password) {
		return this.getUserId(username, password) != -1;
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
					&& user.getPassword().equals(password)
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

		if (username.length() < 4 || password.length() < 6) {
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
	
	public int getUserId(String username, String password) {
		for(ServerUser user : users) {
			if (user.getModelUser().getName().equals(username) 
					&& user.getPassword().equals(password)) {
				return user.getModelUser().getUserId();
			}
			
		}
		return -1;
	}
	public ModelUser getModelUser(int playerId) throws ServerException {
		for(ServerUser user : users) {
			if(user.getModelUser().getUserId() == playerId) {
				return user.getModelUser();
			}
		}
		throw new ServerException(ServerExceptionType.INVALID_OPERATION, "no user data for given id");
	}
}
