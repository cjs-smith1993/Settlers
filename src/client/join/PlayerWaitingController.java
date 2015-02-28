package client.join;

import java.util.Collection;
import java.util.Observable;
import java.util.Observer;

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

	public PlayerWaitingController(IPlayerWaitingView view) {
		super(view);
		this.facade = Facade.getInstance();
		this.facade.addObserver(this);
	}

	@Override
	public IPlayerWaitingView getView() {
		return (IPlayerWaitingView) super.getView();
	}

	@Override
	public void start() {
		int numPlayers = this.facade.getPlayers().size();
		if (numPlayers == this.MAX_PLAYERS) {
			return;
		}

		AIType[] AITypes = this.facade.getAITypes().toArray(new AIType[0]);
		String[] stringTypes = new String[AITypes.length];
		for (int i = 0; i < AITypes.length; i++) {
			stringTypes[i] = AITypes[i].name();
		}
		this.getView().setAIChoices(stringTypes);
		this.getView().showModal();
	}

	@Override
	public void addAI() {
		this.getView().closeModal();

		String type = this.getView().getSelectedAI();
		boolean success = this.facade.addAI(AIType.valueOf(type));
		if (success) {
			int numPlayers = this.facade.getPlayers().size();
			if (numPlayers < this.MAX_PLAYERS) {
				this.getView().showModal();
			}
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		Collection<PlayerInfo> players = this.facade.getPlayers();
		this.getView().setPlayers(players.toArray(new PlayerInfo[0]));
		if (this.getView().isModalShowing() && players.size() >= this.MAX_PLAYERS) {
			this.getView().closeModal();
		}
	}
}
