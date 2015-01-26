package clientBackend;

import shared.definitions.*;

/**
 * Represents an exception that occurs due to server operation.
 * It will have specific information stored that will help 
 * determine what todo with the exception
 */
public class ServerException extends Throwable {
	private ServerExceptionType type;
	private String message;
	//maybe a json object
}
