package server.commands.user;

import client.backend.CatanSerializer;
import client.backend.dataTransportObjects.DTOUserLogin;
import server.certificates.UserCertificate;
import server.commands.CommandResponse;
import server.commands.ContentType;
import server.core.CortexFactory;
import server.core.ICortex;
import server.util.StatusCode;

/**
 * This is the command in user to validate a previous user
 */
public class UserLoginCommand extends AbstractUserCommand {

	private static final String SUCCESS_MESSAGE = "Success";
	private static final String FAILURE_MESSAGE = "Failed to login - bad username or password.";

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
	public CommandResponse execute() {
		ICortex cortex = CortexFactory.getInstance().getCortex();
		UserCertificate userCert = cortex.userLogin(this.username, this.password);
		CommandResponse response = null;
		if (userCert != null) {
			response = new CommandResponse(SUCCESS_MESSAGE);
			response.setStatus(StatusCode.OK);
			response.setResponseType(ContentType.PLAIN_TEXT);
			response.setUserCert(userCert);
		}
		else {
			response = new CommandResponse(FAILURE_MESSAGE);
			response.setStatus(StatusCode.INVALID_REQUEST);
			response.setResponseType(ContentType.PLAIN_TEXT);
		}
		return response;
	}
}