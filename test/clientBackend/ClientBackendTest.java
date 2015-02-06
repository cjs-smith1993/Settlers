package clientBackend;

import static org.junit.Assert.*;

import org.junit.Test;

public class ClientBackendTest {

	@Test
	public void test() {
		fail("Not yet implemented");
	}

	public static void main(String[] args) {

		String[] testClasses = new String[] {
				"clientBackend.model.BoardTest",
				"clientBackend.model.PlayerHoldingsTest"
		};

		org.junit.runner.JUnitCore.main(testClasses);
	}

}
