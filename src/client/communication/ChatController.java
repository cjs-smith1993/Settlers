package client.communication;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import serverCommunication.ServerProxy;
import shared.definitions.CatanColor;
import client.base.*;
import clientBackend.model.Facade;
import clientBackend.model.Message;

/**
 * Chat controller implementation
 */
public class ChatController extends Controller implements IChatController, Observer {
	ServerProxy proxy = ServerProxy.getInstance();
	Facade facade = Facade.getInstance();
	
	public ChatController(IChatView view) {
		super(view);
		
		Facade.getInstance().addObserver(this);
	}

	@Override
	public IChatView getView() {
		return (IChatView)super.getView();
	}

	@Override
	public void sendMessage(String message) {
		proxy.movesSendChat(facade.getClientPlayer().getInteger(), message);
	}

	@Override
	public void update(Observable o, Object arg) {
		List<Message> messages = facade.getMessages();
		CatanColor color = facade.getPlayerColor(facade.getClientPlayer());
		
		List<LogEntry> messageEntries = new ArrayList<>();
		
		for (int i = 0; i < messages.size(); i++) {
			LogEntry entry = new LogEntry(color, messages.get(i).getMessage());
			messageEntries.add(entry);
		}
		
		getView().setEntries(messageEntries);
	}
}