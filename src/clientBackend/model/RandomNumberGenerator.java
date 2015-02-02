package clientBackend.model;

import java.util.Random;

/**
 * Generates a random number
 */
public class RandomNumberGenerator {
	private Random rng;

	public RandomNumberGenerator() {
		this.rng = new Random();
	}

	public RandomNumberGenerator(long seed) {
		this.rng = new Random(seed);
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
