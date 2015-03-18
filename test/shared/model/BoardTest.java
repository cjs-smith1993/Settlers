package shared.model;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import shared.definitions.PlayerNumber;
import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;
import shared.model.Board;
import shared.model.CatanException;
import shared.model.City;
import shared.model.Harbor;
import shared.model.ResourceInvoice;
import shared.model.Road;
import shared.model.Settlement;

public class BoardTest {

	Board board;
	private final PlayerNumber player1 = PlayerNumber.ONE;
	private final PlayerNumber player2 = PlayerNumber.TWO;
	private final PlayerNumber player3 = PlayerNumber.THREE;
	private final PlayerNumber player4 = PlayerNumber.FOUR;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setUp() throws Exception {
		this.board = new Board(false, false, false);
	}

	@Test
	public void testBoardBooleanBooleanBoolean() {
		// fail("Not yet implemented");
	}

	@Test
	public void testBoardTransportMap() {
		// fail("Not yet implemented");
	}

	@Test
	public void testCanMoveRobber() {
		HexLocation startLocation = this.board.getRobberLocation();
		HexLocation newLocation1 = new HexLocation(1, 0);
		HexLocation invalidLocation1 = new HexLocation(-3, -3);

		assertFalse("cannot move the robber to his current location",
				this.board.canMoveRobber(startLocation));
		assertTrue("can move the robber to a new valid location",
				this.board.canMoveRobber(newLocation1));
		assertFalse("cannot move the robber to water",
				this.board.canMoveRobber(invalidLocation1));
	}

	@Test
	public void testMoveRobber() throws CatanException {
		HexLocation newLocation = new HexLocation(1, 1);

		// move the robber to a valid location
		this.board.moveRobber(newLocation);
		assertEquals("robber is moved to new location", newLocation, this.board.getRobberLocation());

		// test moving the robber to his current location
		this.thrown.expect(CatanException.class);
		this.board.moveRobber(newLocation);

		// test moving the robber to a water tile
		HexLocation waterLocation = new HexLocation(2, 2);
		this.thrown.expect(CatanException.class);
		this.board.moveRobber(waterLocation);
	}

	@Test
	public void testGenerateInvoices() throws CatanException {
		VertexLocation location1 = new VertexLocation(new HexLocation(-2, 1),
				VertexDirection.NorthEast);
		VertexLocation location2 = new VertexLocation(new HexLocation(0, 0),
				VertexDirection.NorthWest);
		VertexLocation location3 = new VertexLocation(new HexLocation(1, 1),
				VertexDirection.NorthEast);
		VertexLocation location4 = new VertexLocation(new HexLocation(-2, 0),
				VertexDirection.West);

		Settlement settlement1 = new Settlement(this.player1, location1);
		Settlement settlement2 = new Settlement(this.player2, location2);
		City city2 = new City(this.player2, location2);
		Settlement settlement3 = new Settlement(this.player2, location3);
		Settlement settlement4 = new Settlement(this.player2, location4);
		City city4 = new City(this.player2, location4);

		ResourceInvoice invoice1 = new ResourceInvoice(PlayerNumber.BANK, PlayerNumber.ONE);
		invoice1.setWheat(1);
		ResourceInvoice invoice2_1 = new ResourceInvoice(PlayerNumber.BANK, PlayerNumber.ONE);
		invoice2_1.setSheep(1);
		ResourceInvoice invoice2_2 = new ResourceInvoice(PlayerNumber.BANK, PlayerNumber.TWO);
		invoice2_2.setSheep(2);
		ResourceInvoice invoice3_1 = new ResourceInvoice(PlayerNumber.BANK, PlayerNumber.TWO);
		invoice3_1.setSheep(3);
		ResourceInvoice invoice4_1 = new ResourceInvoice(PlayerNumber.BANK, PlayerNumber.ONE);
		invoice4_1.setOre(1);
		ResourceInvoice invoice4_2 = new ResourceInvoice(PlayerNumber.BANK, PlayerNumber.TWO);
		invoice4_2.setOre(2);
		invoice4_2.setBrick(1);

		// 0. blank test
		Collection<ResourceInvoice> invoices = this.board.generateInvoices(2);
		assertEquals("no invoices for a blank board", 0, invoices.size());

		// 1. single invoice test
		this.board.placeSettlement(settlement1, location1, true);
		invoices = this.board.generateInvoices(2);
		assertEquals("one invoice", 1, invoices.size());
		assertEquals("one wheat to player 1", invoice1, invoices.iterator().next());

		// 2. two invoice test
		this.board.placeSettlement(settlement2, location2, true);
		this.board.placeCity(city2, location2, true);
		invoices = this.board.generateInvoices(10);
		assertEquals("two invoices", 2, invoices.size());
		assertTrue("one sheep to player 1", invoices.contains(invoice2_1));
		assertTrue("two sheep to player 2", invoices.contains(invoice2_2));

		// 3. combining invoices test
		this.board.placeSettlement(settlement3, location3, true);
		invoices = this.board.generateInvoices(10);
		assertEquals("two invoices", 2, invoices.size());
		assertTrue("one sheep to player 1", invoices.contains(invoice2_1));
		assertTrue("three sheep to player 2", invoices.contains(invoice3_1));

		// 4. multiple invoices per player test
		this.board.placeSettlement(settlement4, location4, true);
		this.board.placeCity(city4, location4, true);
		invoices = this.board.generateInvoices(5);
		assertEquals("three invoices", 2, invoices.size());
		assertTrue("one ore to player 1", invoices.contains(invoice4_1));
		assertTrue("two ore and one brick to player 2", invoices.contains(invoice4_2));
	}

