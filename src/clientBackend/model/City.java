package clientBackend.model;

import shared.definitions.PlayerNumber;

/**
 * A city is a dwelling that adds two victory points to its owner, and that
 * generates two resources per connected tile per harvest
 */
public class City extends Dwelling {

	public City(PlayerNumber number) {
		this.owner = number;
		this.victoryPoints = 2;
	}
}
