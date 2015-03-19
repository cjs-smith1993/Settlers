package shared;

import org.junit.Test;

public class CatanSubsetTest {

	public static void main(String[] args) {

		String[] testClasses = new String[] {
				"server.commands.UserCommandsTest"
		};

		org.junit.runner.JUnitCore.main(testClasses);
	}

	@Test
	public void test() {

	}
}
