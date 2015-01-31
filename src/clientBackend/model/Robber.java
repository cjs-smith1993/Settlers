package clientBackend.model;

/**
 * Represents the robber. The robber occupies a tiles and blocks the production
 * of resources for that tile
 */
public class Robber {
	private Tile location;
	
	public Tile getLocation() {
		return this.location;
	}

	/**
	 * Moves the robber to the new location. The robber cannot be moved onto
	 * its current location
	 * @param newLocation the new location of the robber
	 */
	public void setLocation(Tile newLocation) {
		this.location = newLocation;
	}
}
