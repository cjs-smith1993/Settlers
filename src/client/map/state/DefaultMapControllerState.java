package client.map.state;

import java.util.Collection;

import client.data.RobPlayerInfo;
import client.map.IMapView;
import client.map.IRobView;
import client.map.TypeConverter;
import clientBackend.model.Board;
import clientBackend.model.Chit;
import clientBackend.model.Dwelling;
import clientBackend.model.Facade;
import clientBackend.model.Harbor;
import clientBackend.model.Road;
import clientBackend.model.Tile;
import shared.definitions.CatanColor;
import shared.definitions.HexType;
import shared.definitions.PieceType;
import shared.definitions.PortType;
import shared.definitions.PropertyType;
import shared.locations.*;

public class DefaultMapControllerState {

	protected Facade facade;
	protected IMapView view;
	protected IRobView robView;

	public DefaultMapControllerState(Facade facade, IMapView view, IRobView robView) {
		this.facade = facade;
		this.view = view;
		this.robView = robView;
	}

	public void initFromModel() {
		if (!this.facade.isGameReady()) {
			return;
		}

		Board board = this.facade.getBoard();

		// setup tiles
		for (Tile tile : board.getTiles().values()) {
			HexLocation location = tile.getLocation();
			HexType type = TypeConverter.toHexType(tile.getResourceType());
			this.view.addHex(location, type);
		}

		// setup chits
		for (Collection<Chit> collection : board.getChits().values()) {
			for (Chit chit : collection) {
				this.view.addNumber(chit.getLocation(), chit.getNumber());
			}
		}

		// setup harbors
		for (Harbor harbor : board.getHarbors()) {
			VertexLocation[] ports = harbor.getPorts().toArray(new VertexLocation[0]);
			EdgeLocation edge = Geometer.getSharedEdge(ports[0], ports[1]);
			PortType type = TypeConverter.toPortType(harbor.getResource());
			this.view.addPort(edge, type);
		}

		// setup roads
		for (Road road : board.getRoads().values()) {
			CatanColor color = this.facade.getPlayerColor(road.getOwner());
			this.view.placeRoad(road.getLocation(), color);
		}

		// setup dwellings
		for (Dwelling dwelling : board.getDwellings().values()) {
			CatanColor color = this.facade.getPlayerColor(dwelling.getOwner());
			if (dwelling.getPropertyType() == PropertyType.SETTLEMENT) {
				this.view.placeSettlement(dwelling.getLocation(), color);
			}
			else {
				this.view.placeCity(dwelling.getLocation(), color);
			}
		}

		// setup robber
		this.view.placeRobber(board.getRobberLocation());
	}

	public boolean canPlaceRoad(EdgeLocation edge) {
		return false;
	}

	public void placeRoad(EdgeLocation edge) {

	}

	public boolean canPlaceSettlement(VertexLocation vertex) {
		return false;
	}

	public void placeSettlement(VertexLocation vertex) {

	}

	public boolean canPlaceCity(VertexLocation vertex) {
		return false;
	}

	public void placeCity(VertexLocation vertex) {

	}

	public boolean canPlaceRobber(HexLocation hex) {
		return false;
	}

	public void placeRobber(HexLocation hex) {

	}

	public void startMove(PieceType pieceType, boolean isFree, boolean allowDisconnected) {

	}

	public void cancelMove() {

	}

	public void playSoldierCard() {

	}

	public void playRoadBuildingCard() {

	}

	public void robPlayer(RobPlayerInfo victim) {

	}
}
