package client.communication;

import serverCommunication.ServerException;
import serverCommunication.ServerProxy;
import client.base.*;
import clientBackend.model.Facade;


/**
 * Chat controller implementation
 */
public class ChatController extends Controller implements IChatController {
	ServerProxy proxy = ServerProxy.getInstance();
	Facade facade = Facade.getInstance();
	
	public ChatController(IChatView view) {
		
		super(view);
	}

	@Override
	public IChatView getView() {
		return (IChatView)super.getView();
	}

	@Override
	public void sendMessage(String message) {
		try {
			proxy.movesSendChat(facade.getClientPlayer().getInteger(), message);
		} catch (ServerException e) {
			e.printStackTrace();
		}
	}
}

