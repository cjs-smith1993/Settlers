package server.core;

import java.io.IOException;
import java.util.Collection;

import com.google.gson.JsonObject;

import server.cookies.GameCookie;
import server.cookies.UserCookie;
import shared.definitions.CatanColor;
import shared.definitions.PlayerNumber;
import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.ResourceInvoice;
import shared.transport.TransportModel;
import client.backend.dataTransportObjects.DTOGame;
import client.serverCommunication.ServerException;

/**
 * The interface the Commands from use to manipulate user and game data
 * @author kevinjreece
 */
public interface ICortex {

	/*
	 * User methods
	 */
	
	public UserCookie userLogin(String username, String password);

	public UserCookie userRegister(String username, String password);

	/*
	 * Games methods
	 */

	public Collection<DTOGame> gamesList(UserCookie user);

	public DTOGame gamesCreate(UserCookie user, boolean randomTiles, boolean randomNumbers, boolean randomPorts,
			String name);

	public GameCookie gamesJoin(UserCookie user, int gameId, CatanColor color);

	public boolean gamesSave(UserCookie user, int gameId, String name);

	public boolean gamesLoad(UserCookie user, String name);

	/*
	 * Game methods
	 */

	public TransportModel gameModel(UserCookie user, GameCookie game, int version) throws IOException, ServerException;//the return type will depend on if we make listener or if we try to send this to a deserializer

	public TransportModel gameModel(UserCookie user, GameCookie game) throws IOException, ServerException;
	
	public TransportModel gameReset(UserCookie user, GameCookie game);

	public Collection<JsonObject> gameCommands(UserCookie user, GameCookie game);

	public TransportModel gameCommands(UserCookie user, GameCookie game, String commandList);

//	public boolean gameAddAI(AIType AItype);//boolean if they were successfully added
//
//	public Collection<AIType> gameListAI(); //returns a json blob that has the types of AI

	/*
	 * Moves methods
	 */

	/**
	 * If user and game are authentic, call sendChat() on the appropriate game Facade
	 * @param user
	 * @param game
	 * @param playerIndex
	 * @param content
	 * @return
	 */
	public boolean movesSendChat(UserCookie user, GameCookie game, PlayerNumber playerIndex, String content);//returns an updated json of the game there is a message portion of the json blob

	public boolean movesRollNumber(UserCookie user, GameCookie game, 
			PlayerNumber playerIndex,
			int number);//returns updated Json

	public boolean movesRobPlayer(UserCookie user, GameCookie game, 
			PlayerNumber playerIndex,
			PlayerNumber victimIndex,
			HexLocation location);//returns updated json

	public boolean movesFinishTurn(UserCookie user, GameCookie game, PlayerNumber playerIndex);//returns updated json

	public boolean movesBuyDevCard(UserCookie user, GameCookie game, PlayerNumber playerIndex);//returns updated json

	public boolean movesYear_of_Plenty(UserCookie user, GameCookie game, 
			PlayerNumber playerIndex,
			ResourceType resource1,
			ResourceType resource2);//returns updated json

	public boolean movesRoad_Building(UserCookie user, GameCookie game, 
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

}
