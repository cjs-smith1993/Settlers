package server.commands.moves;

import com.google.gson.JsonParseException;

import client.backend.CatanSerializer;
import client.serverCommunication.ServerException;
import server.core.CortexFactory;
import server.core.ICortex;
import shared.dataTransportObjects.DTOMovesMaritimeTrade;
import shared.definitions.PlayerNumber;
import shared.definitions.ResourceType;
import shared.model.CatanException;
import shared.transport.TransportModel;

/**
 * Moves command created when a user attempts to use Maritime Trade.
 *
 */
public class MovesMaritimeTradeCommand extends AbstractMovesCommand {

	private PlayerNumber playerIndex;
	private int ratio;
	private ResourceType inputResource;
	private ResourceType outputResource;

	public MovesMaritimeTradeCommand(String json) {
		DTOMovesMaritimeTrade dto = (DTOMovesMaritimeTrade) CatanSerializer.getInstance()
				.deserializeObject(json, DTOMovesMaritimeTrade.class);

		if (dto.inputResource == null || dto.outputResource == null) {
			throw new JsonParseException("JSON parse error");
		}

		this.playerIndex = PlayerNumber.getPlayerNumber(dto.playerIndex);
		this.ratio = dto.ratio;
		this.inputResource = dto.inputResource;
		this.outputResource = dto.outputResource;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel performMovesCommand() throws CatanException, ServerException {
		ICortex cortex = CortexFactory.getInstance().getCortex();
		return cortex.movesMaritimeTrade(this.playerIndex, this.ratio, this.inputResource,
				this.outputResource);
	}

}
