package clientBackend.transport;

import java.util.List;

public class TransportModel {
	public TransportDeck deck;
	public TransportMap map;
	public List<TransportPlayer> players;
	public TransportLog log;
	public TransportChat chat;
	public TransportBank bank;
	public TransportTurnTracker turnTracker;
	public int winner;
	public int version;
}
