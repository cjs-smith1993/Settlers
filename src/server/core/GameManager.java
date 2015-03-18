package server.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import client.frontend.data.GameInfo;
import client.frontend.data.PlayerInfo;
import shared.definitions.CatanColor;
import shared.definitions.CatanExceptionType;
import shared.model.CatanException;
import shared.model.Game;
import shared.model.ModelUser;

/**
 * Manages the collection of all games
 */
public class GameManager {
	
	private Map<Integer, Game> games;
	private Collection<GameInfo> gamesInfo;
	private GameManager instance;
	private int nextGame;

	private GameManager() {
		games = new HashMap<Integer, Game>();
		gamesInfo = new ArrayList();
		nextGame = 0;
	}

	public GameManager getInstance() {
		if (this.instance == null) {
			this.instance = new GameManager();
		}
		return this.instance;
	}

	/**
	 * Returns a collection of all games
	 *
	 * @return a collection of all games
	 */
	public Collection<GameInfo> getGames() {
		return gamesInfo;
	}

	/**
	 * Creates a new game with the desired features
	 *
	 * @param randomTiles
	 *            whether the tiles should be randomly placed
	 * @param randomNumbers
	 *            whether the chits should be randomly placed
	 * @param randomPorts
	 *            whether the ports should be randomly placed
	 * @param name
	 *            the name of the game
	 * @throws CatanException
	 *             if the game name is already taken
	 */
	public void createGame(
			boolean randomTiles,
			boolean randomNumbers,
			boolean randomPorts,
			String name) throws CatanException {
		GameInfo info = new GameInfo();
		info.setTitle(name);
		info.setId(nextGame++);
		//Game tempGame = new Game(randomTiles, randomNumbers, randomPorts);
		//games.put(info.getId(), tempGame);
	}

	
}
