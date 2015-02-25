package client.join;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Observable;
import java.util.Observer;

import shared.definitions.CatanColor;
import client.base.*;
import client.data.*;
import client.misc.*;
import clientBackend.dataTransportObjects.DTOGame;
import clientBackend.dataTransportObjects.DTOPlayer;
import clientBackend.model.CatanException;
import clientBackend.model.Facade;

/**
 * Implementation for the join game controller
 */
public class JoinGameController extends Controller implements IJoinGameController, Observer {

	private INewGameView newGameView;
	private ISelectColorView selectColorView;
	private IMessageView messageView;
	private IAction joinAction;
	private Facade facade;
	
	//these are variables I made
	private int localPlayerId = -1;
	private int gameId = -1;
	private GameInfo gameInfo;

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

		this.facade = Facade.getInstance();
		this.facade.addObserver(this);
		this.setNewGameView(newGameView);
		this.setSelectColorView(selectColorView);
		this.setMessageView(messageView);
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
		JoinGameView view = (JoinGameView) super.getView();
		Collection<DTOGame> games = this.facade.getGamesList();
		PlayerInfo playerInfo = new PlayerInfo();
		//setlocalPlayerId as well
		localPlayerId = 10;
		playerInfo.setId(localPlayerId);
		//need to find the serverID of the player 
		//that is what is used for the check of re-join
		Collection<GameInfo> gameInfo = new ArrayList<GameInfo>();

		for (DTOGame game : games) {
			GameInfo newGame = new GameInfo();

			newGame.setId(game.id);
			newGame.setTitle(game.title);
			for (DTOPlayer player : game.players) {
				PlayerInfo newPlayer = new PlayerInfo();

				newPlayer.setId(player.id);
				newPlayer.setName(player.name);
				newPlayer.setColor(player.color);
				if (newPlayer.getName() != null) {
					newGame.addPlayer(newPlayer);
				}
			}
			gameInfo.add(newGame);
		}

		GameInfo[] information = gameInfo.toArray(new GameInfo[0]);
		view.setGames(information, playerInfo);

		this.getJoinGameView().showModal();
	}

	@Override
	public void startCreateNewGame() {
		this.getNewGameView().showModal();
	}

	@Override
	public void cancelCreateNewGame() {
		this.getNewGameView().closeModal();
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
			this.getNewGameView().closeModal();
			this.start();
		} catch (CatanException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void startJoinGame(GameInfo game) {
		gameId = game.getId();
		gameInfo = game;
		for(PlayerInfo info:game.getPlayers()) {
			CatanColor color = info.getColor();
			if(info.getId() != localPlayerId) {
				this.getSelectColorView().setColorEnabled(color, false);
			}
		}
		this.getSelectColorView().showModal();
	}

	@Override
	public void cancelJoinGame() {
		this.getJoinGameView().closeModal();
	}

	@Override
	public void joinGame(CatanColor color) {
		// If join succeeded
		if(gameId != -1)
			facade.joinGame(gameId, color);
		
		//this is a place that we could start the poller.
		this.getSelectColorView().closeModal();
		this.getJoinGameView().closeModal();
		this.joinAction.execute();
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

	}

}
