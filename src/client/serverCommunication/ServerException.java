package client.serverCommunication;

import shared.definitions.*;

/**
 * Represents an exception that occurs due to server operation. It will have
 * specific information stored that will help determine what todo with the
 * exception
 */
@SuppressWarnings("serial")
public class ServerException extends Exception {
	private ServerExceptionType type;
	private String message;

	public ServerException(ServerExceptionType type, String message) {
		this.type = type;
		this.message = message;
	}

	public ServerExceptionType getType() {
		return this.type;
	}

	public void setType(ServerExceptionType type) {
		this.type = type;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ServerException of type:" + this.type + "\n\tMessage: " + this.message + "\n";
	}

}
