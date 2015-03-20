package server.commands;

import server.certificates.GameCertificate;
import server.certificates.UserCertificate;
import server.util.StatusCode;

/**
 * Encapsulation of a serialized cookie and a message body. A CommandResponse is
 * returned by each Command class. The cookie may or may not be set, and there
 * will always be a message body.
 *
 */
public class CommandResponse {
	private String body;
	private ContentType contentType;
	private StatusCode status;
	private UserCertificate userCert;
	private GameCertificate gameCert;

	public CommandResponse(String body, StatusCode status, ContentType contentType) {
		this.body = body;
		this.status = status;
		this.contentType = contentType;
	}

	public UserCertificate getUserCert() {
		return this.userCert;
	}

	public void setUserCert(UserCertificate userCert) {
		this.userCert = userCert;
	}

	public GameCertificate getGameCert() {
		return this.gameCert;
	}

	public void setGameCert(GameCertificate gameCert) {
		this.gameCert = gameCert;
	}

	public StatusCode getStatus() {
		return this.status;
	}

	public void setStatus(StatusCode status) {
		this.status = status;
	}

	public ContentType getResponseType() {
		return this.contentType;
	}

	public void setResponseType(ContentType contentType) {
		this.contentType = contentType;
	}

	public String getBody() {
		return this.body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public static CommandResponse getUnauthenticatedUserResponse() {
		String body = "The catan.user HTTP cookie is missing or invalid. You must login or register before calling this method.";
		StatusCode status = StatusCode.INVALID_REQUEST;
		ContentType contentType = ContentType.PLAIN_TEXT;
		return new CommandResponse(body, status, contentType);
	}

	public static CommandResponse getUnauthenticatedGameResponse() {
		String body = "The catan.game HTTP cookie is missing or invalid. You must join a game before calling this method.";
		StatusCode status = StatusCode.INVALID_REQUEST;
		ContentType contentType = ContentType.PLAIN_TEXT;
		return new CommandResponse(body, status, contentType);
	}
}
