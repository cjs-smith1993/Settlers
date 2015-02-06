package clientBackend.dataTransportObjects;

public class DTOGamesCreate {
	String randomTiles;
	String randomNumbers;
	String randomPorts;
	String name;
	
	public DTOGamesCreate(String randomTiles, String randomNumbers, String randomPorts, String name) {
		this.randomTiles = randomTiles;
		this.randomNumbers = randomNumbers;
		this.randomPorts = randomPorts;
		this.name = name;
	}
}
