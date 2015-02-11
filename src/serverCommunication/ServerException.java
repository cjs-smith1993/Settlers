package serverCommunication;

import shared.definitions.*;

/**
 * Represents an exception that occurs due to server operation. It will have
 * specific information stored that will help determine what todo with the
 * exception
 */
public class ServerException extends Exception {
	private ServerExceptionType type;
	private String message;

	public ServerException(ServerExceptionType type, String message) {
		this.type = type;
		this.message = message;
	}
}
