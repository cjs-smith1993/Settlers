package clientBackend.dataTransportObjects;

import shared.definitions.PlayerNumber;

public class DTOMovesSendChat {
	String type = "sendChat";
	PlayerNumber playerIndex;
	String content;
	
	public DTOMovesSendChat(PlayerNumber playerIndex, String content) {
		this.playerIndex = playerIndex;
		this.content = content;
	}
}