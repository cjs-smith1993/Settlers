package omegaTester;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.util.Scanner;

import client.backend.CatanSerializer;
import client.serverCommunication.ServerException;
import client.serverCommunication.ServerProxy;
import shared.definitions.CatanColor;
import shared.definitions.PlayerNumber;
import shared.definitions.ResourceType;
import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;
import shared.model.ResourceInvoice;
import shared.model.User;

/**
 * Command line tester.
 * 
 * @author Kyle
 *
 */
public class OmegaTester implements Runnable {
	private Scanner scanner = new Scanner(System.in);
	private ServerProxy proxy;
	private CatanSerializer serializer = CatanSerializer.getInstance();
	private boolean isPrintOn = false;

	public OmegaTester(ServerProxy proxy) {
		this.proxy = proxy;
	}

	@Override
	public void run() {
		this.startCL();
	}

	public void startCL() {
		// Display help() on startup.
		this.help();
		while (true) {
			this.printDiv();
			this.println("Type Desired Method");
			this.println("Format: \"method.name\"");
			String data = this.scanner.nextLine();

			data = data.toLowerCase();

			// Place all proxy calls in this block.
			synchronized (this.proxy) {
				switch (data) {
				case "clear":
					this.clear();
					break;
				case "help":
					this.help();
					break;
				case "model":
					this.getModel();
					break;
				case "moves.chat":
					this.chat();
					break;
				case "user.login":
					this.login();
					this.getModel();
					break;
				case "games.list":
					this.gamesList();
					break;
				case "moves.roll":
					this.movesRoll();
					this.getModel();
					break;
				case "moves.rob":
					this.movesRob();
					this.getModel();
					break;
				case "moves.finish":
					this.movesFinish();
					this.getModel();
					break;
				case "moves.buydev":
					this.movesBuyDev();
					this.getModel();
					break;
				case "moves.year":
					this.movesYearOfPlenty();
					this.getModel();
					break;
				case "moves.roadbuilding":
					this.movesRoadBuilding();
					this.getModel();
					break;
				case "moves.soldier":
					this.movesSoldier();
					this.getModel();
					break;
				case "moves.monopoly":
					this.movesMonopoly();
					this.getModel();
					break;
				case "moves.monument":
					this.movesMonument();
					this.getModel();
					break;
				case "moves.buildroad":
					this.movesBuildRoad();
					this.getModel();
					break;
				case "moves.buildsettlement":
					this.movesBuildSettlement();
					this.getModel();
					break;
				case "moves.buildcity":
					this.movesBuildCity();
					this.getModel();
					break;
				case "moves.offertrade":
					this.movesOfferTrade();
					this.getModel();
					break;
				case "moves.accepttrade":
					this.movesAcceptTrade();
					this.getModel();
					break;
				case "moves.maritimetrade":
					this.movesMaritimeTrade();
					this.getModel();
					break;
				case "moves.discardcards":
					this.movesDiscardCards();
					this.getModel();
					break;
				case "print.off":
					this.printOff();
					break;
				case "print.on":
					this.printOn();
					break;
				default:
					this.println("Method code not recognized, please try again");
				}
			}
		}
	}

	private boolean isLoggedIn() {
		boolean isLoggedIn = !this.proxy.getCookies().isEmpty();

		if (!isLoggedIn) {
			this.println("WARNING: YOUR USER NOT LOGGED IN!");
		}

		return isLoggedIn;
	}

	private void clear() {
		for (int i = 0; i < 40; i++) {
			this.println("\n");
		}
	}

