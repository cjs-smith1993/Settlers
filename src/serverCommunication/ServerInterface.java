package serverCommunication;

import java.util.Collection;

import clientBackend.model.Game;
import clientBackend.model.ResourceCard;
import clientBackend.model.ResourceInvoice;
import shared.definitions.*;
import shared.locations.*;
/*the majority of these things change the server model
 * so in response the server returns with the new updated model
 * once we decide how we will get that new model to our old model 
 * we can have a clearer picture what types to return.
 */

public interface ServerInterface {
	public boolean userLogin(String username, String password);
	public boolean userRegister(String username, String password);
	public Collection<Game> gamesList();
	public void gamesCreate(
			boolean randomTiles,
			boolean randomNumbers,
			boolean randomPorts,
			String name) throws ServerException;//The json is the name of the game, the id of the game, and the list of players of the game
	public boolean gamesJoin(int gameId, CatanColor color);//if they successfully joined the game
	public boolean gamesSave(int gameId, String name);//success of the save
	public boolean gamesLoad(String name);//success of the load
	public void gameModel(int version) throws ServerException;//the return type will depend on if we make listener or if we try to send this to a deserializer
	public void gameReset() throws ServerException;//also returns the whole game json thing
	public void gameCommands() throws ServerException;//this will return a list of commands that we could just write to a file
	public void gameCommands(String commandList) throws ServerException;//this will return the json game after the list entered were run
	public void gameAddAI(String AIType) throws ServerException;//boolean if they were successfully added
	public void gameListAI() throws ServerException; //returns a json blob that has the types of AI
	public void movesSendChat(
			PlayerNumber playerIndex,
			String content) throws ServerException;//returns an updated json of the game there is a message portion of the json blob
	public void movesRollNumber(
			PlayerNumber playerIndex,
			int number) throws ServerException;//returns updated Json
	public void movesRobPlayer(
			PlayerNumber playerIndex,
			PlayerNumber victimIndex,
			HexLocation location) throws ServerException;//returns updated json
	public void movesFinishTurn(String type, PlayerNumber playerIndex) throws ServerException;//returns updated json
	public void movesBuyDevCard(String type, PlayerNumber playerIndex) throws ServerException;//returns updated json
	public void movesYear_of_Plenty(
			PlayerNumber playerIndex,
			ResourceType resource1,
			ResourceType resource2) throws ServerException;//returns updated json
	public void movesRoad_Building(
			PlayerNumber playerIndex,
			EdgeLocation spot1,
			EdgeLocation spot2) throws ServerException; //returns updated json
	public void movesSoldier(
			PlayerNumber playerIndex,
			PlayerNumber victimIndex,
			HexLocation location) throws ServerException;//returns updated json
	public void movesMonopoly(
			ResourceType resource,
			PlayerNumber playerIndex) throws ServerException;//returns updated json
	public void movesMonument(String type, PlayerNumber playerIndex) throws ServerException;//returns updated json
	public void movesBuildRoad(
			PlayerNumber playerIndex,
			EdgeLocation location,
			boolean free) throws ServerException;//returns updated json
	public void movesBuildSettlement(
			PlayerNumber playerIndex,
			VertexLocation location,
			boolean free) throws ServerException;//returns updated json
	public void movesBuildCity(
			PlayerNumber playerIndex,
			VertexLocation location) throws ServerException;//returns updated json
	public void movesOfferTrade(
			PlayerNumber playerIndex, int brick, int ore, 
			int sheep, int wheat, int wood,
			PlayerNumber receiver) throws ServerException;//returns updated json
	public void movesAcceptTrade(
			PlayerNumber playerIndex,
			boolean willAccept) throws ServerException;//returns updated json
	public void movesMaritimeTrade(
			PlayerNumber playerIndex,
			int ratio,
			ResourceType inputResource,
			ResourceType outputResource) throws ServerException;//returns updated json
	public void movesDiscardCards(
			PlayerNumber playerIndex, int brick, int ore, int sheep, int wheat, int wood) throws ServerException;//returns updated json
	public boolean utilChangeLogLevel(String logLevel)throws ServerException;//if the log level was changed correctly
}
