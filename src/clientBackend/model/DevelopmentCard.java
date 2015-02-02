package clientBackend.model;

import shared.definitions.*;

/**
 * A development card can be purchased from the bank. Each development card
 * type has its own behavior
 */
public class DevelopmentCard {
	private DevCardType type;
	private boolean playable;
	public DevelopmentCard(DevCardType type)
	{
		this.type = type;
		this.playable = false;
	}
	public DevCardType getType() {
		return type;
	}
	public void setType(DevCardType type) {
		this.type = type;
	}
	public boolean isPlayable() {
		return playable;
	}
	public void setPlayable(boolean playable) {
		this.playable = playable;
	}
	
}
