package clientBackend;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import client.backend.ClientFacade;
import client.backend.ServerPoller;
import client.serverCommunication.ServerException;
import client.serverCommunication.ServerInterface;
import client.serverCommunication.ServerMock5;
import shared.definitions.CatanColor;
import shared.definitions.PlayerNumber;

public class ServerPollerTest {

	ServerPoller poller;
	ServerInterface mock5;
	ClientFacade facade = ClientFacade.getInstance();

	@Before
	public void setUp() throws Exception {
		this.facade = ClientFacade.getInstance();
		this.mock5 = new ServerMock5();
		this.facade.setProxy(mock5);

		this.poller = new ServerPoller();

		this.facade.setProxy(this.mock5);
		this.facade.login("Pete", "pete");
		this.facade.joinGame(0, CatanColor.BLUE);
	}

	@Test
	public void testPoll() throws ServerException {
		this.poller.poll();
		int newVersion = this.facade.getVersion();
		assertEquals("version should be 1", 1, newVersion);

		this.facade.sendChat(PlayerNumber.ONE, "chat");

		this.poller.poll();
		newVersion = this.facade.getVersion();
		assertEquals("version should no longer be 1", 2, newVersion);
	}
}