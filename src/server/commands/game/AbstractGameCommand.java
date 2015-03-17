package server.commands.game;

import server.CommandResponse;
import server.certificates.GameCertificate;
import server.certificates.UserCertificate;
import server.commands.ICommand;
import server.core.ICortex;

/**
* Represents the notion of executing the appropriate action for a given server
* endpoint that begins with /game/
*/

public abstract class AbstractGameCommand implements ICommand {
	
	ICortex cortex;
	
	public AbstractGameCommand(ICortex cortex) {
		this.cortex = cortex;
	}
	
	public CommandResponse execute() {
		return null;
	}

}
