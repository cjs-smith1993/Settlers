package serverCommunication;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import serverCommunication.ServerException;
import serverCommunication.ServerProxy;
import shared.definitions.CatanColor;
import shared.definitions.ResourceType;
import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;

public class ServerProxyTest {
	private static ServerProxy proxy = new ServerProxy("localhost", 8081);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		try {
			proxy.userLogin("Pete", "pete");
			proxy.gamesJoin(0, CatanColor.RED);
		} catch (ServerException e) {
			fail("\n-----------------\nERROR: COULD NOT LOGIN TO SERVER, PRIOR TO ServerProxyTest RUNS.\n-----------------\n");
		}
	}

	@Test
	public void testMovesSendChat() throws ServerException {
		try {
			proxy.movesSendChat(3, "Hello world!");
		} catch (ServerException e) {
			fail("Server did not return STATUS CODE 200.");
		}
	}

	@Test
	public void testMovesRollNumber() {
		try {
			proxy.movesRollNumber(3, 4);
		} catch (ServerException e) {
			fail("Server did not return STATUS CODE 200.");
		}
	}

	@Test
	public void testMovesRobPlayer() {
		HexLocation location = new HexLocation(2, 2);

		try {
			proxy.movesRobPlayer(3, 0, location);
		} catch (ServerException e) {
			fail("Server did not return STATUS CODE 200.");
		}
	}

	@Test
	public void testMovesFinishTurn() {
		try {
			proxy.movesFinishTurn("Null", 3);
		} catch (ServerException e) {
			fail("Server did not return STATUS CODE 200.");
		}
	}

	@Test
	public void testMovesBuyDevCard() {
		try {
			proxy.movesBuyDevCard(null, 3);
		} catch (ServerException e) {
			fail("Server did not return STATUS CODE 200.");
		}
	}

	@Test
	public void testMovesYear_of_Plenty() {
		try {
			proxy.movesYear_of_Plenty(3, ResourceType.BRICK, ResourceType.WOOD);
		} catch (ServerException e) {
			fail("Server did not return STATUS CODE 200.");
		}
	}

	@Test
	public void testMovesRoad_Building() {
		HexLocation hexLocation1 = new HexLocation(0, 0);
		EdgeLocation location1 = new EdgeLocation(hexLocation1, EdgeDirection.NorthEast);

		HexLocation hexLocation2 = new HexLocation(1, -1);
		EdgeLocation location2 = new EdgeLocation(hexLocation2, EdgeDirection.South);

		try {
			proxy.movesRoad_Building(0, location1, location2);
		} catch (ServerException e) {
			fail("Server did not return STATUS CODE 200.");
		}
	}

	@Test
	public void testMovesSoldier() {
		HexLocation location = new HexLocation(2, 2);

		try {
			proxy.movesSoldier(3, 0, location);
		} catch (ServerException e) {
			fail("Server did not return STATUS CODE 200.");
		}
	}

	@Test
	public void testMovesMonopoly() {
		try {
			proxy.movesMonopoly(ResourceType.BRICK, 3);
		} catch (ServerException e) {
			fail("Server did not return STATUS CODE 200.");
		}
	}

	@Test
	public void testMovesMonument() {
		try {
			proxy.movesMonument(null, 0);
		} catch (ServerException e) {
			fail("Server did not return STATUS CODE 200.");
		}
	}

	@Test
	public void testMovesBuildRoad() {
		HexLocation hexLocation = new HexLocation(1, 1);
		EdgeLocation location = new EdgeLocation(hexLocation, EdgeDirection.North);

		try {
			proxy.movesBuildRoad(3, location, true);
		} catch (ServerException e) {
			fail("Server did not return STATUS CODE 200.");
		}
	}

	@Test
	public void testMovesBuildSettlement() {
		HexLocation hexLocation = new HexLocation(1, 1);
		VertexLocation location = new VertexLocation(hexLocation, VertexDirection.East);

		try {
			proxy.movesBuildSettlement(3, location, false);
		} catch (ServerException e) {
			fail("Server did not return STATUS CODE 200.");
		}
	}

	@Test
	public void testMovesBuildCity() {
		HexLocation hexLocation = new HexLocation(1, 1);
		VertexLocation location = new VertexLocation(hexLocation, VertexDirection.East);

		try {
			proxy.movesBuildCity(3, location);
		} catch (ServerException e) {
			fail("Server did not return STATUS CODE 200.");
		}
	}

	@Test
	public void testMovesOfferTrade() {
		try {
			proxy.movesOfferTrade(3, 0, 0, 0, 0, 1, 0);
		} catch (ServerException e) {
			fail("Server did not return STATUS CODE 200.");
		}
	}

	@Test
	public void testMovesAcceptTrade() {
		try {
			proxy.movesAcceptTrade(3, true);
		} catch (ServerException e) {
			fail("Server did not return STATUS CODE 200.");
		}
	}

	@Test
	public void testMovesMaritimeTrade() {
		try {
			proxy.movesMaritimeTrade(3, 3, ResourceType.BRICK, ResourceType.ORE);
		} catch (ServerException e) {
			fail("Server did not return STATUS CODE 200.");
		}
	}

	@Test
	public void testMovesDiscardCards() {
		try {
			proxy.movesDiscardCards(3, 2, 2, 2, 2, 2);
		} catch (ServerException e) {
			fail("Server did not return STATUS CODE 200.");
		}
	}
}
