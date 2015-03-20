package shared.dataTransportObjects;

import shared.definitions.PlayerNumber;

public class DTOMovesSendChat {
	String type = "sendChat";
	int playerIndex;
	String content;

	public DTOMovesSendChat(PlayerNumber playerIndex, String content) {
		this.playerIndex = playerIndex.getInteger();
		this.content = content;
	}
}