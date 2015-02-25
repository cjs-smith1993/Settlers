package client.join;

import java.util.Observable;
import java.util.Observer;

import shared.definitions.AIType;
import client.base.*;
import client.data.PlayerInfo;
import clientBackend.model.Facade;
import clientBackend.model.Player;

/**
 * Implementation for the player waiting controller
 */
public class PlayerWaitingController extends Controller implements IPlayerWaitingController,
		Observer {

	private Facade facade;

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
		String type = this.getView().getSelectedAI();
		boolean success = this.facade.addAI(AIType.valueOf(type));
		if (success) {
			int numPlayers = this.facade.getPlayers().size();
			if (numPlayers >= 4) {
				this.getView().closeModal();
			}
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		Player[] players = this.facade.getPlayers().values().toArray(new Player[0]);
		PlayerInfo[] playersInfo = new PlayerInfo[players.length];
		for (int i = 0; i < players.length; i++) {
			Player player = players[i];
			PlayerInfo info = new PlayerInfo();
			info.setId(player.getUser().getUserId());
			info.setPlayerIndex(player.getNumber());
			info.setName(player.getUser().getName());
			info.setColor(player.getColor());
			playersInfo[i] = info;
		}

		this.getView().setPlayers(playersInfo);
	}
}
