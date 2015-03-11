package shared.model;

import java.util.Collection;

import shared.definitions.*;
import shared.locations.*;

/**
 * Represents a harbor, allowing for maritime trade. Each player starts with one
 * harbor with resource ANY that trades at a 4:1 rate
 */
public class Harbor {
	private HexLocation location;
	private Collection<VertexLocation> ports;
	private ResourceType resource;
	private int ratio;

	public Harbor(
			HexLocation location,
			Collection<VertexLocation> ports,
			ResourceType resource,
			int ratio) {
		this.location = location;
		this.ports = ports;
		this.resource = resource;
		this.ratio = ratio;
	}

	public HexLocation getLocation() {
		return this.location;
	}

	public void setLocation(HexLocation location) {
		this.location = location;
	}

	public Collection<VertexLocation> getPorts() {
		return this.ports;
	}

	public void setPorts(Collection<VertexLocation> ports) {
		this.ports = ports;
	}

	public ResourceType getResource() {
		return this.resource;
	}

	public int getRatio() {
		return this.ratio;
	}

	@Override
	public String toString() {
		return "Harbor [location=" + this.location + ", ports=" + this.ports + ", resource="
				+ this.resource + ", ratio=" + this.ratio + "]";
	}
}
