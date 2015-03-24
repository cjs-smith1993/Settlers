package server.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


import shared.dataTransportObjects.DTOGame;
import shared.dataTransportObjects.DTOPlayer;
import shared.definitions.CatanExceptionType;
import shared.model.CatanException;


/**
 * Manages the collection of all games
 */
public class GameManager {

	private Map<Integer, ServerModelFacade> games;
	private ArrayList<DTOGame> gamesInfo;
	private static GameManager instance;
	private int nextGame;

	private GameManager() {
		this.games = new HashMap<Integer, ServerModelFacade>();
		this.gamesInfo = new ArrayList<DTOGame>();
		this.nextGame = 0;
	}

	public static GameManager getInstance() {
		if (instance == null) {
			instance = new GameManager();
		}
		return instance;
	}

	/**
	 * Returns a collection of all games
	 *
	 * @return a collection of all games
	 */
	public Collection<DTOGame> getGames() {
		return this.gamesInfo;
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
	public DTOGame createGame(
			boolean randomTiles,
			boolean randomNumbers,
			boolean randomPorts,
			String name) throws CatanException {
		if(name.equals(null)) {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION, "Game Not created - the name is null");
		}
		int gameid = nextGame++;
		ArrayList<DTOPlayer> player_list = new ArrayList<DTOPlayer>();
		DTOGame info = new DTOGame(gameid, name, player_list);
		ServerModelFacade smf = new ServerModelFacade(randomTiles, randomNumbers, randomPorts);
		games.put(info.id, smf);
		gamesInfo.add(info);
		return info;

	}

	public boolean authenticateGame(int gameId) {
		return this.games.get(gameId) != null;
	}

}
