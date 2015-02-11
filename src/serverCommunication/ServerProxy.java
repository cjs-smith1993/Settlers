package serverCommunication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collection;

import shared.definitions.*;
import shared.locations.*;
import clientBackend.CatanSerializer;
import clientBackend.dataTransportObjects.*;
import clientBackend.model.CatanException;
import clientBackend.model.Game;

/**
 * Implements the server interface and acts as a proxy server so the client does
 * not have to worry about communication details
 */
public class ServerProxy implements ServerInterface {
	private CatanSerializer serializer;
	private String userCookie;
	private String gameCookie;
	private String hostname;
	private int port;

	private final String HTTP_GET = "GET";
	private final String HTTP_POST = "POST";
	private final String consoleNote = "SERVER PROXY NOTE: ";

	public ServerProxy(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
		this.serializer = CatanSerializer.getInstance();
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
			String cookieJson = URLDecoder.decode(cookieText);

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

	public static void main(String args[]) throws ServerException {
		ServerProxy proxy = new ServerProxy("localhost", 8081);
		proxy.userLogin("Brooke", "brooke");
		proxy.gamesJoin(0, CatanColor.BLUE);
	}

	@Override
	public boolean userLogin(String username, String password) throws ServerException {
		DTOUserLogin data = new DTOUserLogin(username, password);

		try {
			URL url = new URL(this.getUrlPrefix() + "/user/login");
			String response = this.doPost(url, data);
			//System.out.println(response);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public boolean userRegister(String username, String password) throws ServerException {
		DTOUserRegister data = new DTOUserRegister(username, password);

		try {
			URL url = new URL(this.getUrlPrefix() + "/user/register");
			String response = this.doPost(url, data);
			//System.out.println(this.consoleNote + response);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public Collection<Game> gamesList() {

		return null;
	}

	@Override
	public void gamesCreate(boolean randomTiles, boolean randomNumbers, boolean randomPorts,
			String name) throws ServerException {

	}

	@Override
	public boolean gamesJoin(int gameId, CatanColor color) throws ServerException {
		DTOGamesJoin data = new DTOGamesJoin(gameId, color);

		try {
			URL url = new URL(this.getUrlPrefix() + "/games/join");
			String response = this.doPost(url, data);
			//System.out.println(this.consoleNote + response);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean gamesSave(int gameId, String name) {

		return false;
	}

	@Override
	public boolean gamesLoad(String name) {
		return false;
	}

	@Override
	public void gameModel(int version) throws ServerException {
		DTOGameModel data = new DTOGameModel(version);

		String response = "";

		try {
			URL url = new URL(this.getUrlPrefix() + "/game/model");
			response = this.doPost(url, data);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			CatanSerializer.getInstance().deserializeModel(response);
		} catch (CatanException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void gameReset() throws ServerException {
		try {
			URL url = new URL(this.getUrlPrefix() + "/game/reset");
			String response = this.doGet(url);
			//System.out.println(this.consoleNote + response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void gameCommands() throws ServerException {
		try {
			URL url = new URL(this.getUrlPrefix() + "/game/commands");
			String response = this.doGet(url);
			//System.out.println(this.consoleNote + response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void gameCommands(String commandList) throws ServerException {
		// NOTE: DO NOTHING, FOR NOW.
	}

	@Override
	public void gameAddAI(String AIType) throws ServerException {
		// NOTE: DO NOTHING.
	}

	@Override
	public void gameListAI() throws ServerException {
		// NOTE: DO NOTHING.
	}

	@Override
	public void movesSendChat(int playerIndex, String content) throws ServerException {
		DTOMovesSendChat data = new DTOMovesSendChat(playerIndex, content);

		try {
			URL url = new URL(this.getUrlPrefix() + "/moves/sendChat");
			String response = this.doPost(url, data);
			//System.out.println(this.consoleNote + response);
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
			CatanSerializer.getInstance().deserializeModel(response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void movesRobPlayer(int playerIndex, int victimIndex,
			HexLocation location) throws ServerException {
		DTOMovesRobPlayer data = new DTOMovesRobPlayer(playerIndex, victimIndex,
				Integer.toString(location.getX()), Integer.toString(location.getY()));

		try {
			URL url = new URL(this.getUrlPrefix() + "/moves/robPlayer");
			String response = this.doPost(url, data);
			//System.out.println(this.consoleNote + response);
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
			//System.out.println(this.consoleNote + response);
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
			//System.out.println(this.consoleNote + response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void movesYear_of_Plenty(int playerIndex, ResourceType resource1,
			ResourceType resource2) throws ServerException {
		DTOMovesYearOfPlenty data = new DTOMovesYearOfPlenty(playerIndex, resource1, resource2);

		try {
			URL url = new URL(this.getUrlPrefix() + "/moves/Year_of_Plenty");
			String response = this.doPost(url, data);
			//System.out.println(this.consoleNote + response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void movesRoad_Building(int playerIndex, EdgeLocation spot1, EdgeLocation spot2)
			throws ServerException {
		DTOMovesRoadBuilding data = new DTOMovesRoadBuilding(playerIndex, spot1.getHexLoc().getX(),
				spot1.getHexLoc().getY(), spot1.getDir(), spot2.getHexLoc().getX(), spot2
						.getHexLoc().getY(), spot2.getDir());

		try {
			URL url = new URL(this.getUrlPrefix() + "/moves/Road_Building");
			String response = this.doPost(url, data);
			//System.out.println(this.consoleNote + response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void movesSoldier(int playerIndex, int victimIndex,
			HexLocation location) throws ServerException {
		DTOMovesSoldier data = new DTOMovesSoldier(playerIndex, victimIndex,
				Integer.toString(location.getX()), Integer.toString(location.getY()));

		try {
			URL url = new URL(this.getUrlPrefix() + "/moves/Soldier");
			String response = this.doPost(url, data);
			//System.out.println(this.consoleNote + response);
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
			//System.out.println(this.consoleNote + response);
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
			//System.out.println(this.consoleNote + response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void movesBuildRoad(int playerIndex, EdgeLocation location, boolean free)
			throws ServerException {
		DTOMovesBuildRoad data = new DTOMovesBuildRoad(playerIndex, location.getHexLoc().getX(),
				location.getHexLoc().getY(), location.getDir(), free);

		try {
			URL url = new URL(this.getUrlPrefix() + "/moves/buildRoad");
			String response = this.doPost(url, data);
			//System.out.println(this.consoleNote + response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void movesBuildSettlement(int playerIndex, VertexLocation location, boolean free)
			throws ServerException {
		DTOMovesBuildSettlement data = new DTOMovesBuildSettlement(playerIndex, location
				.getHexLoc().getX(), location.getHexLoc().getY(), location.getDir(), free);

		try {
			URL url = new URL(this.getUrlPrefix() + "/moves/buildSettlement");
			String response = this.doPost(url, data);
			//System.out.println(this.consoleNote + response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void movesBuildCity(int playerIndex, VertexLocation location)
			throws ServerException {
		DTOMovesBuildCity data = new DTOMovesBuildCity(playerIndex, location.getHexLoc().getX(),
				location.getHexLoc().getY(), location.getDir());

		try {
			URL url = new URL(this.getUrlPrefix() + "/moves/buildCity");
			String response = this.doPost(url, data);
			//System.out.println(this.consoleNote + response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void movesOfferTrade(int playerIndex, int brick, int ore, int sheep, int wheat,
			int wood,
			int receiver) throws ServerException {
		DTOMovesOfferTrade data = new DTOMovesOfferTrade(playerIndex, brick, ore, sheep, wheat,
				wood, receiver);

		try {
			URL url = new URL(this.getUrlPrefix() + "/moves/offerTrade");
			String response = this.doPost(url, data);
			//System.out.println(this.consoleNote + response);
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
			//System.out.println(this.consoleNote + response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void movesMaritimeTrade(int playerIndex, int ratio, ResourceType inputResource,
			ResourceType outputResource) throws ServerException {
		DTOMovesMaritimeTrade data = new DTOMovesMaritimeTrade(playerIndex, ratio, inputResource,
				outputResource);

		try {
			URL url = new URL(this.getUrlPrefix() + "/moves/maritimeTrade");
			String response = this.doPost(url, data);
			//System.out.println(this.consoleNote + response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void movesDiscardCards(int playerIndex, int brick, int ore, int sheep,
			int wheat, int wood)
			throws ServerException {
		DTOMovesDiscardCards data = new DTOMovesDiscardCards(playerIndex, brick, ore, sheep, wheat,
				wood);

		try {
			URL url = new URL(this.getUrlPrefix() + "/moves/discardCards");
			String response = this.doPost(url, data);
			//System.out.println(this.consoleNote + response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean utilChangeLogLevel(String logLevel) throws ServerException {
		// NOTE: DO NOTHING.
		return false;
	}
}
