package server.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import server.certificates.GameCertificate;
import server.certificates.UserCertificate;
import shared.definitions.CatanColor;
import shared.definitions.PlayerNumber;
import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.CatanException;
import shared.model.ResourceInvoice;
import shared.transport.TransportModel;
import client.backend.dataTransportObjects.DTOGame;
import client.backend.dataTransportObjects.DTOPlayer;
import client.serverCommunication.ServerException;

import com.google.gson.JsonObject;

public class MockCortex implements ICortex {

	private static MockCortex instance;

	private MockCortex() {

	}

	public static MockCortex getInstance() {
		if (instance == null) {
			instance = new MockCortex();
		}
		return instance;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean authenticateUser(UserCertificate userCert) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean authenticateGame(GameCertificate gameCert) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserCertificate userLogin(String username, String password) {
		return new UserCertificate(-1, username, password);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserCertificate userRegister(String username, String password) {
		return new UserCertificate(-1, username, password);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<DTOGame> gamesList() throws CatanException, ServerException {
		Collection<DTOGame> games = new ArrayList<DTOGame>();
		ArrayList<DTOPlayer> players = new ArrayList<DTOPlayer>();
		players.add(new DTOPlayer(CatanColor.ORANGE, "Sam", 0));
		players.add(new DTOPlayer(CatanColor.BLUE, "Brooke", 1));
		players.add(new DTOPlayer(CatanColor.RED, "Pete", 10));
		players.add(new DTOPlayer(CatanColor.GREEN, "Mark", 11));
		games.add(new DTOGame(0, "DefaultGame", players));
		//		[{'title':'DefaultGame','id':0,'players':[{'color':'orange','name':'Sam','id':0},{'color':'blue','name':'Brooke','id':1},{'color':'red','name':'Pete','id':10},{'color':'green','name':'Mark','id':11}]},{'title':'AIGame','id':1,'players':[{'color':'orange','name':'Pete','id':10},{'color':'puce','name':'Steve','id':-2},{'color':'blue','name':'Squall','id':-3},{'color':'yellow','name':'Quinn','id':-4}]},{'title':'EmptyGame','id':2,'players':[{'color':'orange','name':'Sam','id':0},{'color':'blue','name':'Brooke','id':1},{'color':'red','name':'Pete','id':10},{'color':'green','name':'Mark','id':11}]}]
		return games;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DTOGame gamesCreate(UserCertificate user, boolean randomTiles,
			boolean randomNumbers, boolean randomPorts, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GameCertificate gamesJoin(UserCertificate user, int gameId, CatanColor color) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean gamesSave(UserCertificate user, int gameId, String name) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean gamesLoad(UserCertificate user, String name) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel gameModel(UserCertificate user, GameCertificate game,
			int version) throws IOException, ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel gameModel(UserCertificate user, GameCertificate game)
			throws IOException, ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel gameReset(UserCertificate user, GameCertificate game) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<JsonObject> gameCommands(UserCertificate user, GameCertificate game) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel gameCommands(UserCertificate user, GameCertificate game,
			Collection<JsonObject> commandList) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesSendChat(UserCertificate user, GameCertificate game,
			PlayerNumber playerIndex, String content) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesRollNumber(UserCertificate user, GameCertificate game,
			PlayerNumber playerIndex, int number) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesRobPlayer(UserCertificate user, GameCertificate game,
			PlayerNumber playerIndex, PlayerNumber victimIndex,
			HexLocation location) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesFinishTurn(UserCertificate user, GameCertificate game,
			PlayerNumber playerIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesBuyDevCard(UserCertificate user, GameCertificate game,
			PlayerNumber playerIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesYear_of_Plenty(UserCertificate user, GameCertificate game,
			PlayerNumber playerIndex, ResourceType resource1,
			ResourceType resource2) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesRoad_Building(UserCertificate user, GameCertificate game,
			PlayerNumber playerIndex, EdgeLocation spot1, EdgeLocation spot2) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesSoldier(UserCertificate user, GameCertificate game,
			PlayerNumber playerIndex, PlayerNumber victimIndex,
			HexLocation location) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesMonopoly(UserCertificate user, GameCertificate game,
			ResourceType resource, PlayerNumber playerIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesMonument(UserCertificate user, GameCertificate game,
			PlayerNumber playerIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesBuildRoad(UserCertificate user, GameCertificate game,
			PlayerNumber playerIndex, EdgeLocation location, boolean free) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesBuildSettlement(UserCertificate user,
			GameCertificate game, PlayerNumber playerIndex, VertexLocation location,
			boolean free) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesBuildCity(UserCertificate user, GameCertificate game,
			PlayerNumber playerIndex, VertexLocation location) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesOfferTrade(UserCertificate user, GameCertificate game,
			ResourceInvoice invoice) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesAcceptTrade(UserCertificate user, GameCertificate game,
			PlayerNumber playerIndex, boolean willAccept) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesMaritimeTrade(UserCertificate user, GameCertificate game,
			PlayerNumber playerIndex, int ratio, ResourceType inputResource,
			ResourceType outputResource) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesDiscardCards(UserCertificate user, GameCertificate game,
			PlayerNumber playerIndex, int brick, int ore, int sheep, int wheat,
			int wood) {
		// TODO Auto-generated method stub
		return null;
	}

}
