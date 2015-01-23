package clientBackend.model;

import java.util.Collection;

import shared.definitions.*;
import shared.locations.*;

public interface ServerInterface {
	public void userLogin(String username, String password);
	public void userRegister(String username, String password);
	public Collection<Game> gamesList();
	public void gamesCreate(
			boolean randomTiles,
			boolean randomNumbers,
			boolean randomPorts,
			String name);
	public void gamesJoin(int gameId, CatanColor color);
	public void gamesSave(int gameId, String name);
	public void gamesLoad(String name);
	public void gameModel(int version);
	public void gameReset();
	public void gameCommands();
	public void gameCommands(String commandList);
	public void gameAddAI(String AIType);
	public void gameListAI();
	public void movesSendChat(
			String type,
			PlayerNumber playerIndex,
			String content);
	public void movesRollNumber(
			String type,
			PlayerNumber playerIndex,
			int number);
	public void movesRobPlayer(
			String type,
			PlayerNumber playerIndex,
			PlayerNumber victimIndex,
			HexLocation location);
	public void movesFinishTurn(String type, PlayerNumber playerIndex);
	public void movesBuyDevCard(String type, PlayerNumber playerIndex);
	public void movesYear_of_Plenty(
			String type,
			PlayerNumber playerIndex,
			ResourceType resource1,
			ResourceType resource2);
	public void movesRoad_Building(
			String type,
			PlayerNumber playerIndex,
			EdgeLocation spot1,
			EdgeLocation spot2); 
	public void movesSoldier(
			String type,
			PlayerNumber playerIndex,
			PlayerNumber victimIndex,
			HexLocation location);
	public void movesMonopoly(
			String type,
			ResourceType resource,
			PlayerNumber playerIndex);
	public void movesMonument(String type, PlayerNumber playerIndex);
	public void movesBuildRoad(
			String type,
			PlayerNumber playerIndex,
			EdgeLocation location,
			boolean free);
	public void movesBuildSettlement(
			String type,
			PlayerNumber playerIndex,
			VertexLocation location,
			boolean free);
	public void movesBuildCity(
			String type,
			PlayerNumber playerIndex,
			VertexLocation location);
	public void movesOfferTrade(
			String type,
			PlayerNumber playerIndex,
			Collection<ResourceInvoice> offer,
			PlayerNumber receiver);
	public void movesAcceptTrade(
			String type,
			PlayerNumber playerIndex,
			boolean willAccept);
	public void movesMaritimeTrade(
			String type,
			PlayerNumber playerIndex,
			int ratio,
			ResourceType inputResource,
			ResourceType outputResource);
	public void movesDiscardCards(
			String type,
			PlayerNumber playerIndex,
			Collection<ResourceCard> discardedCards);
	public void utilChangeLogLevel(String logLevel);
}
