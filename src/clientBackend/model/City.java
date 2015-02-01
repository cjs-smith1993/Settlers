package clientBackend.model;

/**
 * A city is a dwelling that adds two victory points to its owner, and that
 * generates two resources per connected tile per harvest
 */
public class City extends Dwelling {

	public City() {
		this.victoryPoints = 2;
	}
}
