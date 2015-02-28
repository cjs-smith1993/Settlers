package serverTester;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.util.Scanner;

import clientBackend.CatanSerializer;
import clientBackend.model.ResourceInvoice;
import clientBackend.model.User;
import serverCommunication.ServerException;
import serverCommunication.ServerProxy;
import shared.definitions.CatanColor;
import shared.definitions.PlayerNumber;
import shared.definitions.ResourceType;
import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;

/**
 * Command line tester.
 * @author Kyle
 *
 */
public class CLTester implements Runnable {
	private Scanner scanner = new Scanner(System.in);
	private ServerProxy proxy = ServerProxy.getInstance();
	private CatanSerializer serializer = CatanSerializer.getInstance();
	private boolean isPrintOn = false;
	
	public CLTester(ServerProxy proxy) {
		this.proxy = proxy;
	}
	
	@Override
	public void run() {
		startCL();
	}
	
	public void startCL() {
		// Display help() on startup.
		help();
		while(true) {
			printDiv();
			println("Type Desired Method");
			println("Format: \"method.name\"");
			String data = scanner.nextLine();
			
			data = data.toLowerCase();
			
			// Place all proxy calls in this block.
			synchronized (proxy) {
				switch (data) {
				case "clear":
					clear();
					break;
				case "help":
					help();
					break;
				case "model":
					getModel();
					break;
				case "moves.chat":
					chat();
					break;
				case "user.login":
					login();
					getModel();
					break;
				case "games.list":
					gamesList();
					break;
				case "moves.roll":
					movesRoll();
					getModel();
					break;
				case "moves.rob":
					movesRob();
					getModel();
					break;
				case "moves.finish":
					movesFinish();
					getModel();
					break;
				case "moves.buydev":
					movesBuyDev();
					getModel();
					break;
				case "moves.year":
					movesYearOfPlenty();
					getModel();
					break;
				case "moves.roadbuilding":
					movesRoadBuilding();
					getModel();
					break;
				case "moves.soldier":
					movesSoldier();
					getModel();
					break;
				case "moves.monopoly":
					movesMonopoly();
					getModel();
					break;
				case "moves.monument":
					movesMonument();
					getModel();
					break;
				case "moves.buildroad":
					movesBuildRoad();
					getModel();
					break;
				case "moves.buildsettlement":
					movesBuildSettlement();
					getModel();
					break;
				case "moves.buildcity":
					movesBuildCity();
					getModel();
					break;
				case "moves.offertrade":
					movesOfferTrade();
					getModel();
					break;
				case "moves.accepttrade":
					movesAcceptTrade();
					getModel();
					break;
				case "moves.maritimetrade":
					movesMaritimeTrade();
					getModel();
					break;
				case "moves.discardcards":
					movesDiscardCards();
					getModel();
					break;
				case "print.off":
					printOff();
					break;
				case "print.on":
					printOn();
					break;
				default:
					println("Method code not recognized, please try again");
				}
			}
		}
	}

	private boolean isLoggedIn() {
		boolean isLoggedIn = !proxy.getCookies().isEmpty();
		
		if (!isLoggedIn) {
			println("WARNING: YOUR USER NOT LOGGED IN!");
		}
		
		return isLoggedIn;
	}
	
	private void clear() {
		for (int i = 0; i < 40; i++) {
			println("\n");
		}
	}
	
