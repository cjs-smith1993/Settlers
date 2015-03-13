package server.core;

import java.io.IOException;
import java.util.Collection;

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

import com.google.gson.JsonObject;

/**
 * This is our HAL9000.  It will be responsible for interfacing with our Game and User managers.  
 * This will be our server facade so that we can test our connections with a proxy. 
 *
 */
public class CentralCortex implements ICortex {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserCookie userLogin(String username, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserCookie userRegister(String username, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<DTOGame> gamesList(UserCookie user) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DTOGame gamesCreate(UserCookie user, boolean randomTiles,
			boolean randomNumbers, boolean randomPorts, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GameCookie gamesJoin(UserCookie user, int gameId, CatanColor color) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean gamesSave(UserCookie user, int gameId, String name) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean gamesLoad(UserCookie user, String name) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel gameModel(UserCookie user, GameCookie game,
			int version) throws IOException, ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel gameModel(UserCookie user, GameCookie game)
			throws IOException, ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel gameReset(UserCookie user, GameCookie game) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<JsonObject> gameCommands(UserCookie user, GameCookie game) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel gameCommands(UserCookie user, GameCookie game,
			Collection<JsonObject> commandList) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesSendChat(UserCookie user, GameCookie game,
			PlayerNumber playerIndex, String content) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesRollNumber(UserCookie user, GameCookie game,
			PlayerNumber playerIndex, int number) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesRobPlayer(UserCookie user, GameCookie game,
			PlayerNumber playerIndex, PlayerNumber victimIndex,
			HexLocation location) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesFinishTurn(UserCookie user, GameCookie game,
			PlayerNumber playerIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesBuyDevCard(UserCookie user, GameCookie game,
			PlayerNumber playerIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesYear_of_Plenty(UserCookie user, GameCookie game,
			PlayerNumber playerIndex, ResourceType resource1,
			ResourceType resource2) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesRoad_Building(UserCookie user, GameCookie game,
			PlayerNumber playerIndex, EdgeLocation spot1, EdgeLocation spot2) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesSoldier(UserCookie user, GameCookie game,
			PlayerNumber playerIndex, PlayerNumber victimIndex,
			HexLocation location) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesMonopoly(UserCookie user, GameCookie game,
			ResourceType resource, PlayerNumber playerIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesMonument(UserCookie user, GameCookie game,
			PlayerNumber playerIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesBuildRoad(UserCookie user, GameCookie game,
			PlayerNumber playerIndex, EdgeLocation location, boolean free) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesBuildSettlement(UserCookie user,
			GameCookie game, PlayerNumber playerIndex, VertexLocation location,
			boolean free) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesBuildCity(UserCookie user, GameCookie game,
			PlayerNumber playerIndex, VertexLocation location) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesOfferTrade(UserCookie user, GameCookie game,
			ResourceInvoice invoice) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesAcceptTrade(UserCookie user, GameCookie game,
			PlayerNumber playerIndex, boolean willAccept) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesMaritimeTrade(UserCookie user, GameCookie game,
			PlayerNumber playerIndex, int ratio, ResourceType inputResource,
			ResourceType outputResource) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesDiscardCards(UserCookie user, GameCookie game,
			PlayerNumber playerIndex, int brick, int ore, int sheep, int wheat,
			int wood) {
		// TODO Auto-generated method stub
		return null;
	}

}
