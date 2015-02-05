package clientBackend;

import java.util.Timer;
import java.util.TimerTask;

import clientBackend.model.Facade;

/**
 * Uses a timer to poll the server on a fixed interval and update the model
 * when there is new information
 */
public class ServerPoller {
	private ServerInterface server;
	private Facade facade;
	
	/**
	 *  A Server Interface is passed in to provide dependency injection
	 */
	public ServerPoller(ServerInterface server, Facade facade) {
		this.server = server;
		this.facade = facade;
	}
	
	private void initializeTimer() {
		new Timer().schedule(
				new TimerTask() {
					@Override
					public void run() {
						poll();
					}
				}, 0, 3000); // Period delayed is in milliseconds. (e.g. 3000 ms = 3 sec)
	}
	
	/**
	 * Polls the server proxy with the current version number
	 */
	public void poll() {
		final int versionNumber = facade.getVersion();
		
		try {
			server.gameModel(versionNumber);	
		} 
		catch (ServerException e) {
			System.out.println("\n------------\nERROR: Server Poller is having issues.\n------------\n");
		}
	}
}
