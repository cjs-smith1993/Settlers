package clientBackend.model;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import shared.definitions.PlayerNumber;
import shared.definitions.ResourceType;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;

public class BoardTest {

	Board board;

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

		this.board.moveRobber(newLocation);
		assertEquals("robber is moved to new location", newLocation, this.board.getRobberLocation());
		this.thrown.expect(CatanException.class);
		this.board.moveRobber(newLocation);
	}

	@Test
	public void testGenerateInvoices() throws CatanException {
		PlayerNumber owner1 = PlayerNumber.ONE;
		PlayerNumber owner2 = PlayerNumber.TWO;

		VertexLocation location1 = new VertexLocation(new HexLocation(-2, 1),
				VertexDirection.NorthEast);
		VertexLocation location2 = new VertexLocation(new HexLocation(0, 0),
				VertexDirection.NorthWest);
		VertexLocation location3 = new VertexLocation(new HexLocation(1, 1),
				VertexDirection.NorthEast);
		VertexLocation location4 = new VertexLocation(new HexLocation(-2, 0),
				VertexDirection.West);

		Dwelling dwelling1 = new Settlement(owner1, location1);
		Dwelling dwelling2 = new City(owner2, location2);
		Dwelling dwelling3 = new Settlement(owner2, location3);
		Dwelling dwelling4 = new City(owner2, location4);

		ResourceInvoice invoice1 = new ResourceInvoice(ResourceType.WHEAT, 1, PlayerNumber.BANK,
				PlayerNumber.ONE);
		ResourceInvoice invoice2_1 = new ResourceInvoice(ResourceType.SHEEP, 1, PlayerNumber.BANK,
				PlayerNumber.ONE);
		ResourceInvoice invoice2_2 = new ResourceInvoice(ResourceType.SHEEP, 2, PlayerNumber.BANK,
				PlayerNumber.TWO);
		ResourceInvoice invoice3_1 = new ResourceInvoice(ResourceType.SHEEP, 3, PlayerNumber.BANK,
				PlayerNumber.TWO);
		ResourceInvoice invoice4_1 = new ResourceInvoice(ResourceType.ORE, 1, PlayerNumber.BANK,
				PlayerNumber.ONE);
		ResourceInvoice invoice4_2 = new ResourceInvoice(ResourceType.ORE, 2, PlayerNumber.BANK,
				PlayerNumber.TWO);
		ResourceInvoice invoice4_3 = new ResourceInvoice(ResourceType.BRICK, 1, PlayerNumber.BANK,
				PlayerNumber.TWO);

		// blank test
		Collection<ResourceInvoice> invoices = this.board.generateInvoices(2);
		assertEquals("no invoices for a blank board", 0, invoices.size());

		// single invoice test
		this.board.placeDwelling(dwelling1, location1, true);
		invoices = this.board.generateInvoices(2);
		assertEquals("one invoice", 1, invoices.size());
		assertEquals("one wheat to player 1", invoice1, invoices.iterator().next());

		// two invoice test
		this.board.placeDwelling(dwelling2, location2, true);
		invoices = this.board.generateInvoices(10);
		assertEquals("two invoices", 2, invoices.size());
		assertTrue("one sheep to player 1", invoices.contains(invoice2_1));
		assertTrue("two sheep to player 2", invoices.contains(invoice2_2));

		// combining invoices test
		this.board.placeDwelling(dwelling3, location3, true);
		invoices = this.board.generateInvoices(10);
		assertEquals("two invoices", 2, invoices.size());
		assertTrue("one sheep to player 1", invoices.contains(invoice2_1));
		assertTrue("three sheep to player 2", invoices.contains(invoice3_1));

		// multiple invoices per player test
		this.board.placeDwelling(dwelling4, location4, true);
		invoices = this.board.generateInvoices(5);
		assertEquals("three invoices", 3, invoices.size());
		assertTrue("one ore to player 1", invoices.contains(invoice4_1));
		assertTrue("two ore to player 2", invoices.contains(invoice4_2));
		assertTrue("one brick to player 2", invoices.contains(invoice4_3));
	}

	@Test
	public void testCanPlaceRoad() {
		// fail("Not yet implemented");
	}

	@Test
	public void testPlaceRoad() {
		// fail("Not yet implemented");
	}

	@Test
	public void testCanPlaceDwelling() {
		// fail("Not yet implemented");
	}

	@Test
	public void testPlaceDwelling() {
		// fail("Not yet implemented");
	}

	@Test
	public void testGetHarborsByPlayer() {
		// fail("Not yet implemented");
	}

}
