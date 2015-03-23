package server.core;

import java.util.ArrayList;
import java.util.Collection;

import server.certificates.GameCertificate;
import server.certificates.UserCertificate;
import shared.dataTransportObjects.DTOGame;
import shared.dataTransportObjects.DTOPlayer;
import shared.definitions.CatanColor;
import shared.definitions.PlayerNumber;
import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.CatanException;
import shared.model.ResourceInvoice;
import shared.transport.TransportModel;
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
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean authenticateGame(GameCertificate gameCert) {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserCertificate userLogin(String username, String password) {
		// TODO Auto-generated method stub
		return new UserCertificate(-1, username, password);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserCertificate userRegister(String username, String password) {
		// TODO Auto-generated method stub
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
	public DTOGame gamesCreate(
			boolean randomTiles,
			boolean randomNumbers,
			boolean randomPorts,
			String name) throws CatanException, ServerException {

		ArrayList<DTOPlayer> players = new ArrayList<DTOPlayer>();
		return new DTOGame(-1, "mock game", players);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GameCertificate gamesJoin(int gameId, CatanColor color) throws CatanException,
			ServerException {
		return new GameCertificate(-1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean gamesSave(int gameId, String name) throws CatanException, ServerException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean gamesLoad(String name) throws CatanException, ServerException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel gameModel(int version) throws CatanException, ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel gameModel() throws CatanException, ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel gameReset() throws CatanException, ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<JsonObject> gameCommands() throws CatanException, ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel gameCommands(Collection<JsonObject> commandList) throws CatanException,
			ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesSendChat(PlayerNumber playerIndex, String content)
			throws CatanException, ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesRollNumber(PlayerNumber playerIndex, int number)
			throws CatanException, ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesRobPlayer(
			PlayerNumber playerIndex,
			PlayerNumber victimIndex,
			HexLocation location) throws CatanException, ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesFinishTurn(PlayerNumber playerIndex) throws CatanException,
			ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesBuyDevCard(PlayerNumber playerIndex) throws CatanException,
			ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesYear_of_Plenty(
			PlayerNumber playerIndex,
			ResourceType resource1,
			ResourceType resource2) throws CatanException, ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesRoad_Building(
			PlayerNumber playerIndex,
			EdgeLocation spot1,
			EdgeLocation spot2) throws CatanException, ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesSoldier(
			PlayerNumber playerIndex,
			PlayerNumber victimIndex,
			HexLocation location) throws CatanException, ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesMonopoly(ResourceType resource, PlayerNumber playerIndex)
			throws CatanException, ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesMonument(PlayerNumber playerIndex) throws CatanException,
			ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesBuildRoad(
			PlayerNumber playerIndex,
			EdgeLocation location,
			boolean free) throws CatanException, ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesBuildSettlement(
			PlayerNumber playerIndex,
			VertexLocation location,
			boolean free) throws CatanException, ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesBuildCity(PlayerNumber playerIndex, VertexLocation location)
			throws CatanException, ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesOfferTrade(ResourceInvoice invoice) throws CatanException,
			ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesAcceptTrade(PlayerNumber playerIndex, boolean willAccept)
			throws CatanException, ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesMaritimeTrade(
			PlayerNumber playerIndex,
			int ratio,
			ResourceType inputResource,
			ResourceType outputResource) throws CatanException,
			ServerException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransportModel movesDiscardCards(
			PlayerNumber playerIndex,
			int brick,
			int ore,
			int sheep,
			int wheat,
			int wood) throws CatanException, ServerException {
		// TODO Auto-generated method stub
		return null;
	}

}
