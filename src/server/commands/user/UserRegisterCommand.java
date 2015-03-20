package server.commands.user;

import client.backend.CatanSerializer;
import client.backend.dataTransportObjects.DTOUserLogin;
import server.certificates.UserCertificate;
import server.commands.CommandResponse;
import server.core.CortexFactory;
import server.core.ICortex;
import server.util.StatusCode;

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
		if (userCert != null) {
			response = new CommandResponse(SUCCESS_MESSAGE);
			response.setStatus(StatusCode.OK);
			response.setUserCert(userCert);
		}
		else {
			response = new CommandResponse(FAILURE_MESSAGE);
			response.setStatus(StatusCode.INVALID_REQUEST);
		}
		return response;
	}

}