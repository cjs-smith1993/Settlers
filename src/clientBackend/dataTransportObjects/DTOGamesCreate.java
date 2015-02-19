package clientBackend.dataTransportObjects;

public class DTOGamesCreate {
	boolean randomTiles;
	boolean randomNumbers;
	boolean randomPorts;
	String name;
	
	public DTOGamesCreate(boolean randomTiles, boolean randomNumbers, boolean randomPorts, String name) {
		this.randomTiles = randomTiles;
		this.randomNumbers = randomNumbers;
		this.randomPorts = randomPorts;
		this.name = name;
	}
}
