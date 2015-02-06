package clientBackend.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import shared.locations.HexLocation;

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
		fail("Not yet implemented");
	}

	@Test
	public void testBoardTransportMap() {
		fail("Not yet implemented");
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
	public void testGenerateInvoices() {
		// fail("Not yet implemented");
	}

	@Test
	public void testCanPlaceRoad() {
		fail("Not yet implemented");
	}

	@Test
	public void testPlaceRoad() {
		fail("Not yet implemented");
	}

	@Test
	public void testCanPlaceDwelling() {
		fail("Not yet implemented");
	}

	@Test
	public void testPlaceDwelling() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetHarborsByPlayer() {
		fail("Not yet implemented");
	}

}
