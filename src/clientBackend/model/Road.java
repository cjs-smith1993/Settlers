package clientBackend.model;

import shared.definitions.*;
import shared.locations.*;

/**
 * Represents a road. The owner of the road may build a dwelling on a vertex
 * that is connected to the road
 */
public class Road {
	private EdgeLocation location;
	private PlayerNumber owner;
}
