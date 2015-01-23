package clientBackend;

import shared.definitions.*;

/**
 * Represents an exception that occurs due to server operation
 */
public class ServerException extends Throwable {
	private ServerExceptionType type;
	private String message;
}
