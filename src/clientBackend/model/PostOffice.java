package clientBackend.model;

import java.util.ArrayList;
import java.util.List;

import clientBackend.transport.TransportLine;

/**
 * Holding class for the message and the log
 *
 */

public class PostOffice {
	private List<Message> chat;
	private List<Message> log;

	public PostOffice() {
		chat = new ArrayList<Message>();
		log = new ArrayList<Message>();		
	}
	
	public PostOffice(List<TransportLine> chat, List<TransportLine> log) {
		generateMessages(chat, this.chat);
		generateMessages(log, this.chat);
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
	public List<Message> getChat() {
		return chat;
	}

	/**
	 * @return the log
	 */
	public List<Message> getLog() {
		return log;
	}	
}
