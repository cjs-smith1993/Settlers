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
	public ServerPoller() {
		this.facade = Facade.getInstance();
	}

	public void initializeTimer() {
		if (!this.hasStartedPolling) {
			this.hasStartedPolling = true;

			new Timer().schedule(
					new TimerTask() {
						@Override
						public void run() {
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

		this.facade.getModel(true);
	}
}
