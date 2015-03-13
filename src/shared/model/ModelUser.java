package shared.model;

/**
 * Represents a user. The number of games a user can be in is unlimited. The
 * userId of each user is unique
 */
public class ModelUser {
	private String name;
	private int playerID;

	public ModelUser(String name, int playerId) {
		this.name = name;
		this.playerID = playerId;
	}

	public String getName() {
		return this.name;
	}

	public int getUserId() {
		return this.playerID;
	}
}
