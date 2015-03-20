package server.commands;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import server.certificates.UserCertificate;
import server.commands.user.AbstractUserCommand;
import server.core.CortexFactory;
import server.factories.UserCommandFactory;

public class UserCommandsTest {

	private String username = "Test";
	private String password = "TestPassword";

	private String type = "login";
	private String json = "{username:\"" + this.username + "\", password:\"" + this.password
			+ "\"}";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		CortexFactory.setTestEnabled(true);
	}

	@Test
	public void testUserLogin() {
		AbstractUserCommand loginCommand = UserCommandFactory.getInstance().getCommand(this.type,
				this.json);
		CommandResponse response = loginCommand.execute();

		UserCertificate userCert = response.getUserCert();
		assertEquals(userCert.getName(), this.username);
		assertEquals(userCert.getPassword(), this.password);
	}

	@Test
	public void testUserRegister() {
		AbstractUserCommand loginCommand = UserCommandFactory.getInstance().getCommand(this.type,
				this.json);
		CommandResponse response = loginCommand.execute();

		UserCertificate userCert = response.getUserCert();
		assertEquals(userCert.getName(), this.username);
		assertEquals(userCert.getPassword(), this.password);
	}

}