	private void help() {
		this.printDiv();
		this.println("Welcome to Help. Here are the available methods:");
		this.println("-----");
		this.println("NOTES");
		this.println("-----");
		this.println("PRINT: To turn print off use: \"print.off\". Turn on is \"print.on\"");
		this.println("PLAYERNUMBER: Enter as a number, -1 to 3.");
		this.println("RESOURCES: Enter resources as \"wood\", \"sheep\", etc...");
		this.println("DIRECTION: Enter directions as \"NE\", \"NW\", etc...");
		this.println("-----");
		this.println("METHOD FORMAT: \"method.name\" (params)");
		this.println("-----");
		this.println("\t\"help\" (none)");
		this.println("\t\"clear\" (none) --> Clears console space.");
		this.println("\t\"model\" (none) --> Retrieves model. Pastes to your clipboard.");
		this.println("\t\"games.list\" (none)");
		this.println("\t\"user.login\" (username, password)");
		this.println("\t\"moves.roll\" (rolledNumber)");
		this.println("\t\"moves.rob\" (playerIndex, victimIndex, xLocation, yLocation)");
		this.println("\t\"moves.finish\" (playerIndex)");
		this.println("\t\"moves.buyDev\" (playerIndex)");
		this.println("\t\"moves.year\" (playerIndex, resource1, resource2)");
		this.println("\t\"moves.roadBuilding\" (playerIndex, x1, y1, direction1, x2, y2, direction2)");
		this.println("\t\"moves.soldier\" (playerIndex, victimIndex, locationX, locationY)");
		this.println("\t\"moves.monopoly\" (resource, playerIndex)");
		this.println("\t\"moves.monument\" (playerIndex)");
		this.println("\t\"moves.buildRoad\" (playerIndex, roadX, roadY, direction, isFree)");
		this.println("\t\"moves.buildSettlement\" (playerIndex, settlmentX, settlementY, direction, isFree)");
		this.println("\t\"moves.buildCity\" (playerIndex, x, y, direction)");
		this.println("\t\"moves.offerTrade\" (qtyBrick, qtyWood, qtyWheat, qtyOre, qtySheep, sourcePlayerIndex, destinationPlayerIndex)");
		this.println("\t\"moves.acceptTrade\" (playerIndex, willAcceptTrade)");
		this.println("\t\"moves.maritimeTrade\" (playerIndex, ratio, inputResource, outputResource)");
		this.println("\t\"moves.discardCards\" (playerIndex, qtyBrick, qtyOre, qtySheep, qtyWheat, qtyWood)");
		this.printDiv();
	}

	/*
	 * PROXY METHODS
	 */
	private void getModel() {
		if (this.isLoggedIn()) {
			String data = "";

			try {
				data = this.proxy.gameModel();
			} catch (ServerException | IOException e) {
				e.printStackTrace();
			}

			this.ctcb(data);
		}
		else {
			this.println("USER NOT LOGGED IN. CANNOT FETCH MODEL.");
		}
	}

	private void chat() {
		if (this.isLoggedIn()) {
			this.println("Please enter: playerIndex.");
			PlayerNumber playerIndex = this.getPlayerNumber(this.scanner.nextInt());

			this.println("Please enter: message");
			String message = this.scanner.nextLine();

			boolean isSuccess = this.proxy.movesSendChat(playerIndex, message);

			if (isSuccess) {
				this.println("SUCCESS: Chat away!");
			}
			else {
				this.println("ERROR: Chat failed.");
			}
		}
	}

	private void login() {
		if (!this.isLoggedIn()) {
			this.println("Please enter: \"Username\" \"Password\"");

			String username = this.scanner.next();
			String password = this.scanner.next();

			User user = this.proxy.userLogin(username, password);

			if (!user.getName().isEmpty()) {
				this.println("User successfully logged in!");
				this.println("Please enter: \"GameID\" \"DesiredColor\"");

				int gameID = this.scanner.nextInt();
				CatanColor color = this.getCatanColor(this.scanner.next());

				if (gameID < 0 || color == null) {
					this.println("ERROR: Bad gameID or color. Please try again.");
				}
				else {
					if (this.proxy.gamesJoin(gameID, color)) {
						this.println("SUCCESS: \nJoined game " + gameID + "\nColor: " + color);
					}
				}
			}
			else {
				this.println("ERROR: Failed to login. Please begin again.");
			}
		}
		else {
			this.println("ERROR: User is already logged in.");
		}
	}

