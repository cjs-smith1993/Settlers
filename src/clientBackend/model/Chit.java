package clientBackend.model;

import shared.locations.HexLocation;

/**
 * Represents a number chit that is placed on top of a tile. Every tile except
 * the desert has one chit. When the result of a dice roll matches a chit's
 * number, all dwellings on the chit's tile generate resource(s)
 */
public class Chit {
	private char letter;
	private int number;
	private HexLocation location;

	public Chit(char letter, int number, HexLocation location) {
		this.letter = letter;
		this.number = number;
		this.location = location;
	}

	public char getLetter() {
		return this.letter;
	}

	public int getNumber() {
		return this.number;
	}

	public HexLocation getLocation() {
		return this.location;
	}

	public void setTile(HexLocation location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "Chit [letter=" + this.letter + ", number=" + this.number + ", location=" + this.location + "]";
	}
}
