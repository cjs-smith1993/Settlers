package client.data;

import shared.definitions.*;

/**
 * Used to pass player information into views<br>
 * <br>
 * PROPERTIES:<br>
 * <ul>
 * <li>Id: Unique player ID</li>
 * <li>PlayerIndex: Player's order in the game [0-3]</li>
 * <li>Name: Player's name (non-empty string)</li>
 * <li>Color: Player's color (cannot be null)</li>
 * </ul>
 * 
 */
public class PlayerInfo
{
	
	private int id;
	private PlayerNumber playerIndex;
	private String name;
	private CatanColor color;
	
	public PlayerInfo()
	{
		setId(-1);
		setPlayerIndex(PlayerNumber.BANK);
		setName("");
		setColor(CatanColor.WHITE);
	}
	
	public PlayerInfo(int id, PlayerNumber playerIndex, String name, CatanColor color) {
		setId(id);
		setPlayerIndex(playerIndex);
		setName(name);
		setColor(color);
	}
	
 	public int getId()
	{
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public PlayerNumber getPlayerIndex()
	{
		return playerIndex;
	}
	
	public void setPlayerIndex(PlayerNumber playerIndex)
	{
		this.playerIndex = playerIndex;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public CatanColor getColor()
	{
		return color;
	}
	
	public void setColor(CatanColor color)
	{
		this.color = color;
	}

	public String toString() {
		return "player id: " + this.id + "\nplayer index: " + this.playerIndex + "\nplayer name: " + this.name + 
				"\nplayer color: " + this.color;
	}
	
	@Override
	public int hashCode()
	{
		return 31 * this.id;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final PlayerInfo other = (PlayerInfo) obj;
		
		return this.id == other.id;
	}
}