	private void gamesList() {
		Object games = this.proxy.gamesList();
		String json = this.serializer.serializeObject(games);
		this.ctcb(json);
	}

	private void movesRoll() {
		if (this.isLoggedIn()) {
			this.println("Please enter: PlayerNumber (as -1 to 3) and desired roll.");

			int playerIndex = this.scanner.nextInt();
			int rollNumber = this.scanner.nextInt();

			PlayerNumber playerNumber = PlayerNumber.BANK;
			playerNumber = this.getPlayerNumber(playerIndex);

			boolean isSuccess = this.proxy.movesRollNumber(playerNumber, rollNumber);

			if (isSuccess) {
				this.println("SUCCESS: Rolled submitted. Getting model...");
			}
			else {
				this.println("ERROR: Roll was not successfull. Please check conditions.");
			}
		}
	}

	private void movesRob() {
		if (this.isLoggedIn()) {
			this.println("Please enter: playerIndex, victimIndex, locationX, locationY (without commas).");

			int playerIndex = this.scanner.nextInt();
			int victimIndex = this.scanner.nextInt();
			int x = this.scanner.nextInt();
			int y = this.scanner.nextInt();

			HexLocation location = new HexLocation(x, y);

			boolean isSuccess = this.proxy.movesRobPlayer(this.getPlayerNumber(playerIndex),
					this.getPlayerNumber(victimIndex), location);

			if (isSuccess) {
				this.println("SUCCESS: Player looted as they rightly deserved.");
			}
			else {
				this.println("ERROR: Crusade halted.");
			}
		}
	}

	private void movesFinish() {
		if (this.isLoggedIn()) {
			this.println("Please enter: playerIndex (-1 to 3).");

			int playerIndex = this.scanner.nextInt();

			boolean isSuccess = this.proxy.movesFinishTurn(this.getPlayerNumber(playerIndex));

			if (isSuccess) {
				this.println("SUCCESS: Turned ended successfully.");
			}
			else {
				this.println("ERROR: Problem ending turn.");
			}
		}
	}

	private void movesBuyDev() {
		if (this.isLoggedIn()) {
			this.println("Please enter: playerIndex (-1 to 3).");

			int playerIndex = this.scanner.nextInt();

			boolean isSuccess = this.proxy.movesBuyDevCard(this.getPlayerNumber(playerIndex));

			if (isSuccess) {
				this.println("SUCCESS: Purchase succeeded.");
			}
			else {
				this.println("ERROR: Problem with dev card purchase.");
			}
		}
	}

	private void movesYearOfPlenty() {
		if (this.isLoggedIn()) {
			this.println("Please enter: playerIndex (-1 to 3), resource1, resource2.");

			int playerIndex = this.scanner.nextInt();
			String resource1 = this.scanner.next();
			String resource2 = this.scanner.next();

			boolean isSuccess = this.proxy.movesYear_of_Plenty(this.getPlayerNumber(playerIndex),
					this.getResourceType(resource1), this.getResourceType(resource2));

			if (isSuccess) {
				this.println("SUCCESS: The year is plentous.");
			}
			else {
				this.println("ERROR: A drought has occurred. Welcome to Utahr.");
			}
		}
	}

