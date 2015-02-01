package clientBackend.model;

/**
 * Represents a number chit that is placed on top of a tile. Every tile except
 * the desert has one chit. When the result of a dice roll matches a chit's
 * number, all dwellings on the chit's tile generate resource(s)
 */
public class Chit {
	private char letter;
	private int number;
	private Tile tile;
	
	public Chit(char letter, int number, Tile tile) {
		this.letter = letter;
		this.number = number;
		this.tile = tile;
	}
	
	public char getLetter() {
		return this.letter;
	}

	public int getNumber() {
		return this.number;
	}

	public Tile getTile() {
		return this.tile;
	}
}
