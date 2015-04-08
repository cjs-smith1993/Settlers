package server.persistence;

import java.util.Collection;

import server.core.ServerUser;

/**
 * An interface for querying/modifying the users stored in the database.
 */
public interface IUserDAO {

	/**
	 * This will be used to get all the users in the database.
	 *
	 * @return a collection of ServerUsers, one for each user
	 */
	public Collection<ServerUser> getUsers();

	/**
	 * This will add the user with the given username and password to the
	 * database
	 *
	 * @param username
	 *            selected username
	 * @param password
	 *            selected password
	 * @return the id of the user in the database
	 */
	public int createUser(String username, String password);

}
