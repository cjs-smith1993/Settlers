package serverCommunication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
//import java.net.URLDecoder;
import java.util.Collection;

import com.google.gson.reflect.TypeToken;

import shared.definitions.*;
import shared.locations.*;
import clientBackend.CatanSerializer;
import clientBackend.dataTransportObjects.*;
import clientBackend.model.CatanException;

/**
 * Implements the server interface and acts as a proxy server so the client does
 * not have to worry about communication details
 */
public class ServerProxy implements ServerInterface {
	private static ServerProxy proxy;
	private CatanSerializer serializer;
	private String userCookie;
	private String gameCookie;
	private String hostname;
	private int port;

	private final String HTTP_GET = "GET";
	private final String HTTP_POST = "POST";

	private ServerProxy(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
		this.serializer = CatanSerializer.getInstance();
	}
	
	public static ServerProxy getInstance() {
		if (proxy == null) {
			proxy = new ServerProxy("localhost", 8081);
		}
		
		return proxy;
	}

	public String getHostname() {
		return this.hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public int getPort() {
		return this.port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	private String getUrlPrefix() {
		return "http://" + this.hostname + ":" + this.port;
	}

	private String getCookies() {
		String cookieString = "";

		if (this.userCookie != null) {
			cookieString += "catan.user=" + this.userCookie;
		}
		if (this.gameCookie != null) {
			cookieString += "; catan.game=" + this.gameCookie;
		}

		return cookieString;
	}

	private void parseCookie(HttpURLConnection connection) {
		String cookieText = connection.getHeaderField("Set-cookie");
		if (cookieText != null) {
			int idx = cookieText.indexOf('=');
			String type = cookieText.substring(0, idx);
			cookieText = cookieText.replaceAll(type + "=", "").replaceAll(";Path=/;", "");
			//			String cookieJson = URLDecoder.decode(cookieText);

			if (type.equals("catan.user")) {
				this.userCookie = cookieText;
			}
			else {
				this.gameCookie = cookieText;
			}
		}
	}

	private String readFrom(HttpURLConnection connection) throws IOException, ServerException {
		StringBuilder sb = new StringBuilder();

		if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			this.parseCookie(connection);

			InputStreamReader isr = new InputStreamReader(connection.getInputStream());
			BufferedReader br = new BufferedReader(isr);

			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
			br.close();
		}
		else {
			String message = "Status code != 200";
			throw new ServerException(ServerExceptionType.INVALID_OPERATION, message);
		}

		return sb.toString();
	}

	private void writeTo(HttpURLConnection connection, Object data) throws IOException {

		String json = this.serializer.serializeObject(data);
		OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
		BufferedWriter bw = new BufferedWriter(osw);
		bw.write(json);
		bw.close();
	}

	private String doGet(URL url) throws IOException, ServerException {
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod(this.HTTP_GET);
		connection.connect();

		String response = this.readFrom(connection);

		return response;
	}

	private String doPost(URL url, Object data) throws IOException, ServerException {
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod(this.HTTP_POST);
		connection.setRequestProperty("Cookie", this.getCookies());

		//System.out.println(this.getCookies());

		connection.setDoOutput(true);
		connection.connect();

		this.writeTo(connection, data);
		String response = this.readFrom(connection);

		return response;
	}

	/**
	 * Attempts to deserialize a json model
	 *
	 * @param json
	 *            the new game model
	 */
	public void deserializeResponse(String json) {
		try {
			CatanSerializer.getInstance().deserializeModel(json);
		} catch (CatanException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean userLogin(String username, String password) {
		DTOUserLogin data = new DTOUserLogin(username, password);

		try {
			URL url = new URL(this.getUrlPrefix() + "/user/login");
			this.doPost(url, data);
		} catch (IOException | ServerException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public boolean userRegister(String username, String password) {
		DTOUserRegister data = new DTOUserRegister(username, password);

		try {
			URL url = new URL(this.getUrlPrefix() + "/user/register");
			this.doPost(url, data);
		} catch (IOException | ServerException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<DTOGame> gamesList() throws ServerException {
		try {
			URL url = new URL(this.getUrlPrefix() + "/games/list");
			String response = this.doGet(url);
			Type objectType = new TypeToken<Collection<DTOGame>>() {}.getType();
			Object o = serializer.deserializeObject(response, objectType);
			if (o instanceof Collection<?>) {// Make sure o is a Collection
				Collection<?> collection = (Collection<?>) o;
				if (collection.iterator().next() instanceof DTOGame) {// Make sure the Collection contains DTOGames
					return (Collection<DTOGame>) o;
				}
				else {
					throw new IOException();
				}
			}
			else {
				throw new IOException();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public DTOGame gamesCreate(
			boolean randomTiles,
			boolean randomNumbers,
			boolean randomPorts,
			String name) throws ServerException {
		DTOGamesCreate data = new DTOGamesCreate(randomTiles, randomNumbers, randomPorts, name);

		try {
			URL url = new URL(this.getUrlPrefix() + "/games/create");
			String response = this.doPost(url, data);
			Type objectType = new TypeToken<DTOGame>() {}.getType();
			Object o = serializer.deserializeObject(response, objectType);
			if (o instanceof DTOGame) {
				return (DTOGame) o;
			}
			else {
				throw new IOException();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean gamesJoin(int gameId, CatanColor color) throws ServerException {
		DTOGamesJoin data = new DTOGamesJoin(gameId, color);

		try {
			URL url = new URL(this.getUrlPrefix() + "/games/join");
			this.doPost(url, data);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean gamesSave(int gameId, String name) throws ServerException {
		DTOGamesSave data = new DTOGamesSave(gameId, name);

		try {
			URL url = new URL(this.getUrlPrefix() + "/games/save");
			this.doPost(url, data);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean gamesLoad(String name) throws ServerException {
		DTOGamesLoad data = new DTOGamesLoad(name);

		try {
			URL url = new URL(this.getUrlPrefix() + "/games/load");
			this.doPost(url, data);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public void gameModel(int version) throws ServerException {
		DTOGameModel data = new DTOGameModel(version);
		
		try {
			URL url = new URL(this.getUrlPrefix() + "/game/model");
			String response = this.doPost(url, data);
			this.deserializeResponse(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void gameReset() throws ServerException {
		try {
			URL url = new URL(this.getUrlPrefix() + "/game/reset");
			String response = this.doGet(url);
			this.deserializeResponse(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String gameCommands() throws ServerException {
		try {
			URL url = new URL(this.getUrlPrefix() + "/game/commands");
			String response = this.doGet(url);
			return response;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void gameCommands(String commandList) throws ServerException {
		try {
			URL url = new URL(this.getUrlPrefix() + "/game/commands");
			String response = this.doPost(url, commandList);
			this.deserializeResponse(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean gameAddAI(String AIType) throws ServerException {
		DTOAIType type = new DTOAIType(AIType);

		try {
			URL url = new URL(this.getUrlPrefix() + "/game/addAI");
			this.doPost(url, type);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<String> gameListAI() throws ServerException {
		try {
			URL url = new URL(this.getUrlPrefix() + "/game/listAI");
			String response = this.doGet(url);
			Type objectType = new TypeToken<Collection<String>>() {}.getType();
			Object o = CatanSerializer.getInstance().deserializeObject(response, objectType);
			if (o instanceof Collection<?>) {// Make sure o is a Collection
				Collection<?> collection = (Collection<?>) o;
				if (collection.iterator().next() instanceof String) {// Make sure the Collection contains String
					Collection<String> list = (Collection<String>) o;
					return list;
				}
				else {
					throw new IOException();
				}
			}
			else {
				throw new IOException();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void movesSendChat(int playerIndex, String content) throws ServerException {
		DTOMovesSendChat data = new DTOMovesSendChat(playerIndex, content);

		try {
			URL url = new URL(this.getUrlPrefix() + "/moves/sendChat");
			String response = this.doPost(url, data);
			this.deserializeResponse(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void movesRollNumber(int playerIndex, int number) throws ServerException {
		DTOMovesRollNumber data = new DTOMovesRollNumber(playerIndex, number);

		try {
			URL url = new URL(this.getUrlPrefix() + "/moves/rollNumber");
			String response = this.doPost(url, data);
			this.deserializeResponse(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void movesRobPlayer(
			int playerIndex,
			int victimIndex,
			HexLocation location) throws ServerException {
		int x = location.getX();
		int y = location.getY();
		DTOMovesRobPlayer data = new DTOMovesRobPlayer(playerIndex, victimIndex, x, y);

		try {
			URL url = new URL(this.getUrlPrefix() + "/moves/robPlayer");
			String response = this.doPost(url, data);
			this.deserializeResponse(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void movesFinishTurn(String type, int playerIndex) throws ServerException {
		DTOMovesFinishTurn data = new DTOMovesFinishTurn(playerIndex);

		try {
			URL url = new URL(this.getUrlPrefix() + "/moves/finishTurn");
			String response = this.doPost(url, data);
			this.deserializeResponse(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void movesBuyDevCard(String type, int playerIndex) throws ServerException {
		DTOMovesBuyDevCard data = new DTOMovesBuyDevCard(playerIndex);

		try {
			URL url = new URL(this.getUrlPrefix() + "/moves/finishTurn");
			String response = this.doPost(url, data);
			this.deserializeResponse(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void movesYear_of_Plenty(
			int playerIndex,
			ResourceType resource1,
			ResourceType resource2) throws ServerException {
		DTOMovesYearOfPlenty data = new DTOMovesYearOfPlenty(playerIndex, resource1, resource2);

		try {
			URL url = new URL(this.getUrlPrefix() + "/moves/Year_of_Plenty");
			String response = this.doPost(url, data);
			this.deserializeResponse(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void movesRoad_Building(int playerIndex, EdgeLocation edge1, EdgeLocation edge2)
			throws ServerException {

		HexLocation hex1 = edge1.getHexLoc();
		int x1 = hex1.getX();
		int y1 = hex1.getY();
		EdgeDirection dir1 = edge1.getDir();
		HexLocation hex2 = edge1.getHexLoc();
		int x2 = hex2.getX();
		int y2 = hex2.getY();
		EdgeDirection dir2 = edge2.getDir();
		DTOMovesRoadBuilding data = new DTOMovesRoadBuilding(playerIndex, x1, y1, dir1, x2, y2,
				dir2);

		try {
			URL url = new URL(this.getUrlPrefix() + "/moves/Road_Building");
			String response = this.doPost(url, data);
			this.deserializeResponse(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void movesSoldier(int playerIndex, int victimIndex, HexLocation location)
			throws ServerException {

		int x = location.getX();
		int y = location.getY();

		DTOMovesSoldier data = new DTOMovesSoldier(playerIndex, victimIndex, x, y);

		try {
			URL url = new URL(this.getUrlPrefix() + "/moves/Soldier");
			String response = this.doPost(url, data);
			this.deserializeResponse(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void movesMonopoly(ResourceType resource, int playerIndex)
			throws ServerException {
		DTOMovesMonopoly data = new DTOMovesMonopoly(resource, playerIndex);

		try {
			URL url = new URL(this.getUrlPrefix() + "/moves/Monopoly");
			String response = this.doPost(url, data);
			this.deserializeResponse(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void movesMonument(String type, int playerIndex) throws ServerException {
		DTOMovesMonument data = new DTOMovesMonument(playerIndex);

		try {
			URL url = new URL(this.getUrlPrefix() + "/moves/Monument");
			String response = this.doPost(url, data);
			this.deserializeResponse(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void movesBuildRoad(int playerIndex, EdgeLocation location, boolean free)
			throws ServerException {

		HexLocation hex = location.getHexLoc();
		int x = hex.getX();
		int y = hex.getY();
		EdgeDirection dir = location.getDir();

		DTOMovesBuildRoad data = new DTOMovesBuildRoad(playerIndex, x, y, dir, free);

		try {
			URL url = new URL(this.getUrlPrefix() + "/moves/buildRoad");
			String response = this.doPost(url, data);
			this.deserializeResponse(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void movesBuildSettlement(int playerIndex, VertexLocation location, boolean free)
			throws ServerException {

		HexLocation hex = location.getHexLoc();
		int x = hex.getX();
		int y = hex.getY();
		VertexDirection dir = location.getDir();

		DTOMovesBuildSettlement data = new DTOMovesBuildSettlement(playerIndex, x, y, dir, free);

		try {
			URL url = new URL(this.getUrlPrefix() + "/moves/buildSettlement");
			String response = this.doPost(url, data);
			this.deserializeResponse(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void movesBuildCity(int playerIndex, VertexLocation location) throws ServerException {

		HexLocation hex = location.getHexLoc();
		int x = hex.getX();
		int y = hex.getY();
		VertexDirection dir = location.getDir();

		DTOMovesBuildCity data = new DTOMovesBuildCity(playerIndex, x, y, dir);

		try {
			URL url = new URL(this.getUrlPrefix() + "/moves/buildCity");
			String response = this.doPost(url, data);
			this.deserializeResponse(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void movesOfferTrade(
			int playerIndex,
			int brick,
			int ore,
			int sheep,
			int wheat,
			int wood,
			int receiver) throws ServerException {
		DTOMovesOfferTrade data = new DTOMovesOfferTrade(playerIndex, brick, ore, sheep, wheat,
				wood, receiver);

		try {
			URL url = new URL(this.getUrlPrefix() + "/moves/offerTrade");
			String response = this.doPost(url, data);
			this.deserializeResponse(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void movesAcceptTrade(int playerIndex, boolean willAccept)
			throws ServerException {
		DTOMovesAcceptTrade data = new DTOMovesAcceptTrade(playerIndex, willAccept);

		try {
			URL url = new URL(this.getUrlPrefix() + "/moves/acceptTrade");
			String response = this.doPost(url, data);
			this.deserializeResponse(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void movesMaritimeTrade(
			int playerIndex,
			int ratio,
			ResourceType inputResource,
			ResourceType outputResource) throws ServerException {
		DTOMovesMaritimeTrade data = new DTOMovesMaritimeTrade(playerIndex, ratio, inputResource,
				outputResource);

		try {
			URL url = new URL(this.getUrlPrefix() + "/moves/maritimeTrade");
			String response = this.doPost(url, data);
			this.deserializeResponse(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void movesDiscardCards(
			int playerIndex,
			int brick,
			int ore,
			int sheep,
			int wheat,
			int wood) throws ServerException {
		DTOMovesDiscardCards data = new DTOMovesDiscardCards(playerIndex, brick, ore, sheep, wheat,
				wood);

		try {
			URL url = new URL(this.getUrlPrefix() + "/moves/discardCards");
			String response = this.doPost(url, data);
			this.deserializeResponse(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean utilChangeLogLevel(String logLevel) throws ServerException {
		DTOLogLevel data = new DTOLogLevel(logLevel);

		try {
			URL url = new URL(this.getUrlPrefix() + "/moves/discardCards");
			this.doPost(url, data);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}
}
