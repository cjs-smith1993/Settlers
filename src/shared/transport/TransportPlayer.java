package shared.transport;

import shared.definitions.CatanColor;
import shared.definitions.PlayerNumber;

public class TransportPlayer {
	public TransportResources resources;
	public TransportOldDevCards oldDevCards;
	public TransportNewDevCards newDevCards;
	public int roads;
	public int cities;
	public int settlements;
	public int soldiers;
	public int victoryPoints;
	public int monuments;
	public boolean playedDevCard;
	public boolean discarded;
	public int playerID;
	public PlayerNumber playerIndex;
	public String name;
	public CatanColor color;
}