	private void movesRoadBuilding() {
		if (this.isLoggedIn()) {
			this.println("Please enter: playerIndex, x1, y1, direction1, x2, y2, direction2");

			PlayerNumber playerNumber = this.getPlayerNumber(this.scanner.nextInt());
			int x1 = this.scanner.nextInt();
			int y1 = this.scanner.nextInt();
			String direction1 = this.scanner.next();

			int x2 = this.scanner.nextInt();
			int y2 = this.scanner.nextInt();
			String direction2 = this.scanner.next();

			HexLocation hex1 = new HexLocation(x1, y1);
			EdgeLocation loc1 = new EdgeLocation(hex1, this.getEdgeDirection(direction1));

			HexLocation hex2 = new HexLocation(x2, y2);
			EdgeLocation loc2 = new EdgeLocation(hex2, this.getEdgeDirection(direction2));

			boolean isSuccess = this.proxy.movesRoad_Building(playerNumber, loc1, loc2);

			if (isSuccess) {
				this.println("SUCCESS: Road building succeeded.");
			}
			else {
				this.println("ERROR: Road building failed.");
			}
		}
	}

	private void movesSoldier() {
		if (this.isLoggedIn()) {
			this.println("Please enter: playerIndex (-1 to 3), victimIndex (-1 to 3), x, y");

			PlayerNumber playerNumber = this.getPlayerNumber(this.scanner.nextInt());
			PlayerNumber victimNumber = this.getPlayerNumber(this.scanner.nextInt());
			HexLocation location = new HexLocation(this.scanner.nextInt(), this.scanner.nextInt());

			boolean isSuccess = this.proxy.movesSoldier(playerNumber, victimNumber, location);

			if (isSuccess) {
				this.println("SUCCESS: Soldier moved! That lazy chum..");
			}
			else {
				this.println("ERROR: Soldier won't budge.");
			}
		}
	}

	private void movesMonopoly() {
		if (this.isLoggedIn()) {
			this.println("Please enter: playerIndex (-1 to 3), resourceType");

			ResourceType resource = this.getResourceType(this.scanner.next());
			PlayerNumber playerNumber = this.getPlayerNumber(this.scanner.nextInt());

			boolean isSuccess = this.proxy.movesMonopoly(resource, playerNumber);

			if (isSuccess) {
				this.println("SUCCESS: Your friends have been pillaged.");
			}
			else {
				this.println("ERROR: They escaped!");
			}
		}
	}

	private void movesMonument() {
		if (this.isLoggedIn()) {
			this.println("Please enter: playerIndex (-1 to 3)");

			PlayerNumber playerNumber = this.getPlayerNumber(this.scanner.nextInt());

			boolean isSuccess = this.proxy.movesMonument(playerNumber);

			if (isSuccess) {
				this.println("SUCCESS: You got brownie points...and victory points.");
			}
			else {
				this.println("ERROR: Apparently monuments are heavy...and can't be moved.");
			}
		}
	}

	private void movesBuildRoad() {
		if (this.isLoggedIn()) {
			this.println("Please enter: playerIndex, x, y, direction, isFree");

			PlayerNumber playerNumber = this.getPlayerNumber(this.scanner.nextInt());
			int x = this.scanner.nextInt();
			int y = this.scanner.nextInt();
			EdgeDirection direction = this.getEdgeDirection(this.scanner.next());
			boolean isFree = this.scanner.nextBoolean();

			HexLocation hexLoc = new HexLocation(x, y);
			EdgeLocation location = new EdgeLocation(hexLoc, direction);

			boolean isSuccess = this.proxy.movesBuildRoad(playerNumber, location, isFree);

			if (isSuccess) {
				this.println("SUCCESS: Built a road.");
			}
			else {
				this.println("ERROR: Failed to build a road.");
			}
		}
	}

	private void movesBuildSettlement() {
		if (this.isLoggedIn()) {
			this.println("Please enter: playerIndex (-1 to 3), x, y, vertexDirection, isFree");

			PlayerNumber playerNumber = this.getPlayerNumber(this.scanner.nextInt());
			int x = this.scanner.nextInt();
			int y = this.scanner.nextInt();
			VertexDirection direction = this.getVertexDirection(this.scanner.next());
			boolean isFree = this.scanner.nextBoolean();

			HexLocation hexLoc = new HexLocation(x, y);
			VertexLocation location = new VertexLocation(hexLoc, direction);

			boolean isSuccess = this.proxy.movesBuildSettlement(playerNumber, location, isFree);

			if (isSuccess) {
				this.println("SUCCESS: Built a settlement.");
			}
			else {
				this.println("ERROR: Failed to build a settlement.");
			}
		}
	}

