package shared.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import shared.transport.TransportChat;
import shared.transport.TransportLine;
import shared.transport.TransportLog;

/**
 * Holding class for the message and the log
 *
 */

public class PostOffice {
	private ArrayList<Message> chat = new ArrayList<>();
	private ArrayList<Message> log = new ArrayList<>();

	public PostOffice() {
		chat = new ArrayList<Message>();
		log = new ArrayList<Message>();		
	}
	
	public PostOffice(Collection<TransportLine> chat, Collection<TransportLine> log) {
		ArrayList<TransportLine> newChats = new ArrayList<>(chat);
		ArrayList<TransportLine> newLogs = new ArrayList<>(log);
		
		generateMessages(newChats, this.chat);
		generateMessages(newLogs, this.log);
	}
	
	public TransportChat getTransportChat() {
		TransportChat transportChat = new TransportChat();
		transportChat.lines = getTransportLines(chat);
		
		return transportChat;
	}
	
	public TransportLog getTransportLog() {
		TransportLog transportLog = new TransportLog();
		transportLog.lines = getTransportLines(log);
		
		return transportLog;
	}
	
	private TransportLine[] getTransportLines(ArrayList<Message> messages) {
		ArrayList<TransportLine> lines = new ArrayList<>();
		
		for (Message message : messages) {
			lines.add(new TransportLine(message.getName(), message.getMessage()));
		}
		
		return (TransportLine[])lines.toArray();
	}
	
	private void generateMessages(List<TransportLine> newCommunications, List<Message> existingCommunications) {
		for (int i = 0; i < newCommunications.size(); i++) {
			Message message = new Message(newCommunications.get(i).source, newCommunications.get(i).message);
			existingCommunications.add(message);
		}
	}

	/**
	 * @return the chat
	 */
	public List<Message> getMessages() {
		return chat;
	}

	/**
	 * @return the log
	 */
	public List<Message> getLog() {
		return log;
	}	
}
