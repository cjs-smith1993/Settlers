package server.certificates;

/**
 * An encapsulation of a user cookie
 *
 */
public class UserCertificate {

	private int playerID;
	private String name;
	private String password;

	public UserCertificate(int playerID, String name, String password) {
		this.playerID = playerID;
		this.name = name;
	}

	public int getUserId() {
		return this.playerID;
	}

	public void setUserId(int playerID) {
		this.playerID = playerID;
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
