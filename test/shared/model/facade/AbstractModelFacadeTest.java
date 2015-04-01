package shared.model.facade;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import client.backend.CatanSerializer;
import client.backend.ClientModelFacade;
import client.frontend.data.PlayerInfo;
import shared.definitions.CatanColor;
import shared.definitions.PlayerNumber;
import shared.definitions.ResourceType;
import shared.definitions.CatanState;
import shared.locations.EdgeDirection;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.model.CatanException;
import shared.transport.*;

public class AbstractModelFacadeTest {
	private AbstractModelFacade facade;
	TransportModel model;
	TransportPlayer player;
	private String cleanFile = "save/games/cleanGame.txt";
	
	@Before
	public void setUp() throws Exception {
		this.facade = new AbstractModelFacade();
		this.model = this.facade.getModelFromFile(cleanFile);
		this.player = this.model.players[0];
		this.facade.initializeModel(this.model);
	}

	@Test
	public void testCanDiscardCards() throws CatanException {

		this.player.resources.brick = 8;
		this.player.resources.ore = 0;
		this.player.resources.sheep = 0;
		this.player.resources.wheat = 0;
		this.player.resources.wood = 0;
		this.model.players = new TransportPlayer[1];
		this.model.players[0] = this.player;
		this.model.turnTracker.status = CatanState.DISCARDING;
		this.facade.initializeModel(this.model);
		/*
		 * Should work because the status is discarding and the player has more
		 * than 7 cards
		 */
		assertTrue(this.facade.needsToDiscardCards(PlayerNumber.ONE));

		this.player.resources.brick = 7;
		this.model.players = new TransportPlayer[1];
		this.model.players[0] = this.player;
		this.facade.initializeModel(this.model);
		/* Should fail because the client player only has 7 resource cards */
		assertFalse(this.facade.needsToDiscardCards(PlayerNumber.ONE));

		this.model.turnTracker.status = CatanState.ROLLING;
		this.player.resources.brick = 8;
		this.model.players = new TransportPlayer[1];
		this.model.players[0] = this.player;
		this.facade.initializeModel(this.model);
		/* Should fail because the status is not discarding */
		assertFalse(this.facade.needsToDiscardCards(PlayerNumber.ONE));
	}

	@Test
	public void testCanRollNumber() throws CatanException {
		this.model.turnTracker.status = CatanState.ROLLING;
		this.facade.initializeModel(this.model);
		/*
		 * Should pass because it is the player's turn and the status is rolling
		 */
		assertTrue(this.facade.canRollNumber(PlayerNumber.ONE));

		this.model.turnTracker.currentTurn = PlayerNumber.TWO;
		this.facade.initializeModel(this.model);
		/* Should fail because it is not the player's turn */
		assertFalse(this.facade.canRollNumber(PlayerNumber.ONE));
	}

	//	@Test
	//	public void testCanBuildRoad() {
	//		//fail("Not yet implemented");
	//	}
	//
	//	@Test
	//	public void testCanBuildSettlement() {
	//		//fail("Not yet implemented");
	//	}
	//
	//	@Test
	//	public void testCanBuildCity() {
	//		//fail("Not yet implemented");
	//	}
	//
	//	@Test
	//	public void testCanOfferTrade() {
	//		//fail("Not yet implemented");
	//	}
	//
	//	@Test
	//	public void testCanAcceptTrade() {
	//		//fail("Not yet implemented");
	//	}

	@Test
	public void testCanMaritimeTrade() throws CatanException {
		PlayerNumber player = PlayerNumber.ONE;
		this.model.turnTracker.status = CatanState.PLAYING;
		this.model.players[0].resources.ore = 3;
		TransportSettlement newSettlement = new TransportSettlement();
		newSettlement.location = new TransportVertexLocation();
		newSettlement.location.x = 1;
		newSettlement.location.y = 1;
		newSettlement.location.direction = VertexDirection.East;
		newSettlement.owner = PlayerNumber.ONE;
		this.model.map.settlements.add(newSettlement);
		this.facade.initializeModel(this.model);
		// Should not work
		assertFalse(this.facade.canMaritimeTrade(player, ResourceType.SHEEP));
		// Should work
		assertTrue(this.facade.canMaritimeTrade(player, ResourceType.ORE));
	}

	@Test
	public void testCanFinishTurn() throws CatanException {
		this.model.turnTracker.status = CatanState.PLAYING;
		this.facade.initializeModel(this.model);
		/*
		 * Should pass because it is the player's turn and the status is playing
		 */
		assertTrue(this.facade.canFinishTurn(PlayerNumber.ONE));
	}

	//	@Test
	//	public void testCanBuyDevCard() {
	//		//fail("Not yet implemented");
	//	}