	private void movesBuildCity() {
		if (this.isLoggedIn()) {
			this.println("Please enter: playerIndex (-1 to 3), x, y, vertexDirection.");

			PlayerNumber playerNumber = this.getPlayerNumber(this.scanner.nextInt());
			int x = this.scanner.nextInt();
			int y = this.scanner.nextInt();
			VertexDirection direction = this.getVertexDirection(this.scanner.next());

			HexLocation hexLoc = new HexLocation(x, y);
			VertexLocation location = new VertexLocation(hexLoc, direction);

			boolean isSuccess = this.proxy.movesBuildCity(playerNumber, location);

			if (isSuccess) {
				this.println("SUCCESS: Built a city.");
			}
			else {
				this.println("ERROR: Failed to build a city.");
			}
		}
	}

	private void movesOfferTrade() {
		if (this.isLoggedIn()) {
			this.println("Please enter: qtyBrick, qtyWood, qtyWheat, qtyOre, qtySheep, sourcePlayerNumber (-1 to 3), destinationPlayerNumber (-1 to 3).");

			int brick = this.scanner.nextInt();
			int wood = this.scanner.nextInt();
			int wheat = this.scanner.nextInt();
			int ore = this.scanner.nextInt();
			int sheep = this.scanner.nextInt();
			PlayerNumber sourcePlayer = this.getPlayerNumber(this.scanner.nextInt());
			PlayerNumber destinationPlayer = this.getPlayerNumber(this.scanner.nextInt());

			ResourceInvoice invoice = new ResourceInvoice(sourcePlayer, destinationPlayer);
			invoice.setBrick(brick);
			invoice.setWood(wood);
			invoice.setWheat(wheat);
			invoice.setOre(ore);
			invoice.setSheep(sheep);

			boolean isSuccess = this.proxy.movesOfferTrade(invoice);

			if (isSuccess) {
				this.println("SUCCESS: Trade offered successfully.");
			}
			else {
				this.println("ERROR: Trade failed.");
			}
		}
	}

	private void movesAcceptTrade() {
		if (this.isLoggedIn()) {
			this.println("Please enter: playerIndex (-1 to 3), willAcceptTrade");

			PlayerNumber playerNumber = this.getPlayerNumber(this.scanner.nextInt());
			boolean willAccept = this.scanner.nextBoolean();

			boolean isSuccess = this.proxy.movesAcceptTrade(playerNumber, willAccept);

			if (isSuccess) {
				this.println("SUCCESS: Trade accepted successfully.");
			}
			else {
				this.println("ERROR: Trade acception failed. Check conditions.");
			}
		}
	}

	private void movesMaritimeTrade() {
		if (this.isLoggedIn()) {
			this.println("Please enter: playerIndex (-1 to 3), ratio (integer), inputResource, outputResource.");

			PlayerNumber playerNumber = this.getPlayerNumber(this.scanner.nextInt());
			int ratio = this.scanner.nextInt();
			ResourceType inputResource = this.getResourceType(this.scanner.next());
			ResourceType outputResource = this.getResourceType(this.scanner.next());

			boolean isSuccess = this.proxy.movesMaritimeTrade(playerNumber, ratio, inputResource,
					outputResource);

			if (isSuccess) {
				this.println("SUCCESS: Trade accepted successfully.");
			}
			else {
				this.println("ERROR: Trade execution failed. Check conditions.");
			}
		}
	}

