package serverCommunication;

import java.util.Collection;

import clientBackend.dataTransportObjects.DTOGame;
import shared.definitions.*;
import shared.locations.*;

/*the majority of these things change the server model
 * so in response the server returns with the new updated model
 * once we decide how we will get that new model to our old model
 * we can have a clearer picture what types to return.
 */

public interface ServerInterface {
	public boolean userLogin(String username, String password) throws ServerException;

	public boolean userRegister(String username, String password) throws ServerException;

	public Collection<DTOGame> gamesList() throws ServerException;

	public DTOGame gamesCreate(
			boolean randomTiles,
			boolean randomNumbers,
			boolean randomPorts,
			String name) throws ServerException;//The json is the name of the game, the id of the game, and the list of players of the game

	public boolean gamesJoin(int gameId, CatanColor color) throws ServerException;//if they successfully joined the game

	public boolean gamesSave(int gameId, String name) throws ServerException;//success of the save

	public boolean gamesLoad(String name) throws ServerException;//success of the load

	public void gameModel(int version) throws ServerException;//the return type will depend on if we make listener or if we try to send this to a deserializer

	public void gameReset() throws ServerException;//also returns the whole game json thing

	public String gameCommands() throws ServerException;//this will return a list of commands that we could just write to a file

	public void gameCommands(String commandList) throws ServerException;//this will return the json game after the list entered were run

	public boolean gameAddAI(String AIType) throws ServerException;//boolean if they were successfully added

	public Collection<String> gameListAI() throws ServerException; //returns a json blob that has the types of AI

	public void movesSendChat(
			int playerIndex,
			String content) throws ServerException;//returns an updated json of the game there is a message portion of the json blob

	public void movesRollNumber(
			int playerIndex,
			int number) throws ServerException;//returns updated Json

	public void movesRobPlayer(
			int playerIndex,
			int victimIndex,
			HexLocation location) throws ServerException;//returns updated json

	public void movesFinishTurn(String type, int playerIndex) throws ServerException;//returns updated json

	public void movesBuyDevCard(String type, int playerIndex) throws ServerException;//returns updated json

	public void movesYear_of_Plenty(
			int playerIndex,
			ResourceType resource1,
			ResourceType resource2) throws ServerException;//returns updated json

	public void movesRoad_Building(
			int playerIndex,
			EdgeLocation spot1,
			EdgeLocation spot2) throws ServerException; //returns updated json

	public void movesSoldier(
			int playerIndex,
			int victimIndex,
			HexLocation location) throws ServerException;//returns updated json

	public void movesMonopoly(
			ResourceType resource,
			int playerIndex) throws ServerException;//returns updated json

	public void movesMonument(String type, int playerIndex) throws ServerException;//returns updated json

	public void movesBuildRoad(
			int playerIndex,
			EdgeLocation location,
			boolean free) throws ServerException;//returns updated json

	public void movesBuildSettlement(
			int playerIndex,
			VertexLocation location,
			boolean free) throws ServerException;//returns updated json

	public void movesBuildCity(
			int playerIndex,
			VertexLocation location) throws ServerException;//returns updated json

	public void movesOfferTrade(
			int playerIndex, int brick, int ore,
			int sheep, int wheat, int wood,
			int receiver) throws ServerException;//returns updated json

	public void movesAcceptTrade(
			int playerIndex,
			boolean willAccept) throws ServerException;//returns updated json

	public void movesMaritimeTrade(
			int playerIndex,
			int ratio,
			ResourceType inputResource,
			ResourceType outputResource) throws ServerException;//returns updated json

	public void movesDiscardCards(
			int playerIndex, int brick, int ore, int sheep, int wheat, int wood)
			throws ServerException;//returns updated json

	public boolean utilChangeLogLevel(String logLevel) throws ServerException;//if the log level was changed correctly
}
