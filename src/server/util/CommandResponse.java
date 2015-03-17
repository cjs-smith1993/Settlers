package server.util;

/**
 * Encapsulation of a serialized cookie and a message body. A CommandResponse is
 * returned by each Command class. The cookie may or may not be set, and there
 * will always be a message body.
 *
 */
public class CommandResponse {
	private String cookie;
	private String body;

	public CommandResponse(String cookie, String body) {
		this.cookie = cookie;
		this.body = body;
	}

	public CommandResponse(String body) {
		this.cookie = null;
		this.body = body;
	}

	public String getCookie() {
		return this.cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public String getBody() {
		return this.body;
	}

	public void setBody(String body) {
		this.body = body;
	}

}
