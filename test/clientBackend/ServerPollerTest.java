package clientBackend;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import serverCommunication.ServerException;
import serverCommunication.ServerInterface;
import serverCommunication.ServerMock5;
import clientBackend.model.Facade;

public class ServerPollerTest {

	ServerPoller poller;
	ServerInterface mock5;
	Facade facade;

	@Before
	public void setUp() throws Exception {
		this.facade = new Facade();
		this.mock5 = new ServerMock5(this.facade);
		this.poller = new ServerPoller(this.mock5, this.facade);
	}

	@Test
	public void testPoll() throws ServerException {
		this.poller.poll();
		int newVersion = this.facade.getVersion();
		assertEquals("version should be 2", 2, newVersion);

		this.poller.poll();
		newVersion = this.facade.getVersion();
		assertEquals("version should no longer be 3", 3, newVersion);

	}
}
