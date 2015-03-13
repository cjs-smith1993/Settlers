package clientBackend.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import shared.definitions.CatanColor;
import shared.definitions.PlayerNumber;
import shared.model.CatanException;
import shared.model.Player;
import shared.transport.TransportPlayer;

public class PlayerTest {
	TransportPlayer player;
	
	@Before
	public void setUp() throws Exception {
		player = new TransportPlayer();
		player.cities = 5;
		player.color = CatanColor.BROWN;
		player.monuments = 5;
		player.name = "Kalex";
		player.playerID = 3;
		player.playerIndex = PlayerNumber.ONE;
		player.roads = 8;
		player.settlements = 4;
		player.soldiers = 3;
		player.victoryPoints = 2;
	}

	@Test
	public void testGetRoad() throws CatanException {
		Player gamePlayer = new Player(player);
		
		assertEquals(gamePlayer.getNumRoads(), 8);
	}

	@Test
	public void testGetSettlement() throws CatanException {
		Player gamePlayer = new Player(player);
		
		assertEquals(gamePlayer.getNumSettlements(), 4);
	}

	@Test
	public void testGetCity() throws CatanException {
		Player gamePlayer = new Player(player);
		
		assertEquals(gamePlayer.getNumCities(), 5);
	}

}
