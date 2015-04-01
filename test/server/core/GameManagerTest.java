package server.core;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import shared.dataTransportObjects.DTOGame;
import shared.definitions.CatanColor;
import shared.model.CatanException;
import shared.model.ModelUser;

public class GameManagerTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreateGame() {
		try {
			DTOGame game = GameManager.getInstance().createGame(false, false, false, "the first game");
			assertTrue(game.title.equals( "the first GAme"));
			
			assertTrue(GameManager.getInstance().authenticateGame(game.id));
		} catch (CatanException e) {
			e.printStackTrace();
			assertFalse(true);
		}
		
	}
	
	@Test
	public void testJoinGame() {
		GameManager man = GameManager.getInstance();
		ModelUser user = new ModelUser("Kevin", 0);
		
		try {
			man.createGame(false, false, false, "the first GAme");
			man.joinGame(0, CatanColor.BLUE, user);
			man.joinGame(0, CatanColor.BLUE, user);
			man.joinGame(0, CatanColor.GREEN, user);
		} catch (CatanException e) {}
	}

}
