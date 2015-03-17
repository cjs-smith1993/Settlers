package server.commands.user;

import client.backend.CatanSerializer;
import client.backend.dataTransportObjects.DTOUserLogin;
import server.certificates.UserCertificate;
import server.core.ICortex;
import server.util.CommandResponse;
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
		CommandResponse response = new CommandResponse("Success");
		if (this.username.equals("Test") && this.password.equals("test")) {
			response.setStatus(StatusCode.OK);
			response.setBody(this.successMessage);
			response.setUserCert(new UserCertificate(10, this.username, this.password));
		}
		else {
			response.setStatus(StatusCode.INVALID_REQUEST);
			response.setBody(this.failureMessage);
		}
		return response;
	}
}