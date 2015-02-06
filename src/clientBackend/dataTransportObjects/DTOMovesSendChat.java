package clientBackend.dataTransportObjects;

public class DTOMovesSendChat {
	String type = "sendChat";
	int playerIndex;
	String content;
	
	public DTOMovesSendChat(int playerIndex, String content) {
		this.playerIndex = playerIndex;
		this.content = content;
	}
}