package server;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

import server.handlers.GameHandler;
import server.handlers.GamesHandler;
import server.handlers.MovesHandler;
import server.handlers.UserHandler;

/**
 * An encapsulation of the server for Catan. Each of the four groups of commands
 * has its own handler which handles routing to the appropriate destination.
 *
 */
public class CatanServer {
	private CatanServer instance;
	private HttpServer server;
	private UserHandler userHandler;
	private GamesHandler gamesHandler;
	private GameHandler gameHandler;
	private MovesHandler movesHandler;

	private static final int MAX_WAITING_CONNECTIONS = 10;

	private CatanServer(int portNum) {
		try {
			this.server = HttpServer
					.create(new InetSocketAddress(portNum), MAX_WAITING_CONNECTIONS);
			this.server.setExecutor(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public CatanServer getInstance(int portNum) {
		if (this.instance == null) {
			this.instance = new CatanServer(portNum);
		}
		return this.instance;
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

}
