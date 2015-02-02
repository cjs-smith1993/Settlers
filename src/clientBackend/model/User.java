package clientBackend.model;

/**
 * Represents a user. The number of games a user can be in is unlimited. The
 * userId of each user is unique
 */
public class User {
	private String name;
	private int userId;
	
	public User(String name, int userId) {
		this.name = name;
		this.userId = userId;
	}
}
