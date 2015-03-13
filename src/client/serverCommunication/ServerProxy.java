package client.serverCommunication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
//import java.net.URLDecoder;
import java.util.Collection;

import client.backend.CatanSerializer;
import client.backend.dataTransportObjects.*;

import com.google.gson.reflect.TypeToken;

import shared.definitions.*;
import shared.locations.*;
import shared.model.CatanException;
import shared.model.ResourceInvoice;
import shared.model.ModelUser;

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

	public static ServerProxy getInstance(String hostname, int port) {
		if (proxy == null) {
			proxy = new ServerProxy(hostname, port);
		}

		return proxy;
	}

	private String getUrlPrefix() {
		return "http://" + this.hostname + ":" + this.port;
	}

	public synchronized String getCookies() {
		String cookieString = "";

		if (this.userCookie != null) {
			cookieString += "catan.user=" + this.userCookie;
		}
		if (this.gameCookie != null) {
			cookieString += "; catan.game=" + this.gameCookie;
		}

		return cookieString;
	}

	private synchronized void parseCookie(HttpURLConnection connection) {
		String cookieText = connection.getHeaderField("Set-cookie");

		if (cookieText != null) {
			int idx = cookieText.indexOf('=');
			String type = cookieText.substring(0, idx);
			cookieText = cookieText.replaceAll(type + "=", "").replaceAll(";Path=/;", "");

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

	private String doGet(URL url, boolean sendCookies) throws IOException, ServerException {
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod(this.HTTP_GET);
		if (sendCookies) {
			connection.setRequestProperty("Cookie", this.getCookies());
		}
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
			this.serializer.deserializeModel(json);
		} catch (CatanException e) {
			e.printStackTrace();
		}
	}

	/*
	 * User methods
	 */

	@Override
	public ModelUser userLogin(String username, String password) {
		DTOUserLogin data = new DTOUserLogin(username, password);

		try {
			URL url = new URL(this.getUrlPrefix() + "/user/login");
			this.doPost(url, data);
		} catch (IOException | ServerException e) {
			e.printStackTrace();
			return null;
		}

		@SuppressWarnings("deprecation")
		String cookieJson = URLDecoder.decode(this.userCookie);
		ModelUser client = (ModelUser) CatanSerializer.getInstance()
				.deserializeObject(cookieJson, ModelUser.class);

		return client;
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

	/*
	 * Games methods
	 */

	@SuppressWarnings("unchecked")
	@Override
	public Collection<DTOGame> gamesList() {
		try {
			URL url = new URL(this.getUrlPrefix() + "/games/list");
			String response = this.doGet(url, false);
			Type objectType = new TypeToken<Collection<DTOGame>>() {
			}.getType();
			return (Collection<DTOGame>) this.serializer.deserializeObject(response, objectType);
		} catch (IOException | ServerException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public DTOGame gamesCreate(
			boolean randomTiles,
			boolean randomNumbers,
			boolean randomPorts,
			String name) {
		DTOGamesCreate data = new DTOGamesCreate(randomTiles, randomNumbers, randomPorts, name);

		try {
			URL url = new URL(this.getUrlPrefix() + "/games/create");
			String response = this.doPost(url, data);
			Type objectType = new TypeToken<DTOGame>() {
			}.getType();
			return (DTOGame) this.serializer.deserializeObject(response, objectType);
		} catch (IOException | ServerException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean gamesJoin(int gameId, CatanColor color) {
		DTOGamesJoin data = new DTOGamesJoin(gameId, color);

		try {
			URL url = new URL(this.getUrlPrefix() + "/games/join");
			this.doPost(url, data);
		} catch (IOException | ServerException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean gamesSave(int gameId, String fileName) {
		DTOGamesSave data = new DTOGamesSave(gameId, fileName);

		try {
			URL url = new URL(this.getUrlPrefix() + "/games/save");
			this.doPost(url, data);
		} catch (IOException | ServerException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean gamesLoad(String name) {
		DTOGamesLoad data = new DTOGamesLoad(name);

		try {
			URL url = new URL(this.getUrlPrefix() + "/games/load");
			this.doPost(url, data);
		} catch (IOException | ServerException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/*
	 * Game methods
	 */

	@Override
	public void gameModel(int version) throws IOException, ServerException {
		URL url = new URL(this.getUrlPrefix() + "/game/model?version=" + version);
		String response = this.doGet(url, true);
		this.deserializeResponse(response);
	}

	public String gameModel() throws ServerException, IOException {
		URL url = new URL(this.getUrlPrefix() + "/game/model");
		String result = this.doGet(url, true);
		this.deserializeResponse(result);
		return result;
	}

	@Override
	public void gameReset() {
		try {
			URL url = new URL(this.getUrlPrefix() + "/game/reset");
			String response = this.doGet(url, true);
			this.deserializeResponse(response);
		} catch (IOException | ServerException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void gameCommands(String commandList) {
		try {
			URL url = new URL(this.getUrlPrefix() + "/game/commands");
			String response = this.doPost(url, commandList);
			this.deserializeResponse(response);
		} catch (IOException | ServerException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String gameCommands() {
		try {
			URL url = new URL(this.getUrlPrefix() + "/game/commands");
			String response = this.doGet(url, true);
			return response;
		} catch (IOException | ServerException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean gameAddAI(AIType AItype) {
		DTOAIType type = new DTOAIType(AItype);

		try {
			URL url = new URL(this.getUrlPrefix() + "/game/addAI");
			this.doPost(url, type);
			return true;
		} catch (IOException | ServerException e) {
			e.printStackTrace();
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<AIType> gameListAI() {
		try {
			URL url = new URL(this.getUrlPrefix() + "/game/listAI");
			String response = this.doGet(url, true);
			Type objectType = new TypeToken<Collection<AIType>>() {
			}.getType();
			return (Collection<AIType>) this.serializer.deserializeObject(response, objectType);
		} catch (IOException | ServerException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * Moves methods
	 */

	@Override
	public boolean movesSendChat(PlayerNumber playerIndex, String content) {
		DTOMovesSendChat data = new DTOMovesSendChat(playerIndex, content);

		try {
			URL url = new URL(this.getUrlPrefix() + "/moves/sendChat");
			String response = this.doPost(url, data);
			this.deserializeResponse(response);
			return true;
		} catch (IOException | ServerException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean movesRollNumber(PlayerNumber playerIndex, int number) {
		DTOMovesRollNumber data = new DTOMovesRollNumber(playerIndex, number);

		try {
			URL url = new URL(this.getUrlPrefix() + "/moves/rollNumber");
			String response = this.doPost(url, data);
			this.deserializeResponse(response);
			return true;
		} catch (IOException | ServerException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean movesRobPlayer(
			PlayerNumber playerIndex,
			PlayerNumber victimIndex,
			HexLocation location) {
		int x = location.getX();
		int y = location.getY();
		DTOMovesRobPlayer data = new DTOMovesRobPlayer(playerIndex, victimIndex, x, y);

		try {
			URL url = new URL(this.getUrlPrefix() + "/moves/robPlayer");
			String response = this.doPost(url, data);
			this.deserializeResponse(response);
			return true;
		} catch (IOException | ServerException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean movesFinishTurn(PlayerNumber playerIndex) {
		DTOMovesFinishTurn data = new DTOMovesFinishTurn(playerIndex);

		try {
			URL url = new URL(this.getUrlPrefix() + "/moves/finishTurn");
			String response = this.doPost(url, data);
			this.deserializeResponse(response);
			return true;
		} catch (IOException | ServerException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean movesBuyDevCard(PlayerNumber playerIndex) {
		DTOMovesBuyDevCard data = new DTOMovesBuyDevCard(playerIndex);

		try {
			URL url = new URL(this.getUrlPrefix() + "/moves/finishTurn");
			String response = this.doPost(url, data);
			this.deserializeResponse(response);
			return true;
		} catch (IOException | ServerException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean movesYear_of_Plenty(
			PlayerNumber playerIndex,
			ResourceType resource1,
			ResourceType resource2) {
		DTOMovesYearOfPlenty data = new DTOMovesYearOfPlenty(playerIndex, resource1, resource2);

		try {
			URL url = new URL(this.getUrlPrefix() + "/moves/Year_of_Plenty");
			String response = this.doPost(url, data);
			this.deserializeResponse(response);
			return true;
		} catch (IOException | ServerException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean movesRoad_Building(PlayerNumber playerIndex, EdgeLocation edge1,
			EdgeLocation edge2) {

		HexLocation hex1 = edge1.getHexLoc();
		int x1 = hex1.getX();
		int y1 = hex1.getY();
		EdgeDirection dir1 = edge1.getDir();
		HexLocation hex2 = edge2.getHexLoc();
		int x2 = hex2.getX();
		int y2 = hex2.getY();
		EdgeDirection dir2 = edge2.getDir();
		DTOMovesRoadBuilding data = new DTOMovesRoadBuilding(playerIndex, x1, y1, dir1, x2, y2,
				dir2);

		try {
			URL url = new URL(this.getUrlPrefix() + "/moves/Road_Building");
			String response = this.doPost(url, data);
			this.deserializeResponse(response);
			return true;
		} catch (IOException | ServerException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean movesSoldier(PlayerNumber playerIndex, PlayerNumber victimIndex,
			HexLocation location) {

		int x = location.getX();
		int y = location.getY();

		DTOMovesSoldier data = new DTOMovesSoldier(playerIndex, victimIndex, x, y);

		try {
			URL url = new URL(this.getUrlPrefix() + "/moves/Soldier");
			String response = this.doPost(url, data);
			this.deserializeResponse(response);
			return true;
		} catch (IOException | ServerException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean movesMonopoly(ResourceType resource, PlayerNumber playerIndex) {
		DTOMovesMonopoly data = new DTOMovesMonopoly(resource, playerIndex);

		try {
			URL url = new URL(this.getUrlPrefix() + "/moves/Monopoly");
			String response = this.doPost(url, data);
			this.deserializeResponse(response);
			return true;
		} catch (IOException | ServerException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean movesMonument(PlayerNumber playerIndex) {
		DTOMovesMonument data = new DTOMovesMonument(playerIndex);

		try {
			URL url = new URL(this.getUrlPrefix() + "/moves/Monument");
			String response = this.doPost(url, data);
			this.deserializeResponse(response);
			return true;
		} catch (IOException | ServerException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean movesBuildRoad(PlayerNumber playerIndex, EdgeLocation location, boolean free) {

		HexLocation hex = location.getHexLoc();
		int x = hex.getX();
		int y = hex.getY();
		EdgeDirection dir = location.getDir();

		DTOMovesBuildRoad data = new DTOMovesBuildRoad(playerIndex, x, y, dir, free);

		try {
			URL url = new URL(this.getUrlPrefix() + "/moves/buildRoad");
			String response = this.doPost(url, data);
			this.deserializeResponse(response);
			return true;
		} catch (IOException | ServerException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean movesBuildSettlement(PlayerNumber playerIndex, VertexLocation location,
			boolean free) {

		HexLocation hex = location.getHexLoc();
		int x = hex.getX();
		int y = hex.getY();
		VertexDirection dir = location.getDir();

		DTOMovesBuildSettlement data = new DTOMovesBuildSettlement(playerIndex, x, y, dir, free);

		try {
			URL url = new URL(this.getUrlPrefix() + "/moves/buildSettlement");
			String response = this.doPost(url, data);
			this.deserializeResponse(response);
			return true;
		} catch (IOException | ServerException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean movesBuildCity(PlayerNumber playerIndex, VertexLocation location) {

		HexLocation hex = location.getHexLoc();
		int x = hex.getX();
		int y = hex.getY();
		VertexDirection dir = location.getDir();

		DTOMovesBuildCity data = new DTOMovesBuildCity(playerIndex, x, y, dir);

		try {
			URL url = new URL(this.getUrlPrefix() + "/moves/buildCity");
			String response = this.doPost(url, data);
			this.deserializeResponse(response);
			return true;
		} catch (IOException | ServerException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean movesOfferTrade(ResourceInvoice invoice) {
		DTOMovesOfferTrade data = new DTOMovesOfferTrade(invoice);

		try {
			URL url = new URL(this.getUrlPrefix() + "/moves/offerTrade");
			String response = this.doPost(url, data);
			this.deserializeResponse(response);
			return true;
		} catch (IOException | ServerException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean movesAcceptTrade(PlayerNumber playerIndex, boolean willAccept) {
		DTOMovesAcceptTrade data = new DTOMovesAcceptTrade(playerIndex, willAccept);

		try {
			URL url = new URL(this.getUrlPrefix() + "/moves/acceptTrade");
			String response = this.doPost(url, data);
			this.deserializeResponse(response);
			return true;
		} catch (IOException | ServerException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean movesMaritimeTrade(
			PlayerNumber playerIndex,
			int ratio,
			ResourceType inputResource,
			ResourceType outputResource) {
		DTOMovesMaritimeTrade data = new DTOMovesMaritimeTrade(playerIndex, ratio, inputResource,
				outputResource);

		try {
			URL url = new URL(this.getUrlPrefix() + "/moves/maritimeTrade");
			String response = this.doPost(url, data);
			this.deserializeResponse(response);
			return true;
		} catch (IOException | ServerException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean movesDiscardCards(
			PlayerNumber playerIndex,
			int brick,
			int ore,
			int sheep,
			int wheat,
			int wood) {
		DTOMovesDiscardCards data = new DTOMovesDiscardCards(playerIndex, brick, ore, sheep, wheat,
				wood);

		try {
			URL url = new URL(this.getUrlPrefix() + "/moves/discardCards");
			String response = this.doPost(url, data);
			this.deserializeResponse(response);
			return true;
		} catch (IOException | ServerException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean utilChangeLogLevel(String logLevel) {
		DTOLogLevel data = new DTOLogLevel(logLevel);

		try {
			URL url = new URL(this.getUrlPrefix() + "/moves/discardCards");
			this.doPost(url, data);
			return true;
		} catch (IOException | ServerException e) {
			e.printStackTrace();
		}

		return false;
	}

	/*
	 * Getters and setters
	 */

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
}
