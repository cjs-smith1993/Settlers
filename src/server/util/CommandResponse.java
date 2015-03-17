package server.util;

import server.certificates.GameCertificate;
import server.certificates.UserCertificate;

/**
 * Encapsulation of a serialized cookie and a message body. A CommandResponse is
 * returned by each Command class. The cookie may or may not be set, and there
 * will always be a message body.
 *
 */
public class CommandResponse {
	private UserCertificate userCert;
	private GameCertificate gameCert;
	private StatusCode status;
	private String body;

	public CommandResponse(String body) {
		this.userCert = null;
		this.gameCert = null;
		this.status = null;
		this.body = body;
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

	public String getBody() {
		return this.body;
	}

	public void setBody(String body) {
		this.body = body;
	}

}
