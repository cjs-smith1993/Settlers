package clientBackend.dataTransportObjects;

import shared.locations.EdgeDirection;

public class DTOEdgeLocation {
	public int x;
	public int y;
	public EdgeDirection direction;

	public DTOEdgeLocation(int x, int y, EdgeDirection direction) {
		this.x = x;
		this.y = y;
		this.direction = direction;
	}
}
