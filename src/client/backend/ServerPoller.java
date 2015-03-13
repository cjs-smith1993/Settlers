package client.backend;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Uses a timer to poll the server on a fixed interval and update the model when
 * there is new information
 */
public class ServerPoller {
	private ClientFacade facade;
	private boolean hasStartedPolling = false;
	private Timer timer;

	/**
	 * A Server Interface is passed in to provide dependency injection
	 */
	public ServerPoller() {
		this.facade = ClientFacade.getInstance();
		this.timer = new Timer();
	}

	public void initializeTimer() {
		if (!this.hasStartedPolling) {
			//			this.facade.setPoller(this);
			this.hasStartedPolling = true;

			this.timer.schedule(
					new TimerTask() {
						@Override
						public void run() {
							ServerPoller.this.poll();
						}
					}, 0, 500); // Period delayed is in milliseconds. (e.g. 3000 ms = 3 sec)
		}
	}

	/**
	 * Polls the server proxy with the current version number
	 */
	public void poll() {
		if (this.facade.getGame() == null) {
			return;
		}

		this.facade.getModel(true);
	}

	public void killPoller() {
		this.hasStartedPolling = false;
		this.timer.cancel();
	}
}
