package shared.dataTransportObjects;

import com.google.gson.JsonParseException;

import shared.locations.VertexDirection;

public class DTOVertexLocation {
	public int x;
	public int y;
	public VertexDirection direction;

	public DTOVertexLocation(int x, int y, VertexDirection direction) {
		if (direction == null) {
			throw new JsonParseException("JSON parse error");
		}

		this.x = x;
		this.y = y;
		this.direction = direction;
	}
}