	@Test
	public void testCanPlaceRoad() throws CatanException {
		HexLocation hex1 = new HexLocation(0, 0);
		HexLocation hex2 = new HexLocation(-1, -1);
		HexLocation hex3 = new HexLocation(2, 2);
		HexLocation hex4 = new HexLocation(0, 3);

		EdgeLocation edge1 = new EdgeLocation(hex1, EdgeDirection.North);
		EdgeLocation edge2 = new EdgeLocation(hex2, EdgeDirection.SouthEast);
		EdgeLocation edge3 = new EdgeLocation(hex3, EdgeDirection.South);
		EdgeLocation edge4 = new EdgeLocation(hex4, EdgeDirection.North);
		EdgeLocation edge5 = new EdgeLocation(hex1, EdgeDirection.NorthEast);

		VertexLocation vertex1 = new VertexLocation(hex1, VertexDirection.NorthWest);
		VertexLocation vertex4 = new VertexLocation(hex4, VertexDirection.NorthWest);
		VertexLocation vertex5 = new VertexLocation(hex1, VertexDirection.East);

		Settlement settlement1 = new Settlement(this.player1, vertex1);
		Settlement settlement4 = new Settlement(this.player1, vertex4);
		Settlement settlement5 = new Settlement(this.player1, vertex5);

		this.board.placeSettlement(settlement1, vertex1, true);
		this.board.placeSettlement(settlement4, vertex4, true);
		this.board.placeSettlement(settlement5, vertex5, true);

		Road road1 = new Road(this.player1, edge1);
		Road road2 = new Road(this.player1, edge2);
		Road road3 = new Road(this.player1, edge3);
		Road road4 = new Road(this.player1, edge4);
		Road road5 = new Road(this.player1, edge5);

		// 1. try to place a road adjacent to the dwelling
		assertTrue("can place a road next to a dwelling",
				this.board.canPlaceRoad(road1.getOwner(), edge1, true));
		this.board.placeRoad(road1, edge1, true);

		// 2. try to place a road not connected to another road
		assertFalse("cannot place a road disconnected from a dwelling",
				this.board.canPlaceRoad(road2.getOwner(), edge2, false));

		// 3. try to place a road on the water
		assertFalse("cannot place a road on water",
				this.board.canPlaceRoad(road3.getOwner(), edge3, true));

		// 4. try to place a road on the coast
		assertTrue("can place a road on the coast",
				this.board.canPlaceRoad(road4.getOwner(), edge4, true));
		this.board.placeRoad(road4, edge4, true);

		// 5. try to place a road blocked by another player's dwelling
		this.board.placeRoad(road5, edge5, false);
		assertFalse("cannot place a road through another player's dwelling",
				this.board.canPlaceRoad(road5.getOwner(), edge5, false));
	}

