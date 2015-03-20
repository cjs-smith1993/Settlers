package client.serverCommunication;

import java.io.IOException;
import java.util.Collection;

import shared.dataTransportObjects.DTOGame;
import shared.definitions.*;
import shared.locations.*;
import shared.model.ResourceInvoice;
import shared.model.ModelUser;

/*the majority of these things change the server model
 * so in response the server returns with the new updated model
 * once we decide how we will get that new model to our old model
 * we can have a clearer picture what types to return.
 */

public interface ServerInterface {

	/*
	 * User methods
	 */

	public ModelUser userLogin(String username, String password);

	public boolean userRegister(String username, String password);

	/*
	 * Games methods
	 */

	public Collection<DTOGame> gamesList();

	public DTOGame gamesCreate(boolean randomTiles, boolean randomNumbers, boolean randomPorts,
			String name);

	//The json is the name of the game, the id of the game, and the list of players of the game

	public boolean gamesJoin(int gameId, CatanColor color);//if they successfully joined the game

	public boolean gamesSave(int gameId, String name);//success of the save

	public boolean gamesLoad(String name);//success of the load

	/*
	 * Game methods
	 */

	public void gameModel(int version) throws IOException, ServerException;//the return type will depend on if we make listener or if we try to send this to a deserializer

	public String gameModel() throws IOException, ServerException;
	
	public void gameReset();//also returns the whole game json thing

	public String gameCommands();//this will return a list of commands that we could just write to a file

	public void gameCommands(String commandList);//this will return the json game after the list entered were run

	public boolean gameAddAI(AIType AItype);//boolean if they were successfully added

	public Collection<AIType> gameListAI(); //returns a json blob that has the types of AI

	/*
	 * Moves methods
	 */

	public boolean movesSendChat(PlayerNumber playerIndex, String content);//returns an updated json of the game there is a message portion of the json blob

	public boolean movesRollNumber(
			PlayerNumber playerIndex,
			int number);//returns updated Json

	public boolean movesRobPlayer(
			PlayerNumber playerIndex,
			PlayerNumber victimIndex,
			HexLocation location);//returns updated json

	public boolean movesFinishTurn(PlayerNumber playerIndex);//returns updated json

	public boolean movesBuyDevCard(PlayerNumber playerIndex);//returns updated json

	public boolean movesYear_of_Plenty(
			PlayerNumber playerIndex,
			ResourceType resource1,
			ResourceType resource2);//returns updated json

	public boolean movesRoad_Building(
			PlayerNumber playerIndex,
			EdgeLocation spot1,
			EdgeLocation spot2); //returns updated json

	public boolean movesSoldier(
			PlayerNumber playerIndex,
			PlayerNumber victimIndex,
			HexLocation location);//returns updated json

	public boolean movesMonopoly(
			ResourceType resource,
			PlayerNumber playerIndex);//returns updated json

	public boolean movesMonument(PlayerNumber playerIndex);//returns updated json

	public boolean movesBuildRoad(
			PlayerNumber playerIndex,
			EdgeLocation location,
			boolean free);//returns updated json

	public boolean movesBuildSettlement(
			PlayerNumber playerIndex,
			VertexLocation location,
			boolean free);//returns updated json

	public boolean movesBuildCity(
			PlayerNumber playerIndex,
			VertexLocation location);//returns updated json

	public boolean movesOfferTrade(ResourceInvoice invoice);//returns updated json

	public boolean movesAcceptTrade(
			PlayerNumber playerIndex,
			boolean willAccept);//returns updated json

	public boolean movesMaritimeTrade(
			PlayerNumber playerIndex,
			int ratio,
			ResourceType inputResource,
			ResourceType outputResource);//returns updated json

	public boolean movesDiscardCards(
			PlayerNumber playerIndex, int brick, int ore, int sheep, int wheat, int wood);//returns updated json

	public boolean utilChangeLogLevel(String logLevel);//if the log level was changed correctly
}
