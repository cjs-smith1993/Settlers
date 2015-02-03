package clientBackend.model;

import shared.definitions.PlayerNumber;

/**
 * A settlement is a dwelling that adds one victory point to its owner, and
 * that generates one resource per connected tile per harvest
 */
public class Settlement extends Dwelling {
	
	public Settlement(PlayerNumber number) {
		this.owner = number;
		this.victoryPoints = 1;
	}
}
