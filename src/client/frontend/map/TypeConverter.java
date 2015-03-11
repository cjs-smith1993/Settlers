package client.frontend.map;

import shared.definitions.HexType;
import shared.definitions.PortType;
import shared.definitions.ResourceType;

public class TypeConverter {
	public static HexType toHexType(ResourceType type) {
		switch (type) {
		case WOOD:
			return HexType.WOOD;
		case BRICK:
			return HexType.BRICK;
		case SHEEP:
			return HexType.SHEEP;
		case WHEAT:
			return HexType.WHEAT;
		case ORE:
			return HexType.ORE;
		case ALL:
			return HexType.WATER;
		case NONE:
			return HexType.DESERT;
		default:
			return null;
		}
	}

	public static PortType toPortType(ResourceType type) {
		if (type == null) {
			return PortType.THREE;
		}
		switch (type) {
		case WOOD:
			return PortType.WOOD;
		case BRICK:
			return PortType.BRICK;
		case SHEEP:
			return PortType.SHEEP;
		case WHEAT:
			return PortType.WHEAT;
		case ORE:
			return PortType.ORE;
		default:
			return null;
		}
	}
}
