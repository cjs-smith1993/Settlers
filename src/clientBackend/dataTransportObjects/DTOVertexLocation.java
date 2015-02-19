package clientBackend.dataTransportObjects;

import shared.locations.VertexDirection;

public class DTOVertexLocation {
	public int x;
	public int y;
	public VertexDirection direction;

	public DTOVertexLocation(int x, int y, VertexDirection direction) {
		this.x = x;
		this.y = y;
		this.direction = direction;
	}
}
