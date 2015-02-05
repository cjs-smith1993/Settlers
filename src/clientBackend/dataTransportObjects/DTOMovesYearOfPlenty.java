package clientBackend.dataTransportObjects;

public class DTOMovesYearOfPlenty {
	String type = "Year_of_Plenty";
	int playerIndex;
	String resource1;
	String resource2;
	
	public DTOMovesYearOfPlenty(int playerIndex, String resource1, String resource2) {
		this.resource1 = resource1;
		this.resource2 = resource2;
	}
}