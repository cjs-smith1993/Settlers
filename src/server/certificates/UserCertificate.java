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
		this.password = password;
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


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result + playerID;
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserCertificate other = (UserCertificate) obj;
		return (this.name.equals(other.name)
				&& this.password.equals(other.password)
				&& this.playerID == other.playerID);
	}
}
