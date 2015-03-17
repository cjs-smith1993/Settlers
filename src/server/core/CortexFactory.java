package server.core;

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

	public ICortex getCortex() {
		return testingEnabled ? MockCortex.getInstance() : CentralCortex.getInstance();
	}
}
