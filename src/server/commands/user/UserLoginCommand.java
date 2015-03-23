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
 * This is the command in user to validate a previous user
 */
public class UserLoginCommand extends AbstractUserCommand {

	private String username;
	private String password;

	public UserLoginCommand(String json) {
		DTOUserLogin dto = (DTOUserLogin) CatanSerializer.getInstance().deserializeObject(json,
				DTOUserLogin.class);

		this.username = dto.username;
		this.password = dto.password;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandResponse executeInner() throws CatanException, ServerException {
		ICortex cortex = CortexFactory.getInstance().getCortex();
		UserCertificate userCert = cortex.userLogin(this.username, this.password);

		String body = CommandResponse.getSuccessMessage();
		StatusCode status = StatusCode.OK;
		ContentType contentType = ContentType.PLAIN_TEXT;

		CommandResponse response = new CommandResponse(body, status, contentType);
		response.setUserCert(userCert);
		return response;
	}

}