package server.core;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import client.serverCommunication.ServerException;
import server.certificates.GameCertificate;
import server.certificates.UserCertificate;
import shared.model.CatanException;

public class CentralCortexTest {
	private CentralCortex cortex = CentralCortex.getInstance();
	private int userId = 4;
	private String userName = "TstNme";
	private String password = "testPassword";
	private UserCertificate userCert = new UserCertificate(userId, userName, password);
	private int gameId = 0;
	private GameCertificate gameCert = new GameCertificate(gameId);
	
	@BeforeClass
	public static void setUpBeforeClass() {
	}

	@Test
	public void testUserActions() {
		
		/*
		 * 1. Test user login without registering
		 * -> Should throw a CatanException
		 */
		try {
			this.cortex.userLogin(this.userName, this.password);
			fail("Should not be able to login without registering first");
		} catch (CatanException e) {
			
		} catch (ServerException e) {
			e.printStackTrace();
			fail("Should not throw a ServerException");
		}
		
		/*
		 * 2. Test user register
		 * -> Should pass
		 */
		try {
			UserCertificate registerCert = this.cortex.userRegister(userName, password);
			assertTrue(registerCert.equals(userCert));
		} catch(ServerException | CatanException e) {
			e.printStackTrace();
			fail();
		}
		
		/*
		 * 3. Test user login after registering
		 * -> Should pass
		 */
		try {
			UserCertificate loginCert = this.cortex.userLogin(userName, password);
			assertTrue(loginCert.equals(userCert));
		} catch(ServerException | CatanException e) {
			e.printStackTrace();
			fail();
		}
	}

}
