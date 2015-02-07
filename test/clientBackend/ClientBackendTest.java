package clientBackend;

import static org.junit.Assert.*;

import org.junit.Test;

public class ClientBackendTest {

	public static void main(String[] args) {

		String[] testClasses = new String[] {
				"serverCommunication.ServerProxyTest"
		};

		org.junit.runner.JUnitCore.main(testClasses);
	}

	@Test
	public void test() {

	}
}
