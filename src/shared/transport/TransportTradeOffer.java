package shared.transport;

import shared.definitions.PlayerNumber;

public class TransportTradeOffer {
	public PlayerNumber sender;
	public PlayerNumber receiver;
	public TransportOffer offer;
	public TransportTradeOffer() {
		offer = new TransportOffer();
	}
}
