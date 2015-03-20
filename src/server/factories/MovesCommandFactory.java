package server.factories;

import server.commands.moves.*;
import server.core.CortexFactory;
import server.core.ICortex;

/**
 * A factory for creating instances of IMovesCommand corresponding to a given
 * request. The factory uses a string to determine the appropriate command type,
 * then returns an instance of that type.
 */
public class MovesCommandFactory {

	private static MovesCommandFactory instance;

	private MovesCommandFactory() {

	}

	public static MovesCommandFactory getInstance() {
		if (instance == null) {
			instance = new MovesCommandFactory();
		}
		return instance;
	}

	/**
	 * Parses the given type and returns an instance of a corresponding command
	 *
	 * @param type
	 *            A String containing the type of the command
	 * @param json
	 *            A JSON blob containing the information for the command
	 * @return
	 */
	public AbstractMovesCommand getCommand(String type, String json) {
		AbstractMovesCommand cmd = null;
		ICortex cortex = CortexFactory.getInstance().getCortex();

		switch (type) {
		case "sendChat":
			cmd = new MovesSendChatCommand(json);
			break;
		case "rollNumber":
			cmd = new MovesRollNumberCommand(json);
			break;
		case "robPlayer":
			cmd = new MovesRobPlayerCommand(json);
			break;
		case "finishTurn":
			cmd = new MovesFinishTurnCommand(json);
			break;
		case "buyDevCard":
			cmd = new MovesBuyDevCardCommand(json);
			break;
		case "Year_of_Plenty":
			cmd = new MovesYearOfPlentyCommand(json);
			break;
		case "Road_Building":
			cmd = new MovesRoadBuildingCommand(json);
			break;
		case "Soldier":
			cmd = new MovesSoldierCommand(json);
			break;
		case "Monopoly":
			cmd = new MovesMonopolyCommand(json);
			break;
		case "Monument":
			cmd = new MovesMonumentCommand(json);
			break;
		case "buildRoad":
			cmd = new MovesBuildRoadCommand(json);
			break;
		case "buildSettlement":
			cmd = new MovesBuildSettlementCommand(json);
			break;
		case "buildCity":
			cmd = new MovesBuildCityCommand(json);
			break;
		case "offerTrade":
			cmd = new MovesOfferTradeCommand(json);
			break;
		case "acceptTrade":
			cmd = new MovesAcceptTradeCommand(json);
			break;
		case "maritimeTrade":
			cmd = new MovesMaritimeTradeCommand(json);
			break;
		case "discardCards":
			cmd = new MovesDiscardCardsCommand(json);
			break;
		}

		return cmd;
	}
}