	private void help() {
		printDiv();
		println("Welcome to Help. Here are the available methods:");
		println("-----");
		println("NOTES");
		println("-----");
		println("PRINT: To turn print off use: \"print.off\". Turn on is \"print.on\"");
		println("PLAYERNUMBER: Enter as a number, -1 to 3.");
		println("RESOURCES: Enter resources as \"wood\", \"sheep\", etc...");
		println("DIRECTION: Enter directions as \"NE\", \"NW\", etc...");
		println("-----");
		println("METHOD FORMAT: \"method.name\" (params)");
		println("-----");
		println("\t\"help\" (none)");
		println("\t\"clear\" (none) --> Clears console space.");
		println("\t\"model\" (none) --> Retrieves model. Pastes to your clipboard.");
		println("\t\"games.list\" (none)");
		println("\t\"user.login\" (username, password)");
		println("\t\"moves.roll\" (rolledNumber)");
		println("\t\"moves.rob\" (playerIndex, victimIndex, xLocation, yLocation)");
		println("\t\"moves.finish\" (playerIndex)");
		println("\t\"moves.buyDev\" (playerIndex)");
		println("\t\"moves.year\" (playerIndex, resource1, resource2)");
		println("\t\"moves.roadBuilding\" (playerIndex, x1, y1, direction1, x2, y2, direction2)");
		println("\t\"moves.soldier\" (playerIndex, victimIndex, locationX, locationY)");
		println("\t\"moves.monopoly\" (resource, playerIndex)");
		println("\t\"moves.monument\" (playerIndex)");
		println("\t\"moves.buildRoad\" (playerIndex, roadX, roadY, direction, isFree)");
		println("\t\"moves.buildSettlement\" (playerIndex, settlmentX, settlementY, direction, isFree)");
		println("\t\"moves.buildCity\" (playerIndex, x, y, direction)");
		println("\t\"moves.offerTrade\" (qtyBrick, qtyWood, qtyWheat, qtyOre, qtySheep, sourcePlayerIndex, destinationPlayerIndex)");
		println("\t\"moves.acceptTrade\" (playerIndex, willAcceptTrade)");
		println("\t\"moves.maritimeTrade\" (playerIndex, ratio, inputResource, outputResource)");
		println("\t\"moves.discardCards\" (playerIndex, qtyBrick, qtyOre, qtySheep, qtyWheat, qtyWood)");
		printDiv();
	}
	
	/*
	 * PROXY METHODS
	 */
	private void getModel() {
		if (isLoggedIn()) {
			String data = "";
			
			try {
				data = proxy.gameModel();
			} catch (ServerException | IOException e) {
				e.printStackTrace();
			}
			
			ctcb(data);
		}
		else {
			println("USER NOT LOGGED IN. CANNOT FETCH MODEL.");
		}
	}
	
	
	private void chat() {
		if (isLoggedIn()) {
			println("Please enter: playerIndex.");
			PlayerNumber playerIndex = getPlayerNumber(scanner.nextInt());
			
			println("Please enter: message");
			String message = scanner.nextLine();
			
			boolean isSuccess = proxy.movesSendChat(playerIndex, message);
			
			if (isSuccess) {
				println("SUCCESS: Chat away!");
			}
			else {
				println("ERROR: Chat failed.");
			}
		}
	}
	
	private void login() {
		if (!isLoggedIn()) {
			println("Please enter: \"Username\" \"Password\"");
			
			String username = scanner.next();
			String password = scanner.next();
			
			User user = proxy.userLogin(username, password);
			
			if (!user.getName().isEmpty()) {
				println("User successfully logged in!");
				println("Please enter: \"GameID\" \"DesiredColor\"");
				
				int gameID = scanner.nextInt();
				CatanColor color = getCatanColor(scanner.next());
				
				if (gameID < 0 || color == null) {
					println("ERROR: Bad gameID or color. Please try again.");
				}
				else {
					if (proxy.gamesJoin(gameID, color)) {
						println("SUCCESS: \nJoined game " + gameID + "\nColor: " + color);
					}
				}
			}
			else {
				println("ERROR: Failed to login. Please begin again.");
			}
		}
		else {
			println("ERROR: User is already logged in.");
		}
	}
	
	private void gamesList() {
		Object games = proxy.gamesList();
		String json = serializer.serializeObject(games);
		ctcb(json);
	}
	
