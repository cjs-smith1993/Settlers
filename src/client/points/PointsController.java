package client.points;

import java.util.Observable;
import java.util.Observer;

import client.base.*;
import clientBackend.model.Facade;

/**
 * Implementation for the points controller
 */
public class PointsController extends Controller implements IPointsController, Observer {
	private IGameFinishedView finishedView;
	private Facade facade = Facade.getInstance();

	/**
	 * PointsController constructor
	 *
	 * @param view
	 *            Points view
	 * @param finishedView
	 *            Game finished view, which is displayed when the game is over
	 */
	public PointsController(IPointsView view, IGameFinishedView finishedView) {
		super(view);

		this.setFinishedView(finishedView);
		this.facade.addObserver(this);
	}

	public IPointsView getPointsView() {
		return (IPointsView) super.getView();
	}

	public IGameFinishedView getFinishedView() {
		return this.finishedView;
	}

	public void setFinishedView(IGameFinishedView finishedView) {
		this.finishedView = finishedView;
	}

	@Override
	public void update(Observable o, Object arg) {
		int score = this.facade.getPlayerScore(this.facade.getClientPlayerIndex());
		this.getPointsView().setPoints(Math.min(score, 10));

		String winner = this.facade.getWinner();

		if (winner != null) {
			boolean isLocalPlayer = false;

			if (this.facade.getNameForPlayerNumber(this.facade.getClientPlayerIndex()).equals(
					winner)) {
				isLocalPlayer = true;
			}

			this.getFinishedView().setWinner(winner, isLocalPlayer);

			if (!this.getFinishedView().isModalShowing() && this.facade.getWinnerID() != -1) {
				this.getFinishedView().showModal();
			}

			this.getFinishedView().setController(this);
		}
	}

	@Override
	public void endGame() {
		this.facade.setGameFinished(true);
		this.facade.setGameReady(false);
		this.facade.setHasFinishedFirstRound(false);
		this.facade.setHasFinishedSecondRound(false);
		this.facade.forceNotifyOberservers();
		System.out.println("END OF GAME: Forced notifyObservers()");
	}
}
