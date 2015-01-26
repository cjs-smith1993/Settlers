package clientBackend;

import java.util.Timer;

/**
 * Uses a timer to poll the server on a fixed interval and update the model
 * when there is new information
 */
public class ServerPoller {
	private Timer timer;
	private ServerInterface server;
	
	/**
	 *  A Server Interface is passed in to provide dependency injection
	 * */
	public ServerPoller(ServerInterface server) {
		this.server = server;
	}
	
	/**
	 * Polls the server proxy with the current version number
	 * @param version the current version number
	 */
	public void poll(int version) {
		
	}
}
