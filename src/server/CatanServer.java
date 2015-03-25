package server;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

import server.handlers.GameHandler;
import server.handlers.GamesHandler;
import server.handlers.MovesHandler;
import server.handlers.UserHandler;
import server.util.Handlers;

/**
 * An encapsulation of the server for Catan. Each of the four groups of commands
 * has its own handler which handles routing to the appropriate destination.
 *
 */
public class CatanServer {
	private static CatanServer instance;
	private HttpServer server;
	private UserHandler userHandler;
	private GamesHandler gamesHandler;
	private GameHandler gameHandler;
	private MovesHandler movesHandler;

	private static final int MAX_WAITING_CONNECTIONS = 10;

	private CatanServer(int portNum) {
		try {
			InetSocketAddress addr = new InetSocketAddress(portNum);
			this.server = HttpServer.create(addr, MAX_WAITING_CONNECTIONS);
			this.server.setExecutor(null);

			//Server endpoints
			this.userHandler = new UserHandler();
			this.gamesHandler = new GamesHandler();
			this.gameHandler = new GameHandler();
			this.movesHandler = new MovesHandler();
			this.server.createContext("/user", this.userHandler);
			this.server.createContext("/games", this.gamesHandler);
			this.server.createContext("/game", this.gameHandler);
			this.server.createContext("/moves", this.movesHandler);

			//Swagger endpoints
			this.server.createContext("/docs/api/data", new Handlers.JSONAppender(""));
			this.server.createContext("/docs/api/view", new Handlers.BasicFile(""));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static CatanServer getInstance(int portNum) {
		if (instance == null) {
			instance = new CatanServer(portNum);
		}
		return instance;
	}

	public UserHandler getUserHandler() {
		return this.userHandler;
	}

	public void setUserHandler(UserHandler userHandler) {
		this.userHandler = userHandler;
	}

	public GamesHandler getGamesHandler() {
		return this.gamesHandler;
	}

	public void setGamesHandler(GamesHandler gamesHandler) {
		this.gamesHandler = gamesHandler;
	}

	public GameHandler getGameHandler() {
		return this.gameHandler;
	}

	public void setGameHandler(GameHandler gameHandler) {
		this.gameHandler = gameHandler;
	}

	public MovesHandler getMovesHandler() {
		return this.movesHandler;
	}

	public void setMovesHandler(MovesHandler movesHandler) {
		this.movesHandler = movesHandler;
	}

	/**
	 * Start the server
	 */
	public void start() {
		this.server.start();
	}

	public static void main(String args[]) {
		int portNum = args.length > 0 ? Integer.parseInt(args[0]) : 8081;
		CatanServer server = CatanServer.getInstance(portNum);
		server.start();
	}
}
