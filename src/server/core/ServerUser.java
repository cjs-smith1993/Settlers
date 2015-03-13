package server.core;

import java.util.ArrayList;
import java.util.Collection;

import shared.model.Game;
import shared.model.ModelUser;

/**
 * Represents a User on the server. Contains all information stored on a model
 * user, as well as the user's password and a collection of games he has joined.
 *
 */
public class ServerUser {

	private ModelUser modelUser;
	private String password;
	private Collection<Game> joinedGames;

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
		this.joinedGames = new ArrayList<Game>();
	}

	/**
	 * Creates a new user with the given information
	 * 
	 * @param modelUser
	 *            The representation of the user in a game model
	 * @param password
	 *            The user's plain-text password. Yeah, that's a bad idea.
	 * @param joinedGames
	 */
	public ServerUser(ModelUser modelUser, String password, Collection<Game> joinedGames) {
		this.modelUser = modelUser;
		this.password = password;
		this.joinedGames = joinedGames;
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

	public Collection<Game> getJoinedGames() {
		return this.joinedGames;
	}

	public void setJoinedGames(Collection<Game> joinedGames) {
		this.joinedGames = joinedGames;
	}

}
