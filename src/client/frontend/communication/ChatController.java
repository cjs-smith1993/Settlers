package client.frontend.communication;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import client.backend.ClientFacade;
import client.frontend.base.*;
import shared.definitions.CatanColor;
import shared.definitions.PlayerNumber;
import shared.model.Message;

/**
 * Chat controller implementation
 */
public class ChatController extends Controller implements IChatController, Observer {
	ClientFacade facade = ClientFacade.getInstance();

	public ChatController(IChatView view) {
		super(view);

		ClientFacade.getInstance().addObserver(this);
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