	@Test
	public void testCanPlayYearOfPlenty() throws CatanException {
		this.model.turnTracker.currentTurn = PlayerNumber.ONE;
		this.model.turnTracker.status = CatanState.PLAYING;
		
		/*
		 * 1. Test doesn't have a card
		 */
		this.model.players[0].oldDevCards.yearOfPlenty = 0;
		this.model.players[0].newDevCards.yearOfPlenty = 1;
		this.facade.initializeModel(this.model);
		//Should not pass
		assertFalse(this.facade.canPlayYearOfPlenty(PlayerNumber.ONE, ResourceType.BRICK,
				ResourceType.WOOD));

		/*
		 * 2. Test has a card
		 */
		this.model.players[0].oldDevCards.yearOfPlenty = 1;
		this.model.players[0].newDevCards.yearOfPlenty = 0;
		this.facade.initializeModel(this.model);
		// Should pass
		assertTrue(this.facade.canPlayYearOfPlenty(PlayerNumber.ONE, ResourceType.BRICK,
				ResourceType.WOOD));

		/*
		 * 3. Test has a card but player has already played another dev card
		 */
		this.model.players[0].oldDevCards.yearOfPlenty = 1;
		this.model.players[0].newDevCards.yearOfPlenty = 0;
		this.model.players[0].playedDevCard = true;
		this.facade.initializeModel(this.model);
		//Should not pass
		assertFalse(this.facade.canPlayYearOfPlenty(PlayerNumber.ONE, ResourceType.BRICK,
				ResourceType.WOOD));
		
		/*
		 * 4. Test bank doesn't have resources
		 */
		this.model.bank.wood = 0;
		this.model.players[0].playedDevCard = false;
		this.facade.initializeModel(this.model);
		//Should not pass
		assertFalse(this.facade.canPlayYearOfPlenty(PlayerNumber.ONE, ResourceType.BRICK,
				ResourceType.WOOD));

	}

	@Test
	public void testCanUseRoadBuilding() throws CatanException {
		this.model.turnTracker.status = CatanState.ROLLING;
		this.model.players[0].oldDevCards.roadBuilding = 1;
		this.facade.initializeModel(this.model);

		// 1. Not in playing state
		assertFalse("cannot play card when not in the playing state",
				this.facade.canUseRoadBuilding(PlayerNumber.ONE));

		this.model.turnTracker.status = CatanState.PLAYING;
		this.facade.initializeModel(this.model);

		// 2. Not your turn
		assertFalse("cannot play card when it is not your turn",
				this.facade.canUseRoadBuilding(PlayerNumber.TWO));

		// 3. Cannot play when you just bought the card
		this.model.players[0].oldDevCards.roadBuilding = 0;
		this.model.players[0].newDevCards.roadBuilding = 1;
		this.facade.initializeModel(this.model);
		assertFalse("cannot play a card on the turn the card was bought",
				this.facade.canUseRoadBuilding(PlayerNumber.ONE));

		this.model.players[0].oldDevCards.roadBuilding = 2;
		this.model.players[0].newDevCards.roadBuilding = 0;
		this.facade.initializeModel(this.model);

		// 4. Can play when it is your turn
		assertTrue("can play road building when it is your turn",
				this.facade.canUseRoadBuilding(PlayerNumber.ONE));

		this.model.players[0].playedDevCard = true;
		this.facade.initializeModel(this.model);

		// 5. Cannot play when you've already played a card
		assertFalse("cannot play more than one card per turn",
				this.facade.canUseRoadBuilding(PlayerNumber.ONE));

		this.model.players[0].playedDevCard = false;
		this.model.players[0].roads = 1;
		this.facade.initializeModel(this.model);

		// 6. Cannot play when you don't have enough roads
		assertFalse("cannot play with only one available road",
				this.facade.canUseRoadBuilding(PlayerNumber.ONE));
	}

	@Test
	public void testCanUseSoldier() {
		//fail("Not yet implemented");
	}

