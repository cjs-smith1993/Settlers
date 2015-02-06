package clientBackend.dataTransportObjects;

public class DTOGameCreate {
	String randomTiles;
	String randomNumbers;
	String randomPorts;
	String name;
	
	public DTOGameCreate(String randomTiles, String randomNumbers, String randomPorts, String name) {
		this.randomTiles = randomTiles;
		this.randomNumbers = randomNumbers;
		this.randomPorts = randomPorts;
		this.name = name;
	}
}
