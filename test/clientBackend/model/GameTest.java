package clientBackend.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import client.backend.transport.*;
import shared.definitions.CatanColor;
import shared.definitions.PlayerNumber;
import shared.model.CatanException;
import shared.model.Game;
import shared.model.Player;
import shared.model.Settlement;
import shared.model.User;

public class GameTest {
	
	private Game game;

	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		game = new Game();
	}

	@After
	public void tearDown() throws Exception {
		game = null;
	}

	@Test
	public void testGame() throws CatanException {
		User U1 = new User("U1",1);
		User U2 = new User("U2",2);
		User U3 = new User("U3",3);
		User U4 = new User("U4",4);
		User U5 = new User("U5",5);
		
		/* Add user */
		assertTrue(game.canAddPlayer(U1, CatanColor.BLUE));
		game.addPlayer(U1, CatanColor.BLUE);
		
		boolean exceptionThrown = false;
		
		/* Add same user, same color */
		game = new Game();
		assertTrue(game.canAddPlayer(U1, CatanColor.BLUE));
		game.addPlayer(U1, CatanColor.BLUE);
		try {
			assertFalse(game.canAddPlayer(U1, CatanColor.BLUE));
			game.addPlayer(U1, CatanColor.BLUE);
		}
		catch (CatanException err) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		exceptionThrown = false;
		
		/* Add same user, different color */
		game = new Game();
		assertTrue(game.canAddPlayer(U1, CatanColor.BLUE));
		game.addPlayer(U1, CatanColor.BLUE);
		try {
			assertFalse(game.canAddPlayer(U1, CatanColor.BROWN));
			game.addPlayer(U1, CatanColor.BROWN);
		}
		catch (CatanException err) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		exceptionThrown = false;
		
		/* Add different user, same color */
		game = new Game();
		assertTrue(game.canAddPlayer(U1, CatanColor.BLUE));
		game.addPlayer(U1, CatanColor.BLUE);
		try {
			assertFalse(game.canAddPlayer(U2, CatanColor.BLUE));
			game.addPlayer(U2, CatanColor.BLUE);
		}
		catch (CatanException err) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		exceptionThrown = false;
		
		/* Add four users */
		game = new Game();
		assertTrue(game.canAddPlayer(U1, CatanColor.BLUE));
		game.addPlayer(U1, CatanColor.BLUE);
		assertTrue(game.canAddPlayer(U2, CatanColor.BROWN));
		game.addPlayer(U2, CatanColor.BROWN);
		assertTrue(game.canAddPlayer(U3, CatanColor.GREEN));
		game.addPlayer(U3, CatanColor.GREEN);
		assertTrue(game.canAddPlayer(U4, CatanColor.ORANGE));
		game.addPlayer(U4, CatanColor.ORANGE);
		
		/* Size of map should be 4 */
		assertEquals(game.getNumPlayers(), 4);
		
		/* Each player's PlayerNumber should match its map key */
		Map<PlayerNumber, Player> players = game.getPlayers();
		for (PlayerNumber number : players.keySet()) {
			assertEquals(number, players.get(number).getNumber());
		}
		
		/* Add fifth user */
		try {
			assertFalse(game.canAddPlayer(U5, CatanColor.PUCE));
			game.addPlayer(U5, CatanColor.PUCE);
		}
		catch (CatanException err) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
	}

	@Test
	public void testGameCollectionOfTransportPlayerTransportTurnTracker() throws CatanException {
		
		ArrayList<TransportPlayer> new_players = new ArrayList<TransportPlayer>();
		
		TransportPlayer P1 = new TransportPlayer();
		P1.name = "P1";
		P1.color = CatanColor.BLUE;
		P1.playerIndex = PlayerNumber.ONE;
		P1.playerID = 1;
		
		// badP1 is identical to P1
		TransportPlayer badP1 = new TransportPlayer();
		badP1.name = "P1";
		badP1.color = CatanColor.BLUE;
		badP1.playerIndex = PlayerNumber.ONE;
		badP1.playerID = 1;
		
		TransportPlayer P2 = new TransportPlayer();
		P2.name = "P2";
		P2.color = CatanColor.BROWN;
		P2.playerIndex = PlayerNumber.TWO;
		P2.playerID = 2;
		
		// badP2 is identical to P2 except for the name
		TransportPlayer badP2 = new TransportPlayer();
		badP2.name = "badP2";
		badP2.color = CatanColor.BROWN;
		badP2.playerIndex = PlayerNumber.TWO;
		badP2.playerID = 2;
		
		TransportPlayer P3 = new TransportPlayer();
		P3.name = "P3";
		P3.color = CatanColor.GREEN;
		P3.playerIndex = PlayerNumber.THREE;
		P3.playerID = 3;
		
		// badP3 is identical to P3 except for the name and color
		TransportPlayer badP3 = new TransportPlayer();
		badP3.name = "badP3";
		badP3.color = CatanColor.ORANGE;
		badP3.playerIndex = PlayerNumber.THREE;
		badP3.playerID = 3;
		
		TransportPlayer P4 = new TransportPlayer();
		P4.name = "P4";
		P4.color = CatanColor.PUCE;
		P4.playerIndex = PlayerNumber.FOUR;
		P4.playerID = 4;
		
		// badP4 has the same playerID as P4
		TransportPlayer badP4 = new TransportPlayer();
		badP4.name = "badP4";
		badP4.color = CatanColor.PURPLE;
		badP4.playerIndex = PlayerNumber.BANK;
		badP4.playerID = 4;
		
		TransportTurnTracker tracker = new TransportTurnTracker();
		tracker.currentTurn = PlayerNumber.THREE;
		
		/* Test correct initialization */
		game = new Game(new_players, tracker);
		assertEquals(game.getCurrentPlayer(), tracker.currentTurn);
		
		boolean exceptionThrown = false;
		
		/* Test adding 5 players */
		try {
			new_players = new ArrayList<TransportPlayer>();
			new_players.add(P1);
			new_players.add(P2);
			new_players.add(P3);
			new_players.add(P4);
			new_players.add(badP1);
			
			game = new Game(new_players, tracker);
		}
		catch(CatanException err) {
			exceptionThrown = true;
		}
		
		assertTrue(exceptionThrown);
		exceptionThrown = false;
		
		/* Test with identical player */
		try {
			new_players = new ArrayList<TransportPlayer>();
			new_players.add(P1);
			new_players.add(P2);
			new_players.add(P3);
			new_players.add(badP1);
			
			game = new Game(new_players, tracker);
		}
		catch(CatanException err) {
			exceptionThrown = true;
		}

		assertTrue(exceptionThrown);
		exceptionThrown = false;
		
		/* Test with similar player */
		try {
			new_players = new ArrayList<TransportPlayer>();
			new_players.add(P1);
			new_players.add(P2);
			new_players.add(P3);
			new_players.add(badP4);
			
			game = new Game(new_players, tracker);
		}
		catch(CatanException err) {
			exceptionThrown = true;
		}

		assertTrue(exceptionThrown);
		
	}

	@Test
	public void testGetRoad() throws CatanException {
		User U1 = new User("U1",1);
		assertTrue(game.canAddPlayer(U1, CatanColor.BLUE));
		game.addPlayer(U1, CatanColor.BLUE);
		
		/* Remove all roads from player */
		for (int i = 0; i < Player.MAX_ROADS; i++) {
			assertTrue(game.hasRoad(PlayerNumber.ONE));
			game.getRoad(PlayerNumber.ONE);
		}
		
		boolean exceptionThrown = false;
		
		/* Should not have any more roads */
		try {
			assertFalse(game.hasRoad(PlayerNumber.ONE));
			game.getRoad(PlayerNumber.ONE);
		}
		catch (CatanException err) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
	}

	@Test
	public void testGetSettlement() throws CatanException {
		User U1 = new User("U1",1);
		assertTrue(game.canAddPlayer(U1, CatanColor.BLUE));
		game.addPlayer(U1, CatanColor.BLUE);
		assertEquals(game.getNumSettlements(PlayerNumber.ONE), Player.MAX_SETTLEMENTS);
		ArrayList<Settlement> settlements = new ArrayList<Settlement>();
		
		/* Remove all settlements from player */
		for (int i = 0; i < Player.MAX_SETTLEMENTS; i++) {
			assertTrue(game.hasRoad(PlayerNumber.ONE));
			Settlement settlement = game.getSettlement(PlayerNumber.ONE);
			settlements.add(settlement);
		}
		assertEquals(game.getNumSettlements(PlayerNumber.ONE), 0);
		
		boolean exceptionThrown = false;
		
		/* Should not have any more settlements */
		try {
			assertFalse(game.hasSettlement(PlayerNumber.ONE));
			assertEquals(settlements.size(), Player.MAX_SETTLEMENTS);
			game.getSettlement(PlayerNumber.ONE);
		}
		catch (CatanException err) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
		
		/* Return all settlements */
		for (int i = 0; i < Player.MAX_SETTLEMENTS; i++) {
			assertTrue(settlements.size() > 0);
			Settlement settlement = settlements.get(0);
			game.returnSettlement(PlayerNumber.ONE, settlement);
		}
		assertEquals(game.getNumSettlements(PlayerNumber.ONE), Player.MAX_SETTLEMENTS);
	}

	@Test
	public void testGetCity() throws CatanException {
		User U1 = new User("U1",1);
		assertTrue(game.canAddPlayer(U1, CatanColor.BLUE));
		game.addPlayer(U1, CatanColor.BLUE);
		
		/* Remove all cities from player */
		for (int i = 0; i < Player.MAX_CITIES; i++) {
			assertTrue(game.hasCity(PlayerNumber.ONE));
			game.getCity(PlayerNumber.ONE);
		}
		
		boolean exceptionThrown = false;
		
		/* Should not have any more cities */
		try {
			assertFalse(game.hasCity(PlayerNumber.ONE));
			game.getCity(PlayerNumber.ONE);
		}
		catch (CatanException err) {
			exceptionThrown = true;
		}
		assertTrue(exceptionThrown);
	}

}