	private void movesDiscardCards() {
		if (this.isLoggedIn()) {
			this.println("Please enter: playerIndex (-1 to 3), qtyBrick, qtyOre, qtySheep, qtyWheat,  qtyWood.");

			PlayerNumber playerNumber = this.getPlayerNumber(this.scanner.nextInt());
			int qtyBrick = this.scanner.nextInt();
			int qtyOre = this.scanner.nextInt();
			int qtySheep = this.scanner.nextInt();
			int qtyWheat = this.scanner.nextInt();
			int qtyWood = this.scanner.nextInt();

			boolean isSuccess = this.proxy.movesDiscardCards(playerNumber, qtyBrick, qtyOre,
					qtySheep, qtyWheat, qtyWood);

			if (isSuccess) {
				this.println("SUCCESS: Discarded successfully.");
			}
			else {
				this.println("ERROR: Discard failed. Check conditions.");
			}
		}
	}

	private void printOff() {
		this.isPrintOn = false;
	}

	private void printOn() {
		this.isPrintOn = true;
	}

	/*
	 * HELPER METHODS
	 */
	private void ctcb(String message) {
		this.printDiv();
		this.println("Model saved to your clipboard.");

		if (this.isPrintOn) {
			this.println(message);
		}

		this.printDiv();

		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable transferable = new StringSelection(message);
		clipboard.setContents(transferable, null);
	}

	private void println(String message) {
		System.out.println(message);
	}

	private void printDiv() {
		this.println("-------------------------------");
	}

	private PlayerNumber getPlayerNumber(int playerIndex) {
		PlayerNumber playerNumber = PlayerNumber.BANK;

		switch (playerIndex) {
		case -1:
			playerNumber = PlayerNumber.BANK;
			break;
		case 0:
			playerNumber = PlayerNumber.ONE;
			break;
		case 1:
			playerNumber = PlayerNumber.TWO;
			break;
		case 2:
			playerNumber = PlayerNumber.THREE;
			break;
		case 3:
			playerNumber = PlayerNumber.FOUR;
			break;
		default:
			this.println("ERROR: Unrecognized player number. Did you enter -1 to 3?");
		}

		return playerNumber;
	}

	private ResourceType getResourceType(String resource) {
		switch (resource) {
		case "wood":
			return ResourceType.WOOD;
		case "brick":
			return ResourceType.BRICK;
		case "sheep":
			return ResourceType.SHEEP;
		case "wheat":
			return ResourceType.WHEAT;
		case "ore":
			return ResourceType.ORE;
		default:
			return null;
		}
	}

	private EdgeDirection getEdgeDirection(String direction) {
		switch (direction) {
		case "NW":
			return EdgeDirection.NorthWest;
		case "N":
			return EdgeDirection.North;
		case "NE":
			return EdgeDirection.NorthEast;
		case "SE":
			return EdgeDirection.SouthEast;
		case "S":
			return EdgeDirection.South;
		case "SW":
			return EdgeDirection.SouthWest;
		default:
			return null;
		}
	}

	private VertexDirection getVertexDirection(String direction) {
		switch (direction) {
		case "W":
			return VertexDirection.West;
		case "NW":
			return VertexDirection.NorthWest;
		case "NE":
			return VertexDirection.NorthEast;
		case "E":
			return VertexDirection.East;
		case "SE":
			return VertexDirection.SouthEast;
		case "SW":
			return VertexDirection.SouthWest;
		default:
			return null;
		}
	}

	private CatanColor getCatanColor(String color) {
		switch (color) {
		case "red":
			return CatanColor.RED;
		case "orange":
			return CatanColor.ORANGE;
		case "yellow":
			return CatanColor.YELLOW;
		case "blue":
			return CatanColor.BLUE;
		case "green":
			return CatanColor.GREEN;
		case "purple":
			return CatanColor.PURPLE;
		case "puce":
			return CatanColor.PUCE;
		case "white":
			return CatanColor.WHITE;
		case "brown":
			return CatanColor.BROWN;
		default:
			return null;
		}
	}
}
