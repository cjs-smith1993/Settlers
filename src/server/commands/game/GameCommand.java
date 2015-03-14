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
	
	
	public CommandResponse execute() {
		return null;
	}

}
