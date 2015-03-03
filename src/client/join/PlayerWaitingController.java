package client.join;

import java.util.Collection;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import shared.definitions.AIType;
import client.base.*;
import client.data.PlayerInfo;
import clientBackend.model.Facade;

/**
 * Implementation for the player waiting controller
 */
public class PlayerWaitingController extends Controller implements IPlayerWaitingController,
		Observer {

	private Facade facade;
	private final int MAX_PLAYERS = 4;
	boolean isPolling = false;
	private Timer timer;

	public PlayerWaitingController(IPlayerWaitingView view) {
		super(view);
		this.facade = Facade.getInstance();
		this.facade.addObserver(this);

		this.timer = new Timer();
	}

	@Override
	public IPlayerWaitingView getView() {
		return (IPlayerWaitingView) super.getView();
	}

	@Override
	public void start() {
		int numPlayers = this.facade.getPlayers().size();
		if (numPlayers == this.MAX_PLAYERS) {
			this.finish();
			return;
		}

		this.getView().showModal();
		this.setAIChoices();

		if (!this.isPolling) {
			this.isPolling = true;

			this.timer.schedule(new TimerTask() {
				@Override
				public void run() {
					PlayerWaitingController.this.facade.getModel(false);
				}
			}, 0, 3000); // Period delayed is in milliseconds. (e.g. 3000 ms = 3 sec)
		}
	}

	@Override
	public void addAI() {
		String type = this.getView().getSelectedAI();
		this.facade.addAI(AIType.valueOf(type));
		this.facade.getModel(false);
		this.refresh();
	}

	public void refresh() {
		System.out.println("refreshing");
		if (this.getView().isModalShowing()) {
			Collection<PlayerInfo> players = this.facade.getPlayers();
			if (players.size() < this.MAX_PLAYERS) {
				this.getView().closeModal();
				this.getView().setPlayers(players.toArray(new PlayerInfo[0]));
				this.getView().showModal();
			}
			else {
				this.finish();
			}
		}
	}

	public void setAIChoices() {
		if (this.getView().isModalShowing()) {
			AIType[] AITypes = this.facade.getAITypes().toArray(new AIType[0]);
			String[] stringTypes = new String[AITypes.length];
			for (int i = 0; i < AITypes.length; i++) {
				stringTypes[i] = AITypes[i].name();
			}
			this.getView().closeModal();
			this.getView().setAIChoices(stringTypes);
			this.getView().showModal();
		}
	}

	public void finish() {
		this.timer.cancel();
		if (this.getView().isModalShowing()) {
			this.getView().closeModal();
		}
		this.facade.setGameReady(true);
		this.facade.getModel(false);
	}

	@Override
	public void update(Observable o, Object arg) {
		this.refresh();
	}
}
