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
	 * Returns whether a game can be created with the desired properties
	 * @param randomTiles
	 * @param randomNumbers
	 * @param randomPorts
	 * @param name the desired name
	 * @return true if the name is unique
	 */
	public boolean canCreateGame(
			boolean randomTiles,
			boolean randomNumbers,
			boolean randomPorts,
			String name) {
		return false;
	}
	
	/**
	 * Creates a new game with the desired features
	 * @param randomTiles whether the tiles should be randomly placed
	 * @param randomNumbers whether the chits should be randomly placed
	 * @param randomPorts whether the ports should be randomly placed
	 * @param name the name of the game
	 * @throws CatanException if the game name is already taken
	 */
	public void createGame(
		boolean randomTiles,
		boolean randomNumbers,
		boolean randomPorts,
		String name) throws CatanException {
		throw new CatanException();
	}
}
