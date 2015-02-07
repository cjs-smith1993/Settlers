package clientBackend.dataTransportObjects;

import shared.definitions.PlayerNumber;

public class DTOMovesSendChat {
	String type = "sendChat";
	int playerIndex;
	String content;
	
	public DTOMovesSendChat(int playerIndex, String content) {
		this.playerIndex = playerIndex;
		this.content = content;
	}
}