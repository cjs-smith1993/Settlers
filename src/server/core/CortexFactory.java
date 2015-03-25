package server.core;

/**
 * Provides the correct ICortex to be used to access the Catan models on the
 * server. Can be programmed to return either the CentralCortex or the
 * MockCortex
 *
 * @author kevinjreece
 */
public class CortexFactory {

	public static boolean testingEnabled = false;
	private static CortexFactory instance;

	private CortexFactory() {

	}

	public static CortexFactory getInstance() {
		if (instance == null) {
			instance = new CortexFactory();
		}
		return instance;
	}

	/**
	 * Returns either the MockCortex or the CentralCortex
	 *
	 * @return
	 */
	public ICortex getCortex() {
		return testingEnabled ? MockCortex.getInstance() : CentralCortex.getInstance();
	}

	/**
	 * Changes the CortexFactory between testing and production settings
	 *
	 * @param _testingEnabled
	 */
	public static void setTestEnabled(boolean _testingEnabled) {
		testingEnabled = _testingEnabled;
	}
}
