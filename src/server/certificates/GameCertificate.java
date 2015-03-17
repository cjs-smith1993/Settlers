package server.certificates;

/**
 * An encapsulation of a game cookie
 *
 */
public class GameCertificate {

	private int gameId;

	public GameCertificate(int gameId) {
		this.gameId = gameId;
	}

	public int getGameId() {
		return this.gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

}
