package server.commands.moves;

import client.backend.CatanSerializer;
import client.serverCommunication.ServerException;
import server.core.CortexFactory;
import server.core.ICortex;
import shared.dataTransportObjects.DTOMovesSendChat;
import shared.definitions.PlayerNumber;
import shared.model.CatanException;
import shared.transport.TransportModel;

/**
 * Moves command created when a user attempts to send a chat.
 *
 */
public class MovesSendChatCommand extends AbstractMovesCommand {
	public static final String type = "sendChat";

	private PlayerNumber playerIndex;
	private String content;

	public MovesSendChatCommand(String json) {
		DTOMovesSendChat dto = (DTOMovesSendChat) CatanSerializer.getInstance()
				.deserializeObject(json, DTOMovesSendChat.class);

		this.playerIndex = PlayerNumber.getPlayerNumber(dto.playerIndex);
		this.content = dto.content;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel performMovesCommand() throws CatanException, ServerException {
		ICortex cortex = CortexFactory.getInstance().getCortex();
		return cortex.movesSendChat(this.playerIndex, this.content, this.getGameId(),
				this.getPlayerId());
	}

}
