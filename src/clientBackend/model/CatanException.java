package clientBackend.model;

import shared.definitions.*;

/**
 * Represents an exception that occurs within the context of the game *
 */
public class CatanException extends Throwable {
	private CatanExceptionType type;
	private String message;
}
