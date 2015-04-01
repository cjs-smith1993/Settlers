package server.core;

import java.io.IOException;

import org.junit.Test;

import static org.junit.Assert.*;
import server.core.UserManager;
import shared.model.CatanException;

/**
 * Tests the UserManager class.
 */
public class UserManagerTest {
	@Test
	public void testUserManagerTest() throws IOException {
		UserManager manager = UserManager.getInstance();
		
		/* Register User Tests */
		try {
			manager.registerUser("", "");
			fail("\n----------------\nERROR: EMPTY CREDENTIALS, USER SHOULDN'T BE ALLOWED TO REGISTER\n----------------\n");
		} catch (CatanException e) {}
		
		try {
			manager.registerUser("tOmtESTeR", "ToMmy");
			fail("\n----------------\nERROR: INVALID USERNAME, USER SHOULDN'T BE ALLOWED TO REGISTER\n----------------\n");
		} catch (CatanException e) {}
		
		try {
			manager.registerUser("Tommy", "in");
			fail("\n----------------\nERROR: INVALID USER, SHOULDN'T BE ALLOWED TO REGISTER\n----------------\n");
		} catch (CatanException e) {}

		boolean isAuthenticated = manager.authenticateUser("TestUser", "tESTuSER");
		assertFalse(isAuthenticated);
		
		/* User Authentication Tests */
		try {
			manager.registerUser("TstUsr", "tESTuSER");
		} catch (CatanException e) {
			fail("\n----------------\nERROR: USER SHOULD BE ALLOWED TO REGISTER\n----------------\n");
		}
		
		isAuthenticated = manager.authenticateUser("TestUser", "bad-password");
		assertFalse(isAuthenticated);
		
		isAuthenticated = manager.authenticateUser("TstUsr", "tESTuSER");
		assertTrue(isAuthenticated);
	}
}