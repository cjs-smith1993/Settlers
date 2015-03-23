package shared.dataTransportObjects;

import com.google.gson.JsonParseException;

import shared.definitions.CatanColor;

public class DTOGamesJoin {
	public int id;
	public CatanColor color;

	public DTOGamesJoin(int id, CatanColor color) {
		if (color == null) {
			throw new JsonParseException("JSON parse error");
		}

		this.id = id;
		this.color = color;
	}
}
