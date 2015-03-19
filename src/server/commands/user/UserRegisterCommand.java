package server.commands.user;

import client.backend.CatanSerializer;
import client.backend.dataTransportObjects.DTOUserLogin;
import server.certificates.UserCertificate;
import server.commands.CommandResponse;
import server.core.ICortex;
import server.util.StatusCode;

/**
 * This is the command class that will register a new user as well as validate
 * and log them in.
 */
public class UserRegisterCommand extends AbstractUserCommand {
	private String username;
	private String password;

	private final String successMessage = "Success";
	private final String failureMessage = "Failed to register - bad username or password.";

	public UserRegisterCommand(String json, ICortex cortex) {
		super(cortex);

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
		UserCertificate userCert = this.cortex.userRegister(this.username, this.password);
		CommandResponse response = null;
		if (userCert != null) {
			response = new CommandResponse(this.successMessage);
			response.setStatus(StatusCode.OK);
			response.setUserCert(userCert);
		}
		else {
			response = new CommandResponse(this.failureMessage);
			response.setStatus(StatusCode.INVALID_REQUEST);
		}
		return response;
	}

}