package clientBackend;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import serverCommunication.ServerException;
import serverCommunication.ServerInterface;
import serverCommunication.ServerMock5;
import shared.definitions.CatanColor;
import shared.definitions.PlayerNumber;
import clientBackend.ServerPoller;
import clientBackend.model.Facade;

public class ServerPollerTest {

	ServerPoller poller;
	ServerInterface mock5;
	Facade facade = Facade.getInstance();

	@Before
	public void setUp() throws Exception {
		this.facade = Facade.getInstance();
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