	private void movesRoll() {
		if (isLoggedIn()) {
			println("Please enter: PlayerNumber (as -1 to 3) and desired roll.");
			
			int playerIndex = scanner.nextInt();
			int rollNumber = scanner.nextInt();
			
			PlayerNumber playerNumber = PlayerNumber.BANK;
			playerNumber = getPlayerNumber(playerIndex);
			
			boolean isSuccess = proxy.movesRollNumber(playerNumber, rollNumber);
			
			if (isSuccess) {
				println("SUCCESS: Rolled submitted. Getting model...");
			}
			else {
				println("ERROR: Roll was not successfull. Please check conditions.");
			}
		}
	}
	
	private void movesRob() {
		if (isLoggedIn()) {
			println("Please enter: playerIndex, victimIndex, locationX, locationY (without commas).");
			
			int playerIndex = scanner.nextInt();
			int victimIndex = scanner.nextInt();
			int x = scanner.nextInt();
			int y = scanner.nextInt();
			
			HexLocation location = new HexLocation(x, y);
			
			boolean isSuccess = proxy.movesRobPlayer(getPlayerNumber(playerIndex), getPlayerNumber(victimIndex), location);

			if (isSuccess) {
				println("SUCCESS: Player looted as they rightly deserved.");
			}
			else {
				println("ERROR: Crusade halted.");
			}
		}
	}
	
	private void movesFinish() {
		if (isLoggedIn()) {
			println("Please enter: playerIndex (-1 to 3).");
			
			int playerIndex = scanner.nextInt();
			
			boolean isSuccess = proxy.movesFinishTurn(getPlayerNumber(playerIndex));
			
			if (isSuccess) {
				println("SUCCESS: Turned ended successfully.");
			}
			else {
				println("ERROR: Problem ending turn.");
			}
		}
	}
	
	private void movesBuyDev() {
		if (isLoggedIn()) {
			println("Please enter: playerIndex (-1 to 3).");
			
			int playerIndex = scanner.nextInt();
			
			boolean isSuccess = proxy.movesBuyDevCard(getPlayerNumber(playerIndex));
			
			if (isSuccess) {
				println("SUCCESS: Purchase succeeded.");
			}
			else {
				println("ERROR: Problem with dev card purchase.");
			}
		}
	}
	
	private void movesYearOfPlenty() {
		if (isLoggedIn()) {
			println("Please enter: playerIndex (-1 to 3), resource1, resource2.");
			
			int playerIndex = scanner.nextInt();
			String resource1 = scanner.next();
			String resource2 = scanner.next();
			
			boolean isSuccess = proxy.movesYear_of_Plenty(getPlayerNumber(playerIndex), getResourceType(resource1), getResourceType(resource2));
			
			if (isSuccess) {
				println("SUCCESS: The year is plentous.");
			}
			else {
				println("ERROR: A drought has occurred. Welcome to Utahr.");
			}
		}
	}
	
	private void movesRoadBuilding() {
		if (isLoggedIn()) {
			println("Please enter: playerIndex, x1, y1, direction1, x2, y2, direction2");
			
			PlayerNumber playerNumber = getPlayerNumber(scanner.nextInt());
			int x1 = scanner.nextInt();
			int y1 = scanner.nextInt();
			String direction1 = scanner.next();
			
			int x2 = scanner.nextInt();
			int y2 = scanner.nextInt();
			String direction2 = scanner.next();
			
			HexLocation hex1 = new HexLocation(x1, y1);
			EdgeLocation loc1 = new EdgeLocation(hex1, getEdgeDirection(direction1));
			
			HexLocation hex2 = new HexLocation(x2, y2);
			EdgeLocation loc2 = new EdgeLocation(hex2, getEdgeDirection(direction2));
			
			boolean isSuccess = proxy.movesRoad_Building(playerNumber, loc1, loc2);
			
			if (isSuccess) {
				println("SUCCESS: Road building succeeded.");
			}
			else {
				println("ERROR: Road building failed.");
			}
		}
	}
	
