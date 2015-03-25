package server.core;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import shared.definitions.PlayerNumber;
import shared.model.CatanException;
import shared.model.Message;

public class ServerModelFacadeTest {
	private ServerModelFacade facade;
	private String cleanFile = "save/cleanGame.txt";
	
	@Before
	public void setUp() throws Exception {
		this.facade = new ServerModelFacade(false, false, false);
		this.facade.initializeModelFromFile(cleanFile);
		
		
	}
	
	@Test
	public void testSendChat() {
		List<Message> messages = this.facade.getMessages();
		assertEquals(messages.size(), 0);
		this.facade.sendChat(PlayerNumber.ONE, "Test chat");
		assertEquals(messages.size(), 1);
		
	}

}
