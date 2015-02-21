package clientBackend;

import java.util.Timer;
import java.util.TimerTask;

import serverCommunication.ServerException;
import serverCommunication.ServerInterface;
import serverCommunication.ServerProxy;
import clientBackend.model.Facade;

/**
 * Uses a timer to poll the server on a fixed interval and update the model when
 * there is new information
 */
public class ServerPoller {
	private static ServerPoller poller;
	private ServerInterface server;
	private Facade facade;

	/**
	 * A Server Interface is passed in to provide dependency injection
	 */
	private ServerPoller(ServerInterface server, Facade facade) {
		this.server = server;
		this.facade = facade;
	}
	
	public static ServerPoller getInstance() {
		if (poller == null) {
			ServerProxy proxy = ServerProxy.getInstance();
			Facade facade = Facade.getInstance();
			
			poller = new ServerPoller(proxy, facade);
		}
		
		return poller;
	}

	public void initializeTimer() {
		new Timer().schedule(
				new TimerTask() {
					@Override
					public void run() {
						ServerPoller.this.poll();
					}
				}, 0, 3000); // Period delayed is in milliseconds. (e.g. 3000 ms = 3 sec)
	}

	/**
	 * Polls the server proxy with the current version number
	 */
	private void poll() {
		final int versionNumber = this.facade.getVersion();

		try {
			this.server.gameModel(versionNumber);
		} catch (ServerException e) {
			System.out
					.println("\n------------\nERROR: Server Poller is having issues.\n------------\n");
		}
	}
}
