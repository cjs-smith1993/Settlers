package server.commands.game;

import client.backend.CatanSerializer;
import client.serverCommunication.ServerException;
import server.commands.CommandResponse;
import server.commands.ContentType;
import server.core.CortexFactory;
import server.core.ICortex;
import server.util.StatusCode;
import shared.dataTransportObjects.DTOGameModel;
import shared.model.CatanException;
import shared.transport.TransportModel;

/**
 * Game command created when the user attempts to get the model of the current
 * game
 *
 */
public class GameModelCommand extends AbstractGameCommand {

	private int version = -1;

	public GameModelCommand(String json) {
		DTOGameModel dto = (DTOGameModel) CatanSerializer.getInstance().deserializeObject(json,
				DTOGameModel.class);

		if (dto != null) {
			this.version = dto.version;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandResponse executeInner() throws CatanException, ServerException {
		ICortex cortex = CortexFactory.getInstance().getCortex();
		
		CommandResponse response = null;
		TransportModel model = cortex.gameModel(this.version, this.getGameId());
		String body = CatanSerializer.getInstance().serializeObject(model);
		StatusCode status = StatusCode.OK;
		ContentType contentType = ContentType.JSON;

		response = new CommandResponse(body, status, contentType);
		return response;
	}

}
