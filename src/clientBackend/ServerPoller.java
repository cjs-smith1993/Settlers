package clientBackend;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import serverCommunication.ServerException;
import serverCommunication.ServerInterface;
import clientBackend.model.Facade;

/**
 * Uses a timer to poll the server on a fixed interval and update the model when
 * there is new information
 */
public class ServerPoller {
	private ServerInterface server;
	private Facade facade;
	private boolean hasStartedPolling = false;

	/**
	 * A Server Interface is passed in to provide dependency injection
	 */
	public ServerPoller(ServerInterface server) {
		this.server = server;
		this.facade = Facade.getInstance();
	}

	public void initializeTimer() {
		if (!this.hasStartedPolling) {
			this.hasStartedPolling = true;

			new Timer().schedule(
					new TimerTask() {
						@Override
						public void run() {
							System.out.println("SERVER_POLLER: Proxy polled.");
							ServerPoller.this.poll();
						}
					}, 0, 3000); // Period delayed is in milliseconds. (e.g. 3000 ms = 3 sec)
		}
	}

	/**
	 * Polls the server proxy with the current version number
	 */
	public void poll() {
		if (this.facade.getGame() == null) {
			return;
		}

		final int versionNumber = this.facade.getVersion();

		try {
			this.server.gameModel(versionNumber);
		} catch (IOException | ServerException e) {
			String error = "\n------------\nERROR: Server Poller is having issues.\n------------\n";
			System.out.println(error);
			if (e instanceof ServerException) {
				System.out.println(e.toString());
			}
			e.printStackTrace();
		}
	}
}
