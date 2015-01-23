package clientBackend.model;

import java.util.Collection;

/**
 * Manages the collection of all games
 */
public class GameManager {
	private Collection<Game> games;
	
	/**
	 * Returns a collection of all games
	 * @return a collection of all games
	 */
	public Collection<Game> getGames() {
		return null;
	}
	
	/**
	 * Creates a new game with the desired features
	 * @param randomTiles whether the tiles should be randomly placed
	 * @param randomNumbers whether the chits should be randomly placed
	 * @param randomPorts whether the ports should be randomly placed
	 * @param name the name of the game
	 */
	public void createGame(
		boolean randomTiles,
		boolean randomNumbers,
		boolean randomPorts,
		String name) {
		
	}
}
