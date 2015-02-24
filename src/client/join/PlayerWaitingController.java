package client.join;

import java.util.Observable;
import java.util.Observer;

import client.base.*;
import clientBackend.model.Facade;

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
		this.getView().showModal();
	}

	@Override
	public void addAI() {
		// TEMPORARY
		this.getView().closeModal();
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

	}

}
