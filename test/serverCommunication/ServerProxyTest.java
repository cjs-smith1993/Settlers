package serverCommunication;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.BeforeClass;
import org.junit.Test;

import clientBackend.dataTransportObjects.DTOGame;
import serverCommunication.ServerException;
import serverCommunication.ServerProxy;
import shared.definitions.AIType;
import shared.definitions.CatanColor;
import shared.definitions.ResourceType;
import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;

public class ServerProxyTest {
	private static ServerProxy proxy = ServerProxy.getInstance();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		proxy.userLogin("Pete", "pete");
		proxy.gamesJoin(0, CatanColor.RED);
	}

	@Test
	public void testgamesList() throws ServerException {
		try {
			Collection<DTOGame> games = proxy.gamesList();
			assertTrue(games.iterator().next() instanceof DTOGame);
			return;
		} catch (Exception err) {
			fail("GamesList did not return a collection of DTOGame");
			err.printStackTrace();
		}
	}

	@Test
	public void testgamesCreate() {
		try {
			String gameTitle = "TestGame";
			DTOGame game = proxy.gamesCreate(true, true, true, gameTitle);
			assertTrue(game.title.equals(gameTitle));
			return;
		} catch (Exception err) {
			fail("GamesCreate did not return the created game");
			err.printStackTrace();
		}
	}

	@Test
	public void testgameListAI() {
		try {
			Collection<AIType> AIlist = proxy.gameListAI();
			AIType AI = AIlist.iterator().next();
			assertTrue(AI instanceof AIType);
			return;
		} catch (Exception err) {
			fail("GameListAI did not return a collection of String");
			err.printStackTrace();
		}
	}

	@Test
	public void testMovesSendChat() {
		proxy.movesSendChat(3, "Hello world!");
	}

	@Test
	public void testMovesRollNumber() {
		proxy.movesRollNumber(3, 4);
	}

	@Test
	public void testMovesRobPlayer() {
		HexLocation location = new HexLocation(2, 2);
		proxy.movesRobPlayer(3, 0, location);
	}

	@Test
	public void testMovesFinishTurn() {
		proxy.movesFinishTurn("Null", 3);
	}

	@Test
	public void testMovesBuyDevCard() {
		proxy.movesBuyDevCard(null, 3);
	}

	@Test
	public void testMovesYear_of_Plenty() {
		proxy.movesYear_of_Plenty(3, ResourceType.BRICK, ResourceType.WOOD);
	}

	@Test
	public void testMovesRoad_Building() {
		HexLocation hexLocation1 = new HexLocation(0, 0);
		EdgeLocation location1 = new EdgeLocation(hexLocation1, EdgeDirection.NorthEast);

		HexLocation hexLocation2 = new HexLocation(1, -1);
		EdgeLocation location2 = new EdgeLocation(hexLocation2, EdgeDirection.South);

		proxy.movesRoad_Building(0, location1, location2);
	}

	@Test
	public void testMovesSoldier() {
		HexLocation location = new HexLocation(2, 2);

		proxy.movesSoldier(3, 0, location);
	}

	@Test
	public void testMovesMonopoly() {
		proxy.movesMonopoly(ResourceType.BRICK, 3);
	}

	@Test
	public void testMovesMonument() {
		proxy.movesMonument(null, 0);
	}

	@Test
	public void testMovesBuildRoad() {
		HexLocation hexLocation = new HexLocation(1, 1);
		EdgeLocation location = new EdgeLocation(hexLocation, EdgeDirection.North);

		proxy.movesBuildRoad(3, location, true);
	}

	@Test
	public void testMovesBuildSettlement() {
		HexLocation hexLocation = new HexLocation(1, 1);
		VertexLocation location = new VertexLocation(hexLocation, VertexDirection.East);

		proxy.movesBuildSettlement(3, location, false);
	}

	@Test
	public void testMovesBuildCity() {
		HexLocation hexLocation = new HexLocation(1, 1);
		VertexLocation location = new VertexLocation(hexLocation, VertexDirection.East);

		proxy.movesBuildCity(3, location);
	}

	@Test
	public void testMovesOfferTrade() {
		proxy.movesOfferTrade(3, 0, 0, 0, 0, 1, 0);
	}

	@Test
	public void testMovesAcceptTrade() {
		proxy.movesAcceptTrade(3, true);
	}

	@Test
	public void testMovesMaritimeTrade() {
		proxy.movesMaritimeTrade(3, 3, ResourceType.BRICK, ResourceType.ORE);
	}

	@Test
	public void testMovesDiscardCards() {
		proxy.movesDiscardCards(3, 2, 2, 2, 2, 2);
	}
}
