package shared.dataTransportObjects;

public class DTOGamesCreate {
	public boolean randomTiles;
	public boolean randomNumbers;
	public boolean randomPorts;
	public String name;
	
	public DTOGamesCreate(boolean randomTiles, boolean randomNumbers, boolean randomPorts, String name) {
		this.randomTiles = randomTiles;
		this.randomNumbers = randomNumbers;
		this.randomPorts = randomPorts;
		this.name = name;
	}
}
