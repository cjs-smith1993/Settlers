package client.roll;

import java.util.Observable;
import java.util.Observer;

import shared.definitions.CatanState;
import client.base.*;
import clientBackend.model.CatanException;
import clientBackend.model.Facade;

/**
 * Implementation for the roll controller
 */
public class RollController extends Controller implements IRollController, Observer {

	private IRollResultView resultView;
	private Facade facade;

	/**
	 * RollController constructor
	 *
	 * @param view
	 *            Roll view
	 * @param resultView
	 *            Roll result view
	 */
	public RollController(IRollView view, IRollResultView resultView) {
		super(view);
		this.setResultView(resultView);

		this.facade = Facade.getInstance();
		this.facade.addObserver(this);
	}

	public IRollResultView getResultView() {
		return this.resultView;
	}

	public void setResultView(IRollResultView resultView) {
		this.resultView = resultView;
	}

	public IRollView getRollView() {
		return (IRollView) this.getView();
	}

	@Override
	public void rollDice() {
		int value;
		try {
			value = this.facade.rollNumber(this.facade.getClientPlayer());
			if (value > 0) {
				this.getResultView().setRollValue(value);
				this.getResultView().showModal();
			}
		} catch (CatanException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		boolean isRolling = this.facade.getModelState() == CatanState.ROLLING;
		boolean myTurn = this.facade.isClientTurn();
		if (myTurn && isRolling && !this.getRollView().isModalShowing()) {
			this.getRollView().showModal();
		}
	}

}
