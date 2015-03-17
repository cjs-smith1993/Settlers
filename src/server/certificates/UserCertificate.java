package server.certificates;

/**
 * An encapsulation of a user cookie
 *
 */
public class UserCertificate {

	private int userID;
	private String name;
	private String password;

	public UserCertificate(int userId, String name, String password) {
		this.userID = userId;
		this.name = name;
	}

	public int getUserId() {
		return this.userID;
	}

	public void setUserId(int userId) {
		this.userID = userId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
