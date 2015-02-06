package clientBackend.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import shared.locations.HexLocation;

public class BoardTest {

	Board board;

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
		HexLocation startLocation = new HexLocation(0, 0);
		HexLocation newLocation1 = new HexLocation(1, 0);
		HexLocation invalidLocation1 = new HexLocation(-3, -3);

		try {
			this.board.moveRobber(startLocation);
			assertFalse("cannot move the robber to his current location",
					this.board.canMoveRobber(startLocation));
			assertTrue("can move the robber to a new location",
					this.board.canMoveRobber(newLocation1));
			assertFalse("cannot move the robber to water",
					this.board.canMoveRobber(invalidLocation1));

		} catch (CatanException e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}

	@Test
	public void testMoveRobber() {
		fail("Not yet implemented");
	}

	@Test
	public void testGenerateInvoices() {
		fail("Not yet implemented");
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