	@Test
	public void testPlaceRoad() throws CatanException {
		HexLocation hex1 = new HexLocation(0, 0);
		VertexLocation vertex1 = new VertexLocation(hex1, VertexDirection.NorthWest);
		Settlement settlement1 = new Settlement(this.player1, vertex1);
		this.board.placeSettlement(settlement1, vertex1, true);

		// place a road
		EdgeLocation edge1 = new EdgeLocation(hex1, EdgeDirection.North);
		Road road1 = new Road(this.player1, edge1);
		this.board.placeRoad(road1, edge1, true);

		// try to place another road on top of it
		this.thrown.expect(CatanException.class);
		this.board.placeRoad(road1, edge1, true);
	}

	@Test
	public void testCanPlaceDwelling() throws CatanException {
		HexLocation hex1 = new HexLocation(0, 0);
		HexLocation hex4 = new HexLocation(0, 3);
		HexLocation hex7 = new HexLocation(0, 1);

		VertexLocation vertex1 = new VertexLocation(hex1, VertexDirection.NorthWest);
		VertexLocation vertex2 = new VertexLocation(hex1, VertexDirection.East);
		VertexLocation vertex3 = new VertexLocation(hex1, VertexDirection.NorthEast);
		VertexLocation vertex4 = new VertexLocation(hex4, VertexDirection.NorthWest);
		VertexLocation vertex5 = new VertexLocation(hex4, VertexDirection.NorthEast);
		VertexLocation vertex6 = new VertexLocation(hex4, VertexDirection.NorthEast);
		VertexLocation vertex7 = new VertexLocation(hex1, VertexDirection.SouthWest);

		Settlement settlement1 = new Settlement(this.player1, vertex1);
		Settlement settlement2 = new Settlement(this.player1, vertex2);
		Settlement settlement3 = new Settlement(this.player1, vertex3);
		Settlement settlement4 = new Settlement(this.player1, vertex4);
		Settlement settlement5 = new Settlement(this.player1, vertex5);
		Settlement settlement6 = new Settlement(this.player1, vertex6);
		Settlement settlement7 = new Settlement(this.player2, vertex7);

		EdgeLocation edge1 = new EdgeLocation(hex1, EdgeDirection.North);
		EdgeLocation edge2 = new EdgeLocation(hex1, EdgeDirection.NorthEast);
		EdgeLocation edge7_1 = new EdgeLocation(hex1, EdgeDirection.NorthWest);
		EdgeLocation edge7_2 = new EdgeLocation(hex1, EdgeDirection.SouthWest);
		EdgeLocation edge7_3 = new EdgeLocation(hex7, EdgeDirection.NorthWest);

		Road road1 = new Road(this.player1, edge1);
		Road road2 = new Road(this.player1, edge2);
		Road road7_1 = new Road(this.player1, edge7_1);
		Road road7_2 = new Road(this.player1, edge7_2);
		Road road7_3 = new Road(this.player1, edge7_3);

		// 1. try to place the first dwelling
		assertTrue("can place the first dwelling",
				this.board.canPlaceSettlement(settlement1.getOwner(), vertex1, true));
		this.board.placeSettlement(settlement1, vertex1, true);
		this.board.placeRoad(road1, edge1, true);

		// 2. try to place the second dwelling
		assertTrue("can place the second dwelling",
				this.board.canPlaceSettlement(settlement2.getOwner(), vertex2, true));
		this.board.placeSettlement(settlement2, vertex2, true);
		this.board.placeRoad(road2, edge2, true);

		// 3. try to place a dwelling too close to another dwelling
		assertFalse("cannot place a dwelling adjacent to another dwelling",
				this.board.canPlaceSettlement(settlement3.getOwner(), vertex3, false));

		// 4. try to place a dwelling not connected to a road
		assertFalse("cannot place a dwelling disconnected from a road",
				this.board.canPlaceSettlement(settlement4.getOwner(), vertex4, false));

		// 5. try to place a dwelling on the water
		assertFalse("cannot place a dwelling on the water",
				this.board.canPlaceSettlement(settlement5.getOwner(), vertex5, false));

		// 6. try to place a dwelling on the coast
		assertTrue("can place a dwelling on the coast",
				this.board.canPlaceSettlement(settlement6.getOwner(), vertex6, true));
		this.board.placeSettlement(settlement6, vertex6, true);

		// 7. try to place a dwelling between another player's roads
		this.board.placeRoad(road7_1, edge7_1, false);
		this.board.placeRoad(road7_2, edge7_2, false);
		this.board.placeRoad(road7_3, edge7_3, false);
		assertFalse("cannot place a dwelling between another player's roads",
				this.board.canPlaceSettlement(settlement7.getOwner(), vertex7, false));
	}

