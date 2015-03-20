package server.commands;

public enum ContentType {
	PLAIN_TEXT("text/plain"),
	JSON("application/json");

	String name;

	ContentType(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}
