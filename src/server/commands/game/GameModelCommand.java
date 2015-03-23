package server.commands.game;

import client.serverCommunication.ServerException;
import server.commands.CommandResponse;
import shared.model.CatanException;

/**
 * Game command created when the user attempts to get the model of the current
 * game
 *
 */
public class GameModelCommand extends AbstractGameCommand {

	public GameModelCommand(String json) {
		// TODO Auto-generated constructor stub
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandResponse executeInner() throws CatanException, ServerException {
		return null;
	}

}
