package server.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import client.backend.CatanSerializer;
import server.certificates.GameCertificate;
import shared.dataTransportObjects.DTOGame;
import shared.dataTransportObjects.DTOPlayer;
import shared.definitions.CatanColor;
import shared.definitions.CatanExceptionType;
import shared.model.CatanException;
import shared.model.ModelUser;
import shared.model.Player;
import shared.transport.TransportModel;

/**
 * Manages the collection of all games
 */
public class GameManager {

	private Map<Integer, ServerModelFacade> games;
	private static GameManager instance;
	private int nextGameId;
	private final String gamesFile = "save/games/";

	private static final int MAX_PLAYERS = 4;

	private GameManager() {
		this.createGamesList();
		this.nextGameId = 0;
	}

	private void createGamesList() {
		this.games = new HashMap<Integer, ServerModelFacade>();

		File gamesFolder = new File(this.gamesFile);
		File[] listOfFiles = gamesFolder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			File file = listOfFiles[i];
			if (file.isFile()) {
				String fileName = file.getName();
				if (fileName.charAt(0) != '.') {
					this.createGameFromFile(fileName);
				}
			}
		}

		return;
	}

	public static GameManager getInstance() {
		if (instance == null) {
			instance = new GameManager();
		}
		return instance;
	}

	private int getNextGameId() {
		while (this.games.get(this.nextGameId) != null) {
			this.nextGameId++;
		}
		return this.nextGameId;
	}

	/**
	 * Returns a collection of all games
	 *
	 * @return a collection of all games
	 */
	public Collection<DTOGame> getGames() {
		Collection<DTOGame> gamesList = new ArrayList<DTOGame>();
		for (ServerModelFacade game : this.games.values()) {
			gamesList.add(game.getGameInfo());
		}
		return gamesList;
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

		if (name.length() == 0) {
			throw new CatanException(CatanExceptionType.ILLEGAL_OPERATION,
					"Game Not created - the name is null");
		}

		int gameId = this.getNextGameId();
		ArrayList<DTOPlayer> player_list = new ArrayList<DTOPlayer>();
		ServerModelFacade smf = new ServerModelFacade(gameId, name, randomTiles, randomNumbers,
				randomPorts);
		this.games.put(gameId, smf);

		DTOGame info = new DTOGame(gameId, name, player_list);
		return info;
	}

	public boolean createGameFromFile(String filename) {
		try {
			ServerModelFacade facade = new ServerModelFacade(this.gamesFile + filename);
			int gameId = facade.getGameId();
			this.games.put(gameId, facade);
			return true;
		} catch (IOException | CatanException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean saveGameToFile(int gameId, String fileName) throws FileNotFoundException {
		ServerModelFacade facade = this.games.get(gameId);
		String jsonFacade = CatanSerializer.getInstance().serializeObject(facade.getModel());

		PrintWriter writer = new PrintWriter(this.gamesFile + fileName);
		writer.print(jsonFacade);
		writer.close();
		return true;
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
		ServerModelFacade facade = this.games.get(gameId);
		int size = facade.getPlayers().size();
		Player existingPlayer = facade.getPlayer(user.getUserId());
		boolean colorIsTaken = facade.getTakenColors().contains(color);

		if (existingPlayer == null) {
			if (size >= MAX_PLAYERS) {
				throw new CatanException(CatanExceptionType.ILLEGAL_MOVE, "Game is full");
			}
			if (colorIsTaken) {
				throw new CatanException(CatanExceptionType.ILLEGAL_MOVE, "Color is taken");
			}
			facade.joinGame(user, color);
		}
		else {
			if (existingPlayer.getColor() != color && colorIsTaken) {
				throw new CatanException(CatanExceptionType.ILLEGAL_MOVE, "Color is taken");
			}
			existingPlayer.setColor(color);
		}

		return new GameCertificate(gameId);
	}

	public TransportModel resetGame(int gameId) {
		ServerModelFacade oldFacade = this.games.get(gameId);

		if (oldFacade == null) {
			return null;
		}

		String name = oldFacade.getName();
		boolean randomTiles = oldFacade.getRandomTiles();
		boolean randomNumbers = oldFacade.getRandomNumbers();
		boolean randomPorts = oldFacade.getRandomPorts();

		ServerModelFacade newFacade = new ServerModelFacade(gameId, name, randomTiles,
				randomNumbers, randomPorts);
		
		newFacade.setAllPlayers(oldFacade.getPlayers());
		
		this.games.put(gameId, newFacade);
		return this.games.get(gameId).getModel();
	}
}