	@Test
	public void testPlaceDwelling() throws CatanException {
		HexLocation hex1 = new HexLocation(0, 0);
		VertexLocation vertex1 = new VertexLocation(hex1, VertexDirection.NorthWest);
		Settlement settlement1 = new Settlement(this.player1, vertex1);

		// place a dwelling
		this.board.placeSettlement(settlement1, vertex1, true);

		// try to place another dwelling on top of it
		this.thrown.expect(CatanException.class);
		this.board.placeSettlement(settlement1, vertex1, true);
	}

	@Test
	public void testGetHarborsByPlayer() throws CatanException {
		VertexLocation vertex1 = new VertexLocation(new HexLocation(0, 3),
				VertexDirection.NorthWest);
		VertexLocation vertex2 = new VertexLocation(new HexLocation(2, 1),
				VertexDirection.NorthWest);
		VertexLocation vertex3 = new VertexLocation(new HexLocation(3, -1),
				VertexDirection.West);
		VertexLocation vertex4 = new VertexLocation(new HexLocation(3, -3),
				VertexDirection.SouthWest);
		VertexLocation vertex5 = new VertexLocation(new HexLocation(1, -3),
				VertexDirection.SouthWest);
		VertexLocation vertex6 = new VertexLocation(new HexLocation(-1, -2),
				VertexDirection.SouthWest);
		VertexLocation vertex7 = new VertexLocation(new HexLocation(-3, 0),
				VertexDirection.SouthEast);
		VertexLocation vertex8 = new VertexLocation(new HexLocation(-3, 2),
				VertexDirection.NorthEast);
		VertexLocation vertex9 = new VertexLocation(new HexLocation(-2, 3),
				VertexDirection.NorthEast);

		Settlement settlement1 = new Settlement(this.player1, vertex1);
		Settlement settlement2 = new Settlement(this.player1, vertex2);
		Settlement settlement3 = new Settlement(this.player2, vertex3);
		Settlement settlement4 = new Settlement(this.player2, vertex4);
		Settlement settlement5 = new Settlement(this.player3, vertex5);
		Settlement settlement6 = new Settlement(this.player3, vertex6);
		Settlement settlement7 = new Settlement(this.player4, vertex7);
		Settlement settlement8 = new Settlement(this.player4, vertex8);
		Settlement settlement9 = new Settlement(this.player4, vertex9);

		this.board.placeSettlement(settlement1, vertex1, true);
		this.board.placeSettlement(settlement2, vertex2, true);
		this.board.placeSettlement(settlement3, vertex3, true);
		this.board.placeSettlement(settlement4, vertex4, true);
		this.board.placeSettlement(settlement5, vertex5, true);
		this.board.placeSettlement(settlement6, vertex6, true);
		this.board.placeSettlement(settlement7, vertex7, true);
		this.board.placeSettlement(settlement8, vertex8, true);
		this.board.placeSettlement(settlement9, vertex9, true);

		Map<PlayerNumber, Collection<Harbor>> harbors =
				this.board.getHarborsByPlayer();

		assertEquals("player one has two harbors", 2, harbors.get(this.player1).size());
		assertEquals("player two has two harbors", 2, harbors.get(this.player2).size());
		assertEquals("player three has two harbors", 2, harbors.get(this.player3).size());
		assertEquals("player four has three harbors", 3, harbors.get(this.player4).size());

		this.board = new Board(true, true, true);

	}
}
