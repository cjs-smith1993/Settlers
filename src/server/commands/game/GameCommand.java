package server.commands.game;

import server.CommandResponse;
import server.commands.ICommand;
import server.cookies.GameCookie;
import server.cookies.UserCookie;

/**
* Represents the notion of executing the appropriate action for a given server
* endpoint that begins with /game/
*/

public abstract class GameCommand implements ICommand {
	private UserCookie user;
	private GameCookie game;
	
	public GameCommand(UserCookie user, GameCookie game) {
		this.setUser(user);
		this.setGame(game);
	}
	
	public CommandResponse execute() {
		return null;
	}

	/*
	 * These getters and setters are private because I don't think we want the variables to be 
	 * changed or accessed by any other class. These methods should only be used internally 
	 * -kevinjreece
	 */
	
	private UserCookie getUser() {
		return user;
	}

	private void setUser(UserCookie user) {
		this.user = user;
	}

	private GameCookie getGame() {
		return game;
	}

	private void setGame(GameCookie game) {
		this.game = game;
	}

}