	private void movesSoldier() {
		if (isLoggedIn()) {
			println("Please enter: playerIndex (-1 to 3), victimIndex (-1 to 3), x, y");
			
			PlayerNumber playerNumber = getPlayerNumber(scanner.nextInt());
			PlayerNumber victimNumber = getPlayerNumber(scanner.nextInt());
			HexLocation location = new HexLocation(scanner.nextInt(), scanner.nextInt());
			
			boolean isSuccess = proxy.movesSoldier(playerNumber, victimNumber, location);
			
			if (isSuccess) {
				println("SUCCESS: Soldier moved! That lazy chum..");
			}
			else {
				println("ERROR: Soldier won't budge.");
			}
		}
	}

	private void movesMonopoly() {
		if (isLoggedIn()) {
			println("Please enter: playerIndex (-1 to 3), resourceType");
			
			ResourceType resource = getResourceType(scanner.next());
			PlayerNumber playerNumber = getPlayerNumber(scanner.nextInt());
			
			boolean isSuccess = proxy.movesMonopoly(resource, playerNumber);
			
			if (isSuccess) {
				println("SUCCESS: Your friends have been pillaged.");
			}
			else {
				println("ERROR: They escaped!");
			}
		}
	}
	
	private void movesMonument() {
		if (isLoggedIn()) {
			println("Please enter: playerIndex (-1 to 3)");
			
			PlayerNumber playerNumber = getPlayerNumber(scanner.nextInt());
			
			boolean isSuccess = proxy.movesMonument(playerNumber);
			
			if (isSuccess) {
				println("SUCCESS: You got brownie points...and victory points.");
			}
			else {
				println("ERROR: Apparently monuments are heavy...and can't be moved.");
			}
		}
	}
	
	private void movesBuildRoad() {
		if (isLoggedIn()) {
			println("Please enter: playerIndex, x, y, direction, isFree");
			
			PlayerNumber playerNumber = getPlayerNumber(scanner.nextInt());
			int x = scanner.nextInt();
			int y = scanner.nextInt();
			EdgeDirection direction = getEdgeDirection(scanner.next());
			boolean isFree = scanner.nextBoolean();
			
			HexLocation hexLoc = new HexLocation(x, y);
			EdgeLocation location = new EdgeLocation(hexLoc, direction);
			
			boolean isSuccess = proxy.movesBuildRoad(playerNumber, location, isFree);
			
			if (isSuccess) {
				println("SUCCESS: Built a road.");
			}
			else {
				println("ERROR: Failed to build a road.");
			}
		}
	}
	
	private void movesBuildSettlement() {
		if (isLoggedIn()) {
			println("Please enter: playerIndex (-1 to 3), x, y, vertexDirection, isFree");
			
			PlayerNumber playerNumber = getPlayerNumber(scanner.nextInt());
			int x = scanner.nextInt();
			int y = scanner.nextInt();
			VertexDirection direction = getVertexDirection(scanner.next());
			boolean isFree = scanner.nextBoolean();
			
			HexLocation hexLoc = new HexLocation(x, y);
			VertexLocation location = new VertexLocation(hexLoc, direction);
			
			boolean isSuccess = proxy.movesBuildSettlement(playerNumber, location, isFree);
			
			if (isSuccess) {
				println("SUCCESS: Built a settlement.");
			}
			else {
				println("ERROR: Failed to build a settlement.");
			}
		}
	}
	
	private void movesBuildCity() {
		if (isLoggedIn()) {
			println("Please enter: playerIndex (-1 to 3), x, y, vertexDirection.");
			
			PlayerNumber playerNumber = getPlayerNumber(scanner.nextInt());
			int x = scanner.nextInt();
			int y = scanner.nextInt();
			VertexDirection direction = getVertexDirection(scanner.next());
			
			HexLocation hexLoc = new HexLocation(x, y);
			VertexLocation location = new VertexLocation(hexLoc, direction);
			
			boolean isSuccess = proxy.movesBuildCity(playerNumber, location);
			
			if (isSuccess) {
				println("SUCCESS: Built a city.");
			}
			else {
				println("ERROR: Failed to build a city.");
			}
		}
	}
	
