package server.core;

import org.junit.Test;
import static org.junit.Assert.*;

import server.core.UserManager;
import shared.model.CatanException;

/**
 * Tests the UserManager class.
 */
public class UserManagerTest {
	@Test
	public void testUserManagerTest() {
		UserManager manager = UserManager.getInstance();
		
		/* Register User Tests */
		try {
			manager.registerUser("", "");
			fail("\n----------------\nERROR: EMPTY CREDENTIALS, USER SHOULDN'T BE ALLOWED TO REGISTER\n----------------\n");
		} catch (CatanException e) {}
		
		try {
			manager.registerUser("Tom", "tOmtEST");
			fail("\n----------------\nERROR: INVALID USERNAME, USER SHOULDN'T BE ALLOWED TO REGISTER\n----------------\n");
		} catch (CatanException e) {}
		
		try {
			manager.registerUser("Tommy", "invpw");
			fail("\n----------------\nERROR: INVALID USER, SHOULDN'T BE ALLOWED TO REGISTER\n----------------\n");
		} catch (CatanException e) {}

		boolean isAuthenticated = manager.authenticateUser("TestUser", "tESTuSER");
		assertFalse(isAuthenticated);
		
		/* User Authentication Tests */
		try {
			manager.registerUser("TestUser", "tESTuSER");
		} catch (CatanException e) {
			fail("\n----------------\nERROR: USER SHOULD BE ALLOWED TO REGISTER\n----------------\n");
		}
		
		isAuthenticated = manager.authenticateUser("TestUser", "bad-password");
		assertFalse(isAuthenticated);
		
		isAuthenticated = manager.authenticateUser("TestUser", "tESTuSER");
		assertTrue(isAuthenticated);
	}
}