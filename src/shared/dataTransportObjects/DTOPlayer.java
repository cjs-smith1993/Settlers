package shared.dataTransportObjects;

import com.google.gson.JsonParseException;

import shared.definitions.CatanColor;

public class DTOPlayer {
	public CatanColor color;
	public String name;
	public int id;

	public DTOPlayer() {
		this.color = null;
		this.name = null;
		this.id = -1;
	}

	public DTOPlayer(CatanColor color, String name, int id) {
		if (color == null) {
			throw new JsonParseException("JSON parse error");
		}

		this.color = color;
		this.name = name;
		this.id = id;
	}
}
