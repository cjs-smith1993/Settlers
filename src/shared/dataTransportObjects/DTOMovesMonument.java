package shared.dataTransportObjects;

import com.google.gson.JsonParseException;

import shared.definitions.PlayerNumber;

public class DTOMovesMonument {
	public String type = "Monument";
	public int playerIndex;

	public DTOMovesMonument(PlayerNumber playerIndex) {
		if (playerIndex == null) {
			throw new JsonParseException("JSON parse error");
		}

		this.playerIndex = playerIndex.getInteger();
	}
}
