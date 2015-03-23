package shared.dataTransportObjects;

import com.google.gson.JsonParseException;

import shared.locations.EdgeDirection;

public class DTOEdgeLocation {
	public int x;
	public int y;
	public EdgeDirection direction;

	public DTOEdgeLocation(int x, int y, EdgeDirection direction) {
		if (direction == null) {
			throw new JsonParseException("JSON parse error");
		}

		this.x = x;
		this.y = y;
		this.direction = direction;
	}
}
