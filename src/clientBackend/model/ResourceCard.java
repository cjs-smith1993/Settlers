package clientBackend.model;

import shared.definitions.*;

/**
 * A resource card counts for one unit of its resource type
 */
public class ResourceCard extends Card {
	private ResourceType type;

	public ResourceCard(ResourceType type)
	{
		this.type = type;
	}
	public ResourceType getType() {
		return type;
	}

	public void setType(ResourceType type) {
		this.type = type;
	}
	
}
