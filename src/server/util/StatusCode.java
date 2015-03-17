package server.util;

public enum StatusCode {
	OK(200),
	INVALID_REQUEST(400),
	INTERNAL_ERROR(500);

	int code;

	StatusCode(int code) {
		this.code = code;
	}

	public int getCode() {
		return this.code;
	}
}
