package server.cookies;

/**
 * An encapsulation of a user cookie
 *
 */
public class UserCookie {

	private int userId;
	private String name;

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
