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
import clientBackend.model.Game;
import clientBackend.model.ResourceCard;
import clientBackend.model.ResourceInvoice;

/**
 * Implements the server interface and acts as a proxy server so the client does
 * not have to worry about communication details
 */
public class ServerProxy implements ServerInterface {
	private String hostname;
	private int port;
	private CatanSerializer serializer;
	private String userCookie;
	private String gameCookie;

	private final String HTTP_GET = "GET";
	private final String HTTP_POST = "POST";

	public ServerProxy(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
		this.serializer = CatanSerializer.getInstance();
		System.out.println(this.serializer);
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

	private String readFrom(HttpURLConnection connection) throws IOException {
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

		return sb.toString();
	}

	private void writeTo(HttpURLConnection connection, Object data) throws IOException {

		String json = this.serializer.serializeObject(data);
		OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
		BufferedWriter bw = new BufferedWriter(osw);
		bw.write(json);
		bw.close();
	}

	private String doGet(URL url) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod(this.HTTP_GET);
		connection.connect();

		String response = this.readFrom(connection);

		return response;
	}

	private String doPost(URL url, Object data) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod(this.HTTP_POST);
		connection.setRequestProperty("Cookie", this.getCookies());

		System.out.println(this.getCookies());

		connection.setDoOutput(true);
		connection.connect();

		this.writeTo(connection, data);
		String response = this.readFrom(connection);

		return response;
	}

	public static void main(String args[]) {
		ServerProxy proxy = new ServerProxy("localhost", 8081);
		proxy.userLogin("Brooke", "brooke");
		proxy.gamesJoin(0, CatanColor.BLUE);
	}

	@Override
	public boolean userLogin(String username, String password) {
		DTOUserLogin data = new DTOUserLogin(username, password);

		try {
			URL url = new URL(this.getUrlPrefix() + "/user/login");
			String response = this.doPost(url, data);
			System.out.println(response);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public boolean userRegister(String username, String password) {
		return false;
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
	public boolean gamesJoin(int gameId, CatanColor color) {
		DTOGamesJoin data = new DTOGamesJoin(gameId, color);

		return false;
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

	}

	@Override
	public void gameReset() throws ServerException {

	}

	@Override
	public void gameCommands() throws ServerException {

	}

	@Override
	public void gameCommands(String commandList) throws ServerException {

	}

	@Override
	public void gameAddAI(String AIType) throws ServerException {

	}

	@Override
	public void gameListAI() throws ServerException {

	}

	@Override
	public void movesSendChat(PlayerNumber playerIndex, String content) throws ServerException {

	}

	@Override
	public void movesRollNumber(PlayerNumber playerIndex, int number) throws ServerException {

	}

	@Override
	public void movesRobPlayer(PlayerNumber playerIndex, PlayerNumber victimIndex,
			HexLocation location) throws ServerException {

	}

	@Override
	public void movesFinishTurn(String type, PlayerNumber playerIndex) throws ServerException {

	}

	@Override
	public void movesBuyDevCard(String type, PlayerNumber playerIndex) throws ServerException {

	}

	@Override
	public void movesYear_of_Plenty(PlayerNumber playerIndex, ResourceType resource1,
			ResourceType resource2) throws ServerException {

	}

	@Override
	public void movesRoad_Building(PlayerNumber playerIndex, EdgeLocation spot1, EdgeLocation spot2)
			throws ServerException {

	}

	@Override
	public void movesSoldier(PlayerNumber playerIndex, PlayerNumber victimIndex,
			HexLocation location) throws ServerException {

	}

	@Override
	public void movesMonopoly(ResourceType resource, PlayerNumber playerIndex)
			throws ServerException {

	}

	@Override
	public void movesMonument(String type, PlayerNumber playerIndex) throws ServerException {

	}

	@Override
	public void movesBuildRoad(PlayerNumber playerIndex, EdgeLocation location, boolean free)
			throws ServerException {

	}

	@Override
	public void movesBuildSettlement(PlayerNumber playerIndex, VertexLocation location, boolean free)
			throws ServerException {

	}

	@Override
	public void movesBuildCity(PlayerNumber playerIndex, VertexLocation location)
			throws ServerException {

	}

	@Override
	public void movesOfferTrade(PlayerNumber playerIndex, Collection<ResourceInvoice> offer,
			PlayerNumber receiver) throws ServerException {

	}

	@Override
	public void movesAcceptTrade(PlayerNumber playerIndex, boolean willAccept)
			throws ServerException {

	}

	@Override
	public void movesMaritimeTrade(PlayerNumber playerIndex, int ratio, ResourceType inputResource,
			ResourceType outputResource) throws ServerException {

	}

	@Override
	public void movesDiscardCards(PlayerNumber playerIndex, Collection<ResourceCard> discardedCards)
			throws ServerException {

	}

	@Override
	public boolean utilChangeLogLevel(String logLevel) throws ServerException {
		return false;
	}

}
