package clientBackend.model;

import shared.locations.HexLocation;

/**
 * Represents the robber. The robber occupies a tiles and blocks the production
 * of resources for that tile
 */
public class Robber {
	private HexLocation location;

	public Robber(HexLocation location) {
		this.location = location;
	}

	public HexLocation getLocation() {
		return this.location;
	}

	/**
	 * Moves the robber to the new location. The robber cannot be moved onto its
	 * current location
	 *
	 * @param newLocation
	 *            the new location of the robber
	 */
	public void setLocation(HexLocation newLocation) {
		this.location = newLocation;
	}
}
