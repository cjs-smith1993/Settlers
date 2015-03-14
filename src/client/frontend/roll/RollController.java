package client.frontend.roll;

import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import client.backend.ClientModelFacade;
import client.frontend.base.*;
import shared.definitions.CatanState;
import shared.model.CatanException;

/**
 * Implementation for the roll controller
 */
public class RollController extends Controller implements IRollController, Observer {

	private IRollResultView resultView;
	private ClientModelFacade facade;
	private Timer timer;
	private final boolean TIMER_ENABLED = true;
	private final int TIMER_LENGTH = 30 + 1;
	private boolean timerStarted = false;
	private int timeLeft;

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

		this.facade = ClientModelFacade.getInstance();
		this.facade.addObserver(this);
		this.timer = new Timer();
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
		this.stopTimerAndRoll();
	}

	public void startTimer() {
		if (!this.TIMER_ENABLED || this.timerStarted) {
			return;
		}

		this.timer = new Timer();
		this.timerStarted = true;
		this.timeLeft = this.TIMER_LENGTH;
		this.timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (--RollController.this.timeLeft == 0) {
					RollController.this.stopTimerAndRoll();
				}
				else {
					String message = "Rolling automatically in " + RollController.this.timeLeft;
					RollController.this.getRollView().setMessage(message);
				}

			}
		}, 0, 1000);
	}

	public void stopTimerAndRoll() {
		this.timer.cancel();
		this.timerStarted = false;

		if (this.getRollView().isModalShowing()) {
			this.getRollView().closeModal();
		}

		int value;
		try {
			value = this.facade.rollNumber(this.facade.getClientPlayerIndex());
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
			this.startTimer();
		}
	}

}