	private void movesOfferTrade() {
		if (isLoggedIn()) {
			println("Please enter: qtyBrick, qtyWood, qtyWheat, qtyOre, qtySheep, sourcePlayerNumber (-1 to 3), destinationPlayerNumber (-1 to 3).");
			
			int brick = scanner.nextInt();
			int wood = scanner.nextInt();
			int wheat = scanner.nextInt();
			int ore = scanner.nextInt();
			int sheep = scanner.nextInt();
			PlayerNumber sourcePlayer = getPlayerNumber(scanner.nextInt());
			PlayerNumber destinationPlayer = getPlayerNumber(scanner.nextInt());
			
			ResourceInvoice invoice = new ResourceInvoice(sourcePlayer, destinationPlayer);
			invoice.setBrick(brick);
			invoice.setWood(wood);
			invoice.setWheat(wheat);
			invoice.setOre(ore);
			invoice.setSheep(sheep);
			
			boolean isSuccess = proxy.movesOfferTrade(invoice);
			
			if (isSuccess) {
				println("SUCCESS: Trade offered successfully.");
			}
			else {
				println("ERROR: Trade failed.");
			}
		}
	}

	private void movesAcceptTrade() {
		if (isLoggedIn()) {
			println("Please enter: playerIndex (-1 to 3), willAcceptTrade");
			
			PlayerNumber playerNumber = getPlayerNumber(scanner.nextInt());
			boolean willAccept = scanner.nextBoolean();
			
			boolean isSuccess = proxy.movesAcceptTrade(playerNumber, willAccept);
			
			if (isSuccess) {
				println("SUCCESS: Trade accepted successfully.");
			}
			else {
				println("ERROR: Trade acception failed. Check conditions.");
			}
		}
	}

	private void movesMaritimeTrade() {
		if (isLoggedIn()) {
			println("Please enter: playerIndex (-1 to 3), ratio (integer), inputResource, outputResource.");
			
			PlayerNumber playerNumber = getPlayerNumber(scanner.nextInt());
			int ratio = scanner.nextInt();
			ResourceType inputResource = getResourceType(scanner.next());
			ResourceType outputResource = getResourceType(scanner.next());
			
			boolean isSuccess = proxy.movesMaritimeTrade(playerNumber, ratio, inputResource, outputResource);
			
			if (isSuccess) {
				println("SUCCESS: Trade accepted successfully.");
			}
			else {
				println("ERROR: Trade execution failed. Check conditions.");
			}
		}
	}
	
	private void movesDiscardCards() {
		if (isLoggedIn()) {
			println("Please enter: playerIndex (-1 to 3), qtyBrick, qtyOre, qtySheep, qtyWheat,  qtyWood.");
			
			PlayerNumber playerNumber = getPlayerNumber(scanner.nextInt());
			int qtyBrick = scanner.nextInt();
			int qtyOre = scanner.nextInt();
			int qtySheep = scanner.nextInt();
			int qtyWheat = scanner.nextInt();
			int qtyWood = scanner.nextInt();
			
			boolean isSuccess = proxy.movesDiscardCards(playerNumber, qtyBrick, qtyOre, qtySheep, qtyWheat, qtyWood);
			
			if (isSuccess) {
				println("SUCCESS: Discarded successfully.");
			}
			else {
				println("ERROR: Discard failed. Check conditions.");
			}
		}
	}
	
	private void printOff() {
		isPrintOn = false;
	}
	
	private void printOn() {
		isPrintOn = true;
	}

	/*
	 * HELPER METHODS
	 */
	private void ctcb(String message) {
		printDiv();
		println("Model saved to your clipboard.");
		
		if (isPrintOn) {
			println(message);
		}
		
		printDiv();
		
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable transferable = new StringSelection(message);
		clipboard.setContents(transferable, null);
	}
	
	private void println(String message) {
		System.out.println(message);
	}
	
	private void printDiv() {
		println("-------------------------------");
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
			println("ERROR: Unrecognized player number. Did you enter -1 to 3?");
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
