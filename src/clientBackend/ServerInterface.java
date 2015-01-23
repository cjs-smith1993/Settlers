package clientBackend;

import java.util.Collection;

import clientBackend.model.Game;
import clientBackend.model.ResourceCard;
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
			String name);//The json is the name of the game, the id of the game, and the list of players of the game
	public boolean gamesJoin(int gameId, CatanColor color);//if they successfully joined the game
	public boolean gamesSave(int gameId, String name);//success of the save
	public boolean gamesLoad(String name);//success of the load
	public void gameModel(int version);//the return type will depend on if we make listener or if we try to send this to a deserializer
	public void gameReset();//also returns the whole game json thing
	public void gameCommands();//this will return a list of commands that we could just write to a file
	public void gameCommands(String commandList);//this will return the json game after the list entered were run
	public void gameAddAI(String AIType);//boolean if they were successfully added
	public void gameListAI(); //returns a json blob that has the types of AI
	public void movesSendChat(
			PlayerNumber playerIndex,
			String content);//returns an updated json of the game there is a message portion of the json blob
	public void movesRollNumber(
			PlayerNumber playerIndex,
			int number);//returns updated Json
	public void movesRobPlayer(
			PlayerNumber playerIndex,
			PlayerNumber victimIndex,
			HexLocation location);//returns updated json
	public void movesFinishTurn(String type, PlayerNumber playerIndex);//returns updated json
	public void movesBuyDevCard(String type, PlayerNumber playerIndex);//returns updated json
	public void movesYear_of_Plenty(
			PlayerNumber playerIndex,
			ResourceType resource1,
			ResourceType resource2);//returns updated json
	public void movesRoad_Building(
			PlayerNumber playerIndex,
			EdgeLocation spot1,
			EdgeLocation spot2); //returns updated json
	public void movesSoldier(
			PlayerNumber playerIndex,
			PlayerNumber victimIndex,
			HexLocation location);//returns updated json
	public void movesMonopoly(
			ResourceType resource,
			PlayerNumber playerIndex);//returns updated json
	public void movesMonument(String type, PlayerNumber playerIndex);//returns updated json
	public void movesBuildRoad(
			PlayerNumber playerIndex,
			EdgeLocation location,
			boolean free);//returns updated json
	public void movesBuildSettlement(
			PlayerNumber playerIndex,
			VertexLocation location,
			boolean free);//returns updated json
	public void movesBuildCity(
			PlayerNumber playerIndex,
			VertexLocation location);//returns updated json
	public void movesOfferTrade(
			PlayerNumber playerIndex,
			Collection<ResourceInvoice> offer,
			PlayerNumber receiver);//returns updated json
	public void movesAcceptTrade(
			PlayerNumber playerIndex,
			boolean willAccept);//returns updated json
	public void movesMaritimeTrade(
			PlayerNumber playerIndex,
			int ratio,
			ResourceType inputResource,
			ResourceType outputResource);//returns updated json
	public void movesDiscardCards(
			PlayerNumber playerIndex,
			Collection<ResourceCard> discardedCards);//returns updated json
	public boolean utilChangeLogLevel(String logLevel);//if the log level was changed correctly
}
