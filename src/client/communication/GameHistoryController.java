package client.communication;

import java.util.*;

import client.base.*;
import clientBackend.model.Facade;
import clientBackend.model.Message;
import shared.definitions.*;

/**
 * Game history controller implementation
 */
public class GameHistoryController extends Controller implements IGameHistoryController, Observer {
	Facade facade = Facade.getInstance();

	public GameHistoryController(IGameHistoryView view) {
		super(view);
		Facade.getInstance().addObserver(this);
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
