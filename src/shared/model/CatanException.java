package shared.model;

import shared.definitions.*;

/**
 * Represents an exception that occurs within the context of the game *
 */
@SuppressWarnings("serial")
public class CatanException extends Exception {
	private CatanExceptionType type;
	private String message;

	public CatanException(CatanExceptionType type, String message) {
		this.type = type;
		this.message = message;
	}

	public CatanExceptionType getType() {
		return this.type;
	}

	public void setType(CatanExceptionType type) {
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
		return "CatanException of type:" + this.type + "\n\tMessage: " + this.message + "\n";
	}
}
