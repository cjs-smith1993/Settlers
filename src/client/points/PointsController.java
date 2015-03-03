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
		facade.addObserver(this);
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
		this.getPointsView().setPoints(score);
		
		String winner = facade.getWinner();
		
		if (winner != null) {
			boolean isLocalPlayer = false;
			
			if (facade.getNameForPlayerNumber(facade.getClientPlayerIndex()).equals(winner)) {
				isLocalPlayer = true;
			}
			
			getFinishedView().setController(this);
			getFinishedView().setWinner(winner, isLocalPlayer);
			getFinishedView().showModal();
		}
	}

	@Override
	public void endGame() {
		facade.initializeArmageddon();
	}
}
