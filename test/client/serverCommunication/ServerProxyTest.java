package client.serverCommunication;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.BeforeClass;
import org.junit.Test;

import client.backend.ClientModelFacade;
import client.backend.dataTransportObjects.DTOGame;
import client.frontend.data.PlayerInfo;
import client.serverCommunication.ServerException;
import client.serverCommunication.ServerProxy;
import shared.definitions.AIType;
import shared.definitions.CatanColor;
import shared.definitions.PlayerNumber;
import shared.definitions.ResourceType;
import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;
import shared.model.ResourceInvoice;

public class ServerProxyTest {
	private static ServerProxy proxy = ServerProxy.getInstance("localhost", 8081);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		proxy.userLogin("Pete", "pete");
		proxy.gamesJoin(0, CatanColor.RED);

		PlayerInfo clientPlayer = new PlayerInfo();
		ClientModelFacade.getInstance().setClientPlayer(clientPlayer);
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
		assertTrue(proxy.movesSendChat(PlayerNumber.FOUR, "Hello world!"));
	}

	@Test
	public void testMovesRollNumber() {
		assertTrue(proxy.movesRollNumber(PlayerNumber.FOUR, 4));
	}

	@Test
	public void testMovesRobPlayer() {
		HexLocation location = new HexLocation(2, 2);
		assertTrue(proxy.movesRobPlayer(PlayerNumber.FOUR, PlayerNumber.ONE, location));
	}

	@Test
	public void testMovesFinishTurn() {
		assertTrue(proxy.movesFinishTurn(PlayerNumber.FOUR));
	}

	@Test
	public void testMovesBuyDevCard() {
		assertTrue(proxy.movesBuyDevCard(PlayerNumber.FOUR));
	}

	@Test
	public void testMovesYear_of_Plenty() {
		assertTrue(proxy.movesYear_of_Plenty(PlayerNumber.FOUR, ResourceType.BRICK,
				ResourceType.WOOD));
	}

	@Test
	public void testMovesRoad_Building() {
		HexLocation hexLocation1 = new HexLocation(0, 0);
		EdgeLocation location1 = new EdgeLocation(hexLocation1, EdgeDirection.NorthEast);

		HexLocation hexLocation2 = new HexLocation(1, -1);
		EdgeLocation location2 = new EdgeLocation(hexLocation2, EdgeDirection.South);

		assertTrue(proxy.movesRoad_Building(PlayerNumber.ONE, location1, location2));
	}

	@Test
	public void testMovesSoldier() {
		HexLocation location = new HexLocation(2, 2);

		assertTrue(proxy.movesSoldier(PlayerNumber.FOUR, PlayerNumber.ONE, location));
	}

	@Test
	public void testMovesMonopoly() {
		assertTrue(proxy.movesMonopoly(ResourceType.BRICK, PlayerNumber.FOUR));
	}

	@Test
	public void testMovesMonument() {
		assertTrue(proxy.movesMonument(PlayerNumber.FOUR));
	}

	@Test
	public void testMovesBuildRoad() {
		HexLocation hexLocation = new HexLocation(1, 1);
		EdgeLocation location = new EdgeLocation(hexLocation, EdgeDirection.North);

		assertTrue(proxy.movesBuildRoad(PlayerNumber.FOUR, location, true));
	}

	@Test
	public void testMovesBuildSettlement() {
		HexLocation hexLocation = new HexLocation(1, 1);
		VertexLocation location = new VertexLocation(hexLocation, VertexDirection.East);

		assertTrue(proxy.movesBuildSettlement(PlayerNumber.FOUR, location, false));
	}

	@Test
	public void testMovesBuildCity() {
		HexLocation hexLocation = new HexLocation(1, 1);
		VertexLocation location = new VertexLocation(hexLocation, VertexDirection.East);

		assertTrue(proxy.movesBuildCity(PlayerNumber.FOUR, location));
	}

	@Test
	public void testMovesOfferTrade() {
		ResourceInvoice invoice = new ResourceInvoice(PlayerNumber.FOUR, PlayerNumber.ONE);
		invoice.setWood(1);
		assertTrue(proxy.movesOfferTrade(invoice));

	}

	@Test
	public void testMovesAcceptTrade() {
		assertTrue(proxy.movesAcceptTrade(PlayerNumber.FOUR, true));
	}

	@Test
	public void testMovesMaritimeTrade() {
		assertTrue(proxy.movesMaritimeTrade(PlayerNumber.FOUR, 3, ResourceType.BRICK,
				ResourceType.ORE));
	}

	@Test
	public void testMovesDiscardCards() {
		assertTrue(proxy.movesDiscardCards(PlayerNumber.FOUR, 2, 2, 2, 2, 2));
	}
}
