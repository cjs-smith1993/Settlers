package shared.definitions;

public enum DevCardType
{
	SOLDIER(14), YEAR_OF_PLENTY(2), MONOPOLY(2), ROAD_BUILD(2), MONUMENT(5);
	
	private int gameStartNumber; 
	
	private DevCardType(int number) {
		this.gameStartNumber = number;
	}
	
	public int getGameStartNumber() {
		return this.gameStartNumber;
	}
}

