package clientBackend.model;

import shared.definitions.*;

/**
 * Represents an exception that occurs within the context of the game *
 */
public class CatanException extends Exception {
	private CatanExceptionType type;
	private String message;
	
	public CatanException(CatanExceptionType type, String message){ 
		this.type = type;
		this.message = message;
	}

	public CatanExceptionType getType() {
		return type;
	}

	public void setType(CatanExceptionType type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "CatanException of type:" + type + "\n\tMessage: " + message + "\n";
	}
}
