package server.commands.user;

import client.backend.CatanSerializer;
import server.certificates.UserCertificate;
import server.commands.CommandResponse;
import server.commands.ContentType;
import server.core.CortexFactory;
import server.core.ICortex;
import server.util.StatusCode;
import shared.dataTransportObjects.DTOUserLogin;

/**
 * This is the command class that will register a new user as well as validate
 * and log them in.
 */
public class UserRegisterCommand extends AbstractUserCommand {

	private static final String SUCCESS_MESSAGE = "Success";
	private static final String FAILURE_MESSAGE = "Failed to register - bad username or password.";

	private String username;
	private String password;

	public UserRegisterCommand(String json) {
		DTOUserLogin dto = (DTOUserLogin) CatanSerializer.getInstance().deserializeObject(json,
				DTOUserLogin.class);
		this.username = dto.username;
		this.password = dto.password;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandResponse execute() {
		ICortex cortex = CortexFactory.getInstance().getCortex();
		UserCertificate userCert = cortex.userRegister(this.username, this.password);

		CommandResponse response = null;
		String body;
		StatusCode status;
		ContentType contentType;

		if (userCert != null) {
			body = SUCCESS_MESSAGE;
			status = StatusCode.OK;
			contentType = ContentType.PLAIN_TEXT;
		}
		else {
			body = FAILURE_MESSAGE;
			status = StatusCode.INVALID_REQUEST;
			contentType = ContentType.PLAIN_TEXT;
		}

		response = new CommandResponse(body, status, contentType);
		response.setUserCert(userCert);
		return response;
	}

}