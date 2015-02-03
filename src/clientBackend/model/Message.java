package clientBackend.model;

/**
 * Stores the information of a log or chat message
 *
 */
public class Message {
	private String name;
	private String message;
	
	/**
	 * Creates a new message.
	 * @param name
	 * @param message
	 */
	public Message(String name, String message) {
		this.name = name;
		this.message = message;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
