package clientBackend.model;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import shared.definitions.PlayerNumber;
import shared.definitions.Status;
import clientBackend.transport.*;

public class FacadeTest {
	private Facade facade;
	TransportModel model;
	TransportPlayer player;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		facade = new Facade();
		model = new TransportModel();
		player = new TransportPlayer();
		
		model.deck = new TransportDeck();
		model.bank = new TransportBank();
		model.players = new ArrayList<TransportPlayer>();
		model.map = new TransportMap();
		model.turnTracker = new TransportTurnTracker();
		model.chat = new TransportChat();
		model.chat.lines = new ArrayList<TransportLine>();
		model.log = new TransportLog();
		model.log.lines = new ArrayList<TransportLine>();
		
		model.turnTracker.status = Status.PLAYING;
		model.turnTracker.currentTurn = PlayerNumber.ONE;
		
		model.deck.yearOfPlenty = 2;
		model.deck.monopoly = 2;
		model.deck.soldier = 14;
		model.deck.roadBuilding = 2;
		model.deck.monument = 5;
		
		model.bank.brick = 23;
		model.bank.ore = 21;
		model.bank.sheep = 23;
		model.bank.wheat = 21;
		model.bank.wood = 23;
		
		player.resources = new TransportResources();
		player.oldDevCards = new TransportOldDevCards();
		player.newDevCards = new TransportNewDevCards();
		
		
		player.resources.brick = 2;
		player.resources.ore = 4;
		player.resources.sheep = 2;
		player.resources.wheat = 4;
		player.resources.wood = 2;
		
		player.oldDevCards.yearOfPlenty = 1;
		player.oldDevCards.monopoly = 1;
		player.oldDevCards.soldier = 1;
		player.oldDevCards.roadBuilding = 1;
		player.oldDevCards.monument = 1;
		
		player.roads = 15;
		player.settlements = 5;
		player.cities = 4;
		player.playerIndex = PlayerNumber.ONE;
		
		model.players.add(player);
		
		model.map.hexes = new ArrayList<TransportHex>();
		model.map.roads = new ArrayList<TransportRoad>();
		model.map.cities = new ArrayList<TransportCity>();
		model.map.settlements = new ArrayList<TransportSettlement>();
		model.map.ports = new ArrayList<TransportPort>();
		model.map.robber = new TransportRobber();
		
		try {
			facade.initializeModel(model);
		} catch (CatanException e) {
			e.printStackTrace();
			fail();
		}
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testCanDiscardCards() throws CatanException {
		
		player.resources.brick = 8;
		player.resources.ore = 0;
		player.resources.sheep = 0;
		player.resources.wheat = 0;
		player.resources.wood = 0;
		model.players = new ArrayList<TransportPlayer>();
		model.players.add(player);
		model.turnTracker.status = Status.DISCARDING;
		facade.initializeModel(model);
		/* Should work because the status is discarding and the player has more
		 * than 7 cards */
		assertTrue(facade.canDiscardCards(PlayerNumber.ONE));
		
		player.resources.brick = 7;
		model.players = new ArrayList<TransportPlayer>();
		model.players.add(player);
		facade.initializeModel(model);
		/* Should fail because the client player only has 7 resource cards*/
		assertFalse(facade.canDiscardCards(PlayerNumber.ONE));
		
		model.turnTracker.status = Status.ROLLING;
		player.resources.brick = 8;
		model.players = new ArrayList<TransportPlayer>();
		model.players.add(player);
		facade.initializeModel(model);
		/* Should fail because the status is not discarding */
		assertFalse(facade.canDiscardCards(PlayerNumber.ONE));
	}

	@Test
	public void testCanRollNumber() throws CatanException {
		model.turnTracker.status = Status.ROLLING;
		facade.initializeModel(model);
		/* Should pass because it is the player's turn and the status is
		 * rolling */
		assertTrue(facade.canRollNumber(PlayerNumber.ONE));
		
		model.turnTracker.currentTurn = PlayerNumber.TWO;
		facade.initializeModel(model);
		/* Should fail because it is not the player's turn*/
		assertFalse(facade.canRollNumber(PlayerNumber.ONE));
	}

	@Test
	public void testCanBuildRoad() {
		//fail("Not yet implemented");
	}

	@Test
	public void testCanBuildSettlement() {
		//fail("Not yet implemented");
	}

	@Test
	public void testCanBuildCity() {
		//fail("Not yet implemented");
	}

	@Test
	public void testCanOfferTrade() {
		//fail("Not yet implemented");
	}

	@Test
	public void testCanAcceptTrade() {
		//fail("Not yet implemented");
	}

	@Test
	public void testCanMaritimeTrade() {
		//fail("Not yet implemented");
	}

	@Test
	public void testCanFinishTurn() throws CatanException {
		model.turnTracker.status = Status.PLAYING;
		facade.initializeModel(model);
		/* Should pass because it is the player's turn and the status is
		 * playing */
		assertTrue(facade.canFinishTurn(PlayerNumber.ONE));
	}

	@Test
	public void testFinishTurn() throws CatanException {
		
	}

	@Test
	public void testCanBuyDevCard() {
		//fail("Not yet implemented");
	}

	@Test
	public void testCanUseYearOfPlenty() {
		//fail("Not yet implemented");
	}

	@Test
	public void testCanUseRoadBuilder() {
		//fail("Not yet implemented");
	}

	@Test
	public void testCanUseSoldier() {
		//fail("Not yet implemented");
	}

	@Test
	public void testCanUseMonopoly() {
		//fail("Not yet implemented");
	}

	@Test
	public void testCanUseMonument() {
		//fail("Not yet implemented");
	}

	@Test
	public void testCanPlaceRobber() {
		//fail("Not yet implemented");
	}

}
