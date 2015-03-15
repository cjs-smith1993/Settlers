package server.commands.game;

import server.CommandResponse;
import server.certificates.GameCertificate;
import server.certificates.UserCertificate;
import server.commands.ICommand;

/**
* Represents the notion of executing the appropriate action for a given server
* endpoint that begins with /game/
*/

public abstract class GameCommand implements ICommand {
	private UserCertificate user;
	private GameCertificate game;
	
	public GameCommand(UserCertificate user, GameCertificate game) {
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
	
	private UserCertificate getUser() {
		return user;
	}

	private void setUser(UserCertificate user) {
		this.user = user;
	}

	private GameCertificate getGame() {
		return game;
	}

	private void setGame(GameCertificate game) {
		this.game = game;
	}

}
