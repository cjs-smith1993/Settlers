package shared.transport;

public class TransportLine {
	public String source;
	public String message;
	
	public TransportLine(){}
	
	public TransportLine(String source, String message) {
		this.source = source;
		this.message = message;
	}
}
