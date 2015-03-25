package shared.model;

import java.util.HashMap;
import java.util.Random;

/**
 * Generates a random number
 */
public class RandomNumberGenerator {
	private Random rng;
	private static HashMap<Long, RandomNumberGenerator> generators = new HashMap<Long, RandomNumberGenerator>();

	public RandomNumberGenerator() {
		this.rng = new Random();
	}

	private RandomNumberGenerator(long seed) {
		this.rng = new Random(seed);
	}

	public static RandomNumberGenerator getInstance(long seed) {
		if (generators.get(seed) == null) {
			generators.put(seed, new RandomNumberGenerator(seed));
		}
		return generators.get(seed);
	}

	public void reSeed() {
		this.rng = new Random();
	}

	public void reSeed(long seed) {
		this.rng.setSeed(seed);
	}

	/**
	 * Generates a random integer between the two bounds (inclusive)
	 *
	 * @param lowerBound
	 * @param upperBound
	 * @return the generated number
	 */
	public int generate(int lowerBound, int upperBound) {
		return this.rng.nextInt(upperBound - lowerBound + 1) + lowerBound;
	}
}
