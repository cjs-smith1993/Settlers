package client.frontend.join;

import java.util.Collection;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import client.backend.ClientModelFacade;
import client.frontend.base.*;
import client.frontend.data.PlayerInfo;
import shared.definitions.AIType;

/**
 * Implementation for the player waiting controller
 */
public class PlayerWaitingController extends Controller implements IPlayerWaitingController,
		Observer {

	private ClientModelFacade facade;
	private final int MAX_PLAYERS = 4;
	boolean isPolling = false;
	private Timer timer;

	public PlayerWaitingController(IPlayerWaitingView view) {
		super(view);
		this.facade = ClientModelFacade.getInstance();
		this.facade.addObserver(this);
	}

	@Override
	public IPlayerWaitingView getView() {
		return (IPlayerWaitingView) super.getView();
	}

	@Override
	public void start() {
		this.timer = new Timer();

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
		if (this.getView().isModalShowing() && this.facade.getAITypes() != null) {
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
		this.isPolling = false;
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
