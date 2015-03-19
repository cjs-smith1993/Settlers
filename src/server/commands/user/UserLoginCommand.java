package server.commands.user;

import client.backend.CatanSerializer;
import client.backend.dataTransportObjects.DTOUserLogin;
import server.certificates.UserCertificate;
import server.commands.CommandResponse;
import server.core.ICortex;
import server.util.StatusCode;

/**
 * This is the command in user to validate a previous user
 */
public class UserLoginCommand extends AbstractUserCommand {
	private String username;
	private String password;

	private final String successMessage = "Success";
	private final String failureMessage = "Failed to login - bad username or password.";

	public UserLoginCommand(String json, ICortex cortex) {
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
		UserCertificate userCert = this.cortex.userLogin(this.username, this.password);
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