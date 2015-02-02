package clientBackend.model;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents the logical collection of dice
 */
public class Dice {
	private final int numDice = 2;
	private Collection<RandomNumberGenerator> dieCollection;

	public Dice() {
		this.dieCollection = new ArrayList<RandomNumberGenerator>();
		for (int i = 0; i < this.numDice; i++) {
			RandomNumberGenerator rng = new RandomNumberGenerator();
			this.dieCollection.add(rng);
		}
	}

	/**
	 * Rolls the dice
	 *
	 * @return the sum of all die rolls
	 */
	public int roll() {
		int sum = 0;

		for (RandomNumberGenerator rng : this.dieCollection) {
			int num = rng.generate(1, 6);
			sum += num;
		}

		return sum;
	}
}
