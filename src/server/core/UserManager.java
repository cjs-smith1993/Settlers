package server.core;

import java.io.IOException;
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

	private static final int MIN_USERNAME_LENGTH = 3;
	private static final int MIN_PASSWORD_LENGTH = 5;

	private UserManager() throws IOException {
		this.users = new ArrayList<ServerUser>();
		this.users.add(new ServerUser(new ModelUser("Kevin", this.playerIDCount++), "kevin"));
		this.users.add(new ServerUser(new ModelUser("Kyle", this.playerIDCount++), "kyle"));
		this.users.add(new ServerUser(new ModelUser("Connor", this.playerIDCount++), "connor"));
		this.users.add(new ServerUser(new ModelUser("Alex", this.playerIDCount++), "alex"));
	}

	public static UserManager getInstance() throws IOException {
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
		if (!this.validateUserCredentials(username, password) || this.checkUserExistence(username)) {
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
			this.users.add(new ServerUser(new ModelUser(username, this.playerIDCount), password));
			this.playerIDCount++;
			return this.playerIDCount - 1;
		}
		else {
			String message = "A user with the desired username already exists";
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION, message);
		}
	}

	/**
	 * Authenticates the user at LOGIN.
	 *
	 * @param username
	 * @param password
	 * @return isAuthenticated
	 */
	public boolean authenticateUser(String username, String password) {
		return this.getUserId(username, password) != -1;
	}

	/**
	 * Authenticates the user from API call.
	 *
	 * @param username
	 * @param password
	 * @return isAuthenticated
	 */
	public boolean authenticateUser(int id, String username, String password) {
		return this.getUserId(username, password) == id;
	}

	private boolean validateUserCredentials(String username, String password) {
		if (username.length() < MIN_USERNAME_LENGTH) {
			return false;
		}
		if (password.length() < MIN_PASSWORD_LENGTH) {
			return false;
		}
		return true;
	}

	private boolean checkUserExistence(String username) {
		for (ServerUser user : this.users) {
			if (user.getModelUser().getName().equals(username)) {
				return true;
			}
		}

		return false;
	}

	public int getUserId(String username, String password) {
		for (ServerUser user : this.users) {
			if (user.getModelUser().getName().equals(username)
					&& user.getPassword().equals(password)) {
				return user.getModelUser().getUserId();
			}

		}
		return -1;
	}

	public ModelUser getModelUser(int playerId) throws ServerException {
		for (ServerUser user : this.users) {
			if (user.getModelUser().getUserId() == playerId) {
				return user.getModelUser();
			}
		}
		throw new ServerException(ServerExceptionType.INVALID_OPERATION,
				"no user data for given id");
	}
}
