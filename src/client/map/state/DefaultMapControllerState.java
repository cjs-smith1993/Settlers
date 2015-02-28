package client.map.state;

import java.util.Collection;

import client.data.RobPlayerInfo;
import client.map.IMapView;
import client.map.IRobView;
import client.map.TypeConverter;
import clientBackend.model.Board;
import clientBackend.model.CatanException;
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

	private Facade facade;
	private IMapView view;
	private IRobView robView;

	public DefaultMapControllerState(Facade facade, IMapView view, IRobView robView) {
		this.facade = facade;
		this.view = view;
		this.robView = robView;
	}

	public void initFromModel() {

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
		System.out.println("can place road at " + edge);
		return this.facade.canPlaceRoad(this.facade.getClientPlayerIndex(), edge, false);
	}

	public void placeRoad(EdgeLocation edge) {
		System.out.println("place road at " + edge);
		if (this.canPlaceRoad(edge)) {
			try {
				this.facade.buildRoad(this.facade.getClientPlayerIndex(), edge, false, false);
				this.view.placeRoad(edge, CatanColor.ORANGE);
			} catch (CatanException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean canPlaceSettlement(VertexLocation vertex) {
		System.out.println("can place settlement at " + vertex);
		return this.facade.canPlaceSettlement(this.facade.getClientPlayerIndex(), vertex, false);
	}

	public void placeSettlement(VertexLocation vertex) {
		System.out.println("place settlement at " + vertex);
		if (this.canPlaceSettlement(vertex)) {
			try {
				this.facade.buildSettlement(this.facade.getClientPlayerIndex(), vertex, false,
						false);
				this.view.placeSettlement(vertex, CatanColor.ORANGE);
			} catch (CatanException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean canPlaceCity(VertexLocation vertex) {
		System.out.println("can place city at " + vertex);
		return this.facade.canPlaceCity(this.facade.getClientPlayerIndex(), vertex);
	}

	public void placeCity(VertexLocation vertex) {
		System.out.println("place city at " + vertex);
		if (this.canPlaceCity(vertex)) {
			try {
				this.facade.buildCity(this.facade.getClientPlayerIndex(), vertex);
				this.view.placeCity(vertex, CatanColor.ORANGE);
			} catch (CatanException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean canPlaceRobber(HexLocation hex) {
		System.out.println("can place robber at " + hex);
		return this.facade.canPlaceRobber(this.facade.getClientPlayerIndex(), hex);
	}

	public void placeRobber(HexLocation hex) {
		System.out.println("place robber at " + hex);
		if (this.canPlaceRobber(hex)) {
			this.view.placeRobber(hex);
			this.robView.showModal();
		}
	}

	public void startMove(PieceType pieceType, boolean isFree, boolean allowDisconnected) {
		System.out.println("start move of " + pieceType);
		this.view.startDrop(pieceType, this.facade.getClientPlayer().getColor(), true);
	}

	public void cancelMove() {
		System.out.println("cancel move");
		//		this.view.
	}

	public void playSoldierCard() {
		System.out.println("play soldier");
	}

	public void playRoadBuildingCard() {
		System.out.println("play road building");
	}

	public void robPlayer(RobPlayerInfo victim) {
		System.out.println("rob " + victim);
	}
}
