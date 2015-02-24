package serverCommunication;

import java.io.IOException;
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
	public boolean userLogin(String username, String password);

	public boolean userRegister(String username, String password);

	public Collection<DTOGame> gamesList();

	public DTOGame gamesCreate(
			boolean randomTiles,
			boolean randomNumbers,
			boolean randomPorts,
			String name);//The json is the name of the game, the id of the game, and the list of players of the game

	public boolean gamesJoin(int gameId, CatanColor color);//if they successfully joined the game

	public boolean gamesSave(int gameId, String name);//success of the save

	public boolean gamesLoad(String name);//success of the load

	public void gameModel(int version) throws IOException, ServerException;//the return type will depend on if we make listener or if we try to send this to a deserializer

	public void gameReset();//also returns the whole game json thing

	public String gameCommands();//this will return a list of commands that we could just write to a file

	public void gameCommands(String commandList);//this will return the json game after the list entered were run

	public boolean gameAddAI(AIType AItype);//boolean if they were successfully added

	public Collection<AIType> gameListAI(); //returns a json blob that has the types of AI

	public void movesSendChat(int playerIndex, String content);//returns an updated json of the game there is a message portion of the json blob

	public void movesRollNumber(
			int playerIndex,
			int number);//returns updated Json

	public void movesRobPlayer(
			int playerIndex,
			int victimIndex,
			HexLocation location);//returns updated json

	public void movesFinishTurn(String type, int playerIndex);//returns updated json

	public void movesBuyDevCard(String type, int playerIndex);//returns updated json

	public void movesYear_of_Plenty(
			int playerIndex,
			ResourceType resource1,
			ResourceType resource2);//returns updated json

	public void movesRoad_Building(
			int playerIndex,
			EdgeLocation spot1,
			EdgeLocation spot2); //returns updated json

	public void movesSoldier(
			int playerIndex,
			int victimIndex,
			HexLocation location);//returns updated json

	public void movesMonopoly(
			ResourceType resource,
			int playerIndex);//returns updated json

	public void movesMonument(String type, int playerIndex);//returns updated json

	public void movesBuildRoad(
			int playerIndex,
			EdgeLocation location,
			boolean free);//returns updated json

	public void movesBuildSettlement(
			int playerIndex,
			VertexLocation location,
			boolean free);//returns updated json

	public void movesBuildCity(
			int playerIndex,
			VertexLocation location);//returns updated json

	public void movesOfferTrade(
			int playerIndex, int brick, int ore,
			int sheep, int wheat, int wood,
			int receiver);//returns updated json

	public void movesAcceptTrade(
			int playerIndex,
			boolean willAccept);//returns updated json

	public void movesMaritimeTrade(
			int playerIndex,
			int ratio,
			ResourceType inputResource,
			ResourceType outputResource);//returns updated json

	public void movesDiscardCards(
			int playerIndex, int brick, int ore, int sheep, int wheat, int wood)
			throws ServerException;//returns updated json

	public boolean utilChangeLogLevel(String logLevel);//if the log level was changed correctly
}