	@Test
	public void testCanUseMonopoly() throws CatanException {
		this.model.turnTracker.status = CatanState.ROLLING;
		this.model.players[0].oldDevCards.roadBuilding = 1;
		this.facade.initializeModel(this.model);

		// 1. Not in playing state
		assertFalse("cannot play card when not in the playing state",
				this.facade.canUseMonopoly(PlayerNumber.ONE));

		this.model.turnTracker.status = CatanState.PLAYING;
		this.facade.initializeModel(this.model);

		// 2. Not your turn
		assertFalse("cannot play card when it is not your turn",
				this.facade.canUseMonopoly(PlayerNumber.TWO));

		// 3. Cannot play when you just bought the card
		this.model.players[0].oldDevCards.monopoly = 0;
		this.model.players[0].newDevCards.monopoly = 1;
		this.facade.initializeModel(this.model);
		assertFalse("cannot play a card on the turn the card was bought",
				this.facade.canUseMonopoly(PlayerNumber.ONE));

		this.model.players[0].oldDevCards.monopoly = 2;
		this.model.players[0].newDevCards.monopoly = 0;
		this.facade.initializeModel(this.model);

		// 4. Can play when it is your turn
		assertTrue("can play monopoly when it is your turn",
				this.facade.canUseMonopoly(PlayerNumber.ONE));

		this.model.players[0].playedDevCard = true;
		this.facade.initializeModel(this.model);

		// 5. Cannot play when you've already played a card
		assertFalse("cannot play more than one card per turn",
				this.facade.canUseMonopoly(PlayerNumber.ONE));
	}

	@Test
	public void testCanUseMonument() throws CatanException {
		this.model.turnTracker.status = CatanState.ROLLING;
		this.model.players[0].oldDevCards.monument = 1;
		this.facade.initializeModel(this.model);

		// 1. Not in playing state
		assertFalse("cannot play card when not in the playing state",
				this.facade.canUseMonument(PlayerNumber.ONE));

		this.model.turnTracker.status = CatanState.PLAYING;
		this.facade.initializeModel(this.model);

		// 2. Not your turn
		assertFalse("cannot play card when it is not your turn",
				this.facade.canUseMonument(PlayerNumber.TWO));

		// 3. Cannot play when you just bought the card
		this.model.players[0].oldDevCards.monument = 0;
		this.model.players[0].newDevCards.monument = 1;
		this.facade.initializeModel(this.model);
		assertFalse("cannot play a card on the turn the card was bought",
				this.facade.canUseMonument(PlayerNumber.ONE));

		this.model.players[0].oldDevCards.monument = 2;
		this.model.players[0].newDevCards.monument = 0;
		this.facade.initializeModel(this.model);

		// 4. Can play when it is your turn
		assertTrue("can play monopoly when it is your turn",
				this.facade.canUseMonument(PlayerNumber.ONE));

		this.model.players[0].playedDevCard = true;
		this.facade.initializeModel(this.model);

		// 5. Can play when you've already played another dev card
		assertTrue("can play more than one monument per turn",
				this.facade.canUseMonument(PlayerNumber.ONE));
	}

	@Test
	public void testCanRobPlayer() throws CatanException {
		this.model.turnTracker.status = CatanState.ROBBING;

		// 1. Test rob player with no cards, should fail
		TransportPlayer otherPlayer = new TransportPlayer();
		otherPlayer.resources = new TransportResources();
		otherPlayer.oldDevCards = new TransportOldDevCards();
		otherPlayer.newDevCards = new TransportNewDevCards();

		otherPlayer.playerIndex = PlayerNumber.TWO;
		otherPlayer.resources.brick = 0;
		otherPlayer.resources.wood = 0;
		otherPlayer.resources.sheep = 0;
		otherPlayer.resources.ore = 0;
		otherPlayer.resources.wheat = 0;
		otherPlayer.color = CatanColor.WHITE;
		otherPlayer.name = "OtherPlayer";
		otherPlayer.playerID = 1;

		this.model.players = new TransportPlayer[2];
		this.model.players[0] = this.player;
		this.model.players[1] = otherPlayer;
		this.facade.initializeModel(this.model);
		assertFalse(this.facade
				.canRobPlayer(PlayerNumber.ONE, PlayerNumber.TWO));

		// 2. Test rob player with one card, should succeed
		otherPlayer.resources.brick = 1;
		this.facade.initializeModel(this.model);
		assertTrue(this.facade.canRobPlayer(PlayerNumber.ONE, PlayerNumber.TWO));
	}

	@Test
	public void testCanPlaceRobber() throws CatanException {
		this.model.turnTracker.status = CatanState.ROBBING;
		this.model.map.robber = new TransportRobber();
		this.model.map.robber.x = 0;
		this.model.map.robber.y = 0;
		this.facade.initializeModel(this.model);
		// 1. Test place Robber on same location; should fail
		assertFalse(this.facade.canPlaceRobber(PlayerNumber.ONE, new HexLocation(0, 0)));
		// 2. Test place Rober on new location; should pass
		assertTrue(this.facade.canPlaceRobber(PlayerNumber.ONE, new HexLocation(0, 1)));
		// 3. Test place Robber on water; should fail
		assertFalse(this.facade.canPlaceRobber(PlayerNumber.ONE, new HexLocation(-3, 0)));


	}

}
