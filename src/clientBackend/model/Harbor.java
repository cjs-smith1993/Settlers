package clientBackend.model;

import java.util.Collection;

import shared.definitions.*;
import shared.locations.*;

/**
 * Represents a harbor, allowing for maritime trade. Each player starts with
 * one harbor with resource ANY that trades at a 4:1 rate
 */
public class Harbor {
	private HexLocation location;
	private Collection<VertexLocation> ports;
	private ResourceType resource;
	private int ratio;
}
