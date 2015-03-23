package server.commands.user;

import client.backend.CatanSerializer;
import client.serverCommunication.ServerException;
import server.certificates.UserCertificate;
import server.commands.CommandResponse;
import server.commands.ContentType;
import server.core.CortexFactory;
import server.core.ICortex;
import server.util.StatusCode;
import shared.dataTransportObjects.DTOUserLogin;
import shared.model.CatanException;

/**
 * This is the command class that will register a new user as well as validate
 * and log them in.
 */
public class UserRegisterCommand extends AbstractUserCommand {

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
		CommandResponse response = null;
		String body;
		StatusCode status;
		ContentType contentType;
		UserCertificate userCert = null;

		try {
			userCert = cortex.userRegister(this.username, this.password);
			body = CommandResponse.getSuccessMessage();
			status = StatusCode.OK;
			contentType = ContentType.PLAIN_TEXT;
		} catch (CatanException | ServerException e) {
			body = e.getMessage();
			status = StatusCode.INVALID_REQUEST;
			contentType = ContentType.PLAIN_TEXT;
		}

		response = new CommandResponse(body, status, contentType);
		response.setUserCert(userCert);
		return response;
	}

}