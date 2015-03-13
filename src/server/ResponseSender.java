package server;

/**
 * this the the object we will return so that we can send headers and response bodies back
 *
 */
public class ResponseSender {
	private boolean headerExists;
	private String Headers;
	private String Body;
	
	
	public ResponseSender(boolean headerExists, String headers) {
		this.setBody(null);
		this.headerExists = headerExists;
		Headers = headers;
	}
	public ResponseSender(String body) {
		this.setHeader(false);
		this.setHeaders(null);
		Body = body;
	}
	public ResponseSender(boolean isHeader, String headers, String body) {
		this.headerExists = isHeader;
		Headers = headers;
		Body = body;
	}
	public boolean isHeader() {
		return headerExists;
	}
	public void setHeader(boolean isHeader) {
		this.headerExists = isHeader;
	}
	public String getHeaders() {
		return Headers;
	}
	public void setHeaders(String headers) {
		Headers = headers;
	}
	public String getBody() {
		return Body;
	}
	public void setBody(String body) {
		Body = body;
	}

}
