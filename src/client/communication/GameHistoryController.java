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
		return (IGameHistoryView)super.getView();
	}
	
	@Override
	public void update(Observable o, Object arg) {
		List<Message> logs = facade.getLog();
		CatanColor color = facade.getPlayerColor(facade.getClientPlayer());
		
		List<LogEntry> logEntries = new ArrayList<>();
		
		for (int i = 0; i < logs.size(); i++) {
			LogEntry entry = new LogEntry(color, logs.get(i).getMessage());
			logEntries.add(entry);
		}
		
		getView().setEntries(logEntries);
	}
}

