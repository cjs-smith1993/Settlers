package client.communication;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import shared.definitions.CatanColor;
import shared.definitions.PlayerNumber;
import client.base.*;
import clientBackend.model.Facade;
import clientBackend.model.Message;

/**
 * Chat controller implementation
 */
public class ChatController extends Controller implements IChatController, Observer {
	Facade facade = Facade.getInstance();

	public ChatController(IChatView view) {
		super(view);

		Facade.getInstance().addObserver(this);
	}

	@Override
	public IChatView getView() {
		return (IChatView) super.getView();
	}

	@Override
	public void sendMessage(String message) {
		this.facade.sendChat(this.facade.getClientPlayerIndex(), message);
	}

	@Override
	public void update(Observable o, Object arg) {
		List<Message> messages = this.facade.getMessages();
		List<LogEntry> messageEntries = new ArrayList<>();

		for (Message message : messages) {
			PlayerNumber player = this.facade.getPlayerNumberForName(message.getName());
			CatanColor color = this.facade.getPlayerColor(player);
			LogEntry entry = new LogEntry(color, message.getMessage());
			messageEntries.add(entry);
		}

		this.getView().setEntries(messageEntries);
	}
}