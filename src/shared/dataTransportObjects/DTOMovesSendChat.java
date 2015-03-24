package shared.dataTransportObjects;

import shared.definitions.PlayerNumber;

public class DTOMovesSendChat {
	public String type = "sendChat";
	public int playerIndex;
	public String content;

	public DTOMovesSendChat(PlayerNumber playerIndex, String content) {
		this.playerIndex = playerIndex.getInteger();
		this.content = content;
	}
}