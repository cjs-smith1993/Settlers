package clientBackend.dataTransportObjects;

public class DTOMovesMaritimeTrade {
	String type = "maritimeTrade";
	int playerIndex;
	int ratio;
	String inputResource;
	String outputResource;
	
	public DTOMovesMaritimeTrade(int playerIndex, int ratio, String inputResource, String outputResource) {
		this.playerIndex = playerIndex;
		this.ratio = ratio;
		this.inputResource = inputResource;
		this.outputResource = outputResource;
	}
}
