package server.core;

import shared.model.ModelUser;

/**
 * Represents a User on the server. Contains all information stored on a model
 * user, as well as the user's password and a collection of games he has joined.
 *
 */
public class ServerUser {

	private ModelUser modelUser;
	private String password;

	/**
	 * Creates a new user with the given information, and initializes the user's
	 * collection of joined games
	 *
	 * @param modelUser
	 *            The representation of the user in a game model
	 * @param password
	 *            The user's plain-text password. Yeah, that's a bad idea.
	 */
	public ServerUser(ModelUser modelUser, String password) {
		this.modelUser = modelUser;
		this.password = password;
	}

	public ModelUser getModelUser() {
		return this.modelUser;
	}

	public void setModelUser(ModelUser modelUser) {
		this.modelUser = modelUser;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
