package client.frontend.communication;

import java.util.*;

import client.backend.ClientFacade;
import client.frontend.base.*;
import shared.definitions.*;
import shared.model.Message;

/**
 * Game history controller implementation
 */
public class GameHistoryController extends Controller implements IGameHistoryController, Observer {
	ClientFacade facade = ClientFacade.getInstance();

	public GameHistoryController(IGameHistoryView view) {
		super(view);
		ClientFacade.getInstance().addObserver(this);
	}

	@Override
	public IGameHistoryView getView() {
		return (IGameHistoryView) super.getView();
	}

	@Override
	public void update(Observable o, Object arg) {
		List<Message> logs = this.facade.getLog();
		List<LogEntry> logEntries = new ArrayList<>();

		for (Message log : logs) {
			PlayerNumber player = this.facade.getPlayerNumberForName(log.getName());
			CatanColor color = this.facade.getPlayerColor(player);
			LogEntry entry = new LogEntry(color, log.getMessage());
			logEntries.add(entry);
		}

		this.getView().setEntries(logEntries);
	}
}
