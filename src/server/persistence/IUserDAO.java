package server.persistence;

import java.util.Collection;

import server.core.ServerUser;

/*
 * CREATE TABLE user
 * (
 * UserId int,
 * userName varchar(10),
 * password varchar(10)
 * );
 */

/**
 * This is the interface to access the user table or collection.
 * the cortex will call these methods to get users or add them.
 *
 */
public interface IUserDAO {
	
	/**
	 * This will be used to get all the users for our server.
	 * @return collection of ServerUsers.
	 */
	public Collection<ServerUser> getUsers();
	
	/**
	 * This will add the user with the given username and password to the database
	 * @param username - selected username
	 * @param password - selected password
	 * @return the id of the user in the database
	 */
	public int createUser(String username, String password);

}
