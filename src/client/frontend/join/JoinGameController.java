package client.frontend.join;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import client.backend.ClientModelFacade;
import client.frontend.base.*;
import client.frontend.data.*;
import client.frontend.misc.*;
import shared.dataTransportObjects.DTOGame;
import shared.dataTransportObjects.DTOPlayer;
import shared.definitions.CatanColor;
import shared.definitions.PlayerNumber;
import shared.model.CatanException;

/**
 * Implementation for the join game controller
 */
public class JoinGameController extends Controller implements IJoinGameController, Observer {

	private INewGameView newGameView;
	private ISelectColorView selectColorView;
	private IMessageView messageView;
	private IAction joinAction;

	private ClientModelFacade facade;
	private GameInfo curGame;
	boolean isPolling = false;
	private Timer timer;

	/**
	 * JoinGameController constructor
	 *
	 * @param view
	 *            Join game view
	 * @param newGameView
	 *            New game view
	 * @param selectColorView
	 *            Select color view
	 * @param messageView
	 *            Message view (used to display error messages that occur while
	 *            the user is joining a game)
	 */
	public JoinGameController(IJoinGameView view, INewGameView newGameView,
			ISelectColorView selectColorView, IMessageView messageView) {

		super(view);

		this.facade = ClientModelFacade.getInstance();
		this.setNewGameView(newGameView);
		this.setSelectColorView(selectColorView);
		this.setMessageView(messageView);

		this.facade.addObserver(this);
	}

	public IJoinGameView getJoinGameView() {
		return (IJoinGameView) super.getView();
	}

	/**
	 * Returns the action to be executed when the user joins a game
	 *
	 * @return The action to be executed when the user joins a game
	 */
	public IAction getJoinAction() {
		return this.joinAction;
	}

	/**
	 * Sets the action to be executed when the user joins a game
	 *
	 * @param value
	 *            The action to be executed when the user joins a game
	 */
	public void setJoinAction(IAction value) {
		this.joinAction = value;
	}

	public INewGameView getNewGameView() {
		return this.newGameView;
	}

	public void setNewGameView(INewGameView newGameView) {
		this.newGameView = newGameView;
	}

	public ISelectColorView getSelectColorView() {
		return this.selectColorView;
	}

	public void setSelectColorView(ISelectColorView selectColorView) {
		this.selectColorView = selectColorView;
	}

	public IMessageView getMessageView() {
		return this.messageView;
	}

	public void setMessageView(IMessageView messageView) {
		this.messageView = messageView;
	}

	@Override
	public void start() {
		this.getJoinGameView().showModal();
		this.refreshGamesList();

		if (!this.isPolling) {
			this.isPolling = true;
			this.timer = new Timer();
			this.timer.schedule(new TimerTask() {
				@Override
				public void run() {
					JoinGameController.this.update();
				}
			}, 0, 3000); // Period delayed is in milliseconds. (e.g. 3000 ms = 3 sec)
		}

	}

	@Override
	public void startCreateNewGame() {
		this.getJoinGameView().closeModal();
		this.getNewGameView().showModal();
	}

	@Override
	public void cancelCreateNewGame() {
		this.getNewGameView().closeModal();
		this.getJoinGameView().showModal();
	}

	@Override
	public void createNewGame() {
		boolean randomTiles;
		boolean randomNumbers;
		boolean randomPorts;
		String gameName;

		randomTiles = this.newGameView.getRandomlyPlaceHexes();
		randomNumbers = this.newGameView.getRandomlyPlaceNumbers();
		randomPorts = this.newGameView.getUseRandomPorts();
		gameName = this.newGameView.getTitle();

		try {
			this.facade.createGame(randomTiles, randomNumbers, randomPorts, gameName);
			this.createGameReset();
			this.getNewGameView().closeModal();
			this.getJoinGameView().showModal();
			this.refreshGamesList();
		} catch (CatanException e) {
			e.printStackTrace();
		}
	}

	private void createGameReset() {
		this.getNewGameView().setRandomlyPlaceHexes(false);
		this.getNewGameView().setRandomlyPlaceNumbers(false);
		this.getNewGameView().setUseRandomPorts(false);
		this.getNewGameView().setTitle("");
	}

	@Override
	public void startJoinGame(GameInfo game) {
		this.curGame = game;
		this.getJoinGameView().closeModal();
		this.getSelectColorView().showModal();
		this.refreshColorPicker();
	}

	@Override
	public void cancelJoinGame() {
		this.getSelectColorView().closeModal();
		this.getJoinGameView().showModal();
	}

	@Override
	public void joinGame(CatanColor color) {
		if (this.getSelectColorView().isModalShowing()) {
			this.getSelectColorView().closeModal();
		}

		boolean success = this.facade.joinGame(this.curGame.getId(), color);

		if (success) {
			this.facade.setGameFinished(false);
			this.timer.cancel();
			this.joinAction.execute();
		}
		else {
			this.getSelectColorView().showModal();
		}
	}

	public void refreshGamesList() {
		if (this.getJoinGameView().isModalShowing()) {

			Collection<DTOGame> gamesList = this.facade.getGamesList();
			Collection<GameInfo> gameInfoList = new ArrayList<GameInfo>();

			for (DTOGame game : gamesList) {
				GameInfo curGame = new GameInfo();

				curGame.setId(game.id);
				curGame.setTitle(game.title);

				for (DTOPlayer player : game.players) {
					if (player.id == -1) {
						continue;
					}

					int id = player.id;
					String name = player.name;
					CatanColor color = player.color;
					PlayerNumber index = PlayerNumber.BANK;

					PlayerInfo curPlayer = new PlayerInfo(id, index, name, color);
					curGame.addPlayer(curPlayer);
				}
				gameInfoList.add(curGame);
			}

			GameInfo[] gameInfoArray = gameInfoList.toArray(new GameInfo[0]);
			this.getJoinGameView().closeModal();
			this.getJoinGameView().setGames(gameInfoArray, this.facade.getClientPlayer());
			this.getJoinGameView().showModal();
		}
	}

	public void refreshColorPicker() {
		if (this.getSelectColorView().isModalShowing()) {
			for (DTOGame game : this.facade.getGamesList()) {
				if (game.id == this.curGame.getId()) {
					this.curGame = new GameInfo();
					this.curGame.setId(game.id);
					this.curGame.setTitle(game.title);

					for (DTOPlayer player : game.players) {
						if (player.id == -1) {
							continue;
						}

						int id = player.id;
						PlayerNumber index = PlayerNumber.BANK;
						String name = player.name;
						CatanColor color = player.color;

						PlayerInfo curPlayer = new PlayerInfo(id, index, name, color);
						this.curGame.addPlayer(curPlayer);
					}
				}

			}

			this.getSelectColorView().closeModal();
			this.disableTakenColors();
			this.getSelectColorView().showModal();
		}
	}

	public void disableTakenColors() {
		for (CatanColor color : CatanColor.values()) {
			this.getSelectColorView().setColorEnabled(color, true);
		}
		for (PlayerInfo player : this.curGame.getPlayers()) {
			CatanColor color = player.getColor();
			boolean taken = player.getId() != this.facade.getClientPlayer().getId();
			if (taken) {
				this.getSelectColorView().setColorEnabled(color, false);
			}
		}
	}

	public void update() {
		this.refreshGamesList();
		this.refreshColorPicker();
	}

	@Override
	public void update(Observable o, Object arg) {
		if (this.facade.hasGameFinished() && this.facade.getWinnerID() != -1) {
			this.facade.setWinnerID(-1);
			this.facade.setGameFinished(false);
			this.isPolling = false;
			this.start();
		}
	}
}
