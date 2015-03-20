package shared.dataTransportObjects;

import java.util.ArrayList;

public class DTOGame {
	public String title;
	public int id;
	public ArrayList<DTOPlayer> players;

	public DTOGame(int id, String title, ArrayList<DTOPlayer> players) {
		this.id = id;
		this.title = title;
		this.players = players;
	}
}
