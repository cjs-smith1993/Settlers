package server.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import server.certificates.GameCertificate;
import shared.dataTransportObjects.DTOGame;
import shared.dataTransportObjects.DTOPlayer;
import shared.definitions.CatanColor;
import shared.definitions.CatanExceptionType;
import shared.model.CatanException;
import shared.model.ModelUser;

/**
 * Manages the collection of all games
 */
public class GameManager {

	private Map<Integer, ServerModelFacade> games;
	private Map<Integer, DTOGame> gamesInfo;
	private static GameManager instance;
	private int nextGame;

	private GameManager() {
		this.games = new HashMap<Integer, ServerModelFacade>();
		this.gamesInfo = new HashMap<Integer, DTOGame>();//map int to DTOgame
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
		return this.gamesInfo.values();
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
		if (name.equals(null)) {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION,
					"Game Not created - the name is null");
		}
		int gameid = this.nextGame++;
		ArrayList<DTOPlayer> player_list = new ArrayList<DTOPlayer>();
		DTOGame info = new DTOGame(gameid, name, player_list);
		ServerModelFacade smf = new ServerModelFacade(gameid, randomTiles, randomNumbers, randomPorts);
		games.put(info.id, smf);
		gamesInfo.put(info.id, info);
		return info;

	}

	public boolean authenticateGame(int gameId) {
		return this.games.get(gameId) != null;
	}

	public ServerModelFacade getFacadeById(int gameId) {
		if (this.games.get(gameId) != null) {
			return this.games.get(gameId);
		}
		return null;
	}

	public GameCertificate joinGame(int gameId, CatanColor color, ModelUser user)
			throws CatanException {
		DTOGame game = this.gamesInfo.get(gameId);
		DTOPlayer player = new DTOPlayer();
		player.color = color;
		player.id = user.getUserId();
		player.name = user.getName();
		if (game.players.size() <= 3) {
			for (DTOPlayer play : game.players) {
				if (play.id == player.id) {
					throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION,
							"Already joined that game");
				}
			}
			game.players.add(player);
		}
		else {
			throw new CatanException(CatanExceptionType.ILLEGAL_MOVE, "Game is full");
		}
		this.games.get(gameId).joinGame(user, color);//do the join game on the facade
		this.gamesInfo.remove(gameId);
		this.gamesInfo.put(game.id, game);
		return new GameCertificate(game.id);

	}
}
