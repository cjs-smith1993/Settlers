package client.map;

import java.util.*;

import shared.definitions.*;
import shared.locations.*;
import client.base.*;
import client.data.*;
import client.map.state.*;
import clientBackend.model.Board;
import clientBackend.model.BoardFactory;
import clientBackend.model.Chit;
import clientBackend.model.Dwelling;
import clientBackend.model.Facade;
import clientBackend.model.Harbor;
import clientBackend.model.Road;
import clientBackend.model.Tile;

/**
 * Implementation for the map controller
 */
public class MapController extends Controller implements IMapController, Observer {

	private IRobView robView;
	private Facade facade;
	private DefaultMapControllerState state;

	public MapController(IMapView view, IRobView robView) {
		super(view);

		this.setRobView(robView);

		this.facade = Facade.getInstance();
		this.facade.addObserver(this);

		this.state = new DefaultMapControllerState();
	}

	public IMapView getView() {
		return (IMapView) super.getView();
	}

	private IRobView getRobView() {
		return this.robView;
	}

	private void setRobView(IRobView robView) {
		this.robView = robView;
	}

	protected void initFromModel() {
		this.state.initFromModel();

		Board board = this.facade.getBoard();

		// setup tiles
		for (Tile tile : board.getTiles().values()) {
			HexLocation location = tile.getLocation();
			HexType type = TypeConverter.toHexType(tile.getResourceType());
			this.getView().addHex(location, type);
		}

		// setup chits
		for (Collection<Chit> collection : board.getChits().values()) {
			for (Chit chit : collection) {
				this.getView().addNumber(chit.getLocation(), chit.getNumber());
			}
		}

		// setup harbors
		ArrayList<EdgeLocation> mapViewEdges = BoardFactory.getPortLocations();
		for (Harbor harbor : board.getHarbors()) {
			VertexLocation[] ports = harbor.getPorts().toArray(new VertexLocation[0]);
			EdgeLocation edge = Geometer.getSharedEdge(ports[0], ports[1]);

			// view crap for orienting port correctly
			for (EdgeLocation viewEdge : mapViewEdges) {
				if (edge.getNormalizedLocation().equals(viewEdge.getNormalizedLocation())) {
					edge = new EdgeLocation(viewEdge.getHexLoc(), viewEdge.getDir());
				}
			}

			PortType type = TypeConverter.toPortType(harbor.getResource());
			this.getView().addPort(edge, type);
		}

		// setup roads
		for (Road road : board.getRoads().values()) {
			CatanColor color = this.facade.getPlayerColor(road.getOwner());
			this.getView().placeRoad(road.getLocation(), color);
		}

		// setup dwellings
		for (Dwelling dwelling : board.getDwellings().values()) {
			CatanColor color = this.facade.getPlayerColor(dwelling.getOwner());
			if (dwelling.getPropertyType() == PropertyType.SETTLEMENT) {
				this.getView().placeSettlement(dwelling.getLocation(), color);
			}
			else {
				this.getView().placeCity(dwelling.getLocation(), color);
			}
		}

		// setup robber
		this.getView().placeRobber(board.getRobberLocation());
	}

	public boolean canPlaceRoad(EdgeLocation edgeLoc) {
		System.out.println("can place road at " + edgeLoc);
		return true;
	}

	public boolean canPlaceSettlement(VertexLocation vertLoc) {
		System.out.println("can place settlement at " + vertLoc);
		return true;
	}

	public boolean canPlaceCity(VertexLocation vertLoc) {
		System.out.println("can place city at " + vertLoc);
		return true;
	}

	public boolean canPlaceRobber(HexLocation hexLoc) {
		System.out.println("can place robber at " + hexLoc);
		return true;
	}

	public void placeRoad(EdgeLocation edgeLoc) {
		System.out.println("place road at " + edgeLoc);
		this.getView().placeRoad(edgeLoc, CatanColor.ORANGE);
	}

	public void placeSettlement(VertexLocation vertLoc) {
		System.out.println("place settlement at " + vertLoc);
		this.getView().placeSettlement(vertLoc, CatanColor.ORANGE);
	}

	public void placeCity(VertexLocation vertLoc) {
		System.out.println("place city at " + vertLoc);
		this.getView().placeCity(vertLoc, CatanColor.ORANGE);
	}

	public void placeRobber(HexLocation hexLoc) {
		System.out.println("place robber at " + hexLoc);
		this.getView().placeRobber(hexLoc);
		this.getRobView().showModal();
	}

	public void startMove(PieceType pieceType, boolean isFree, boolean allowDisconnected) {
		System.out.println("start move of " + pieceType);
		this.getView().startDrop(pieceType, CatanColor.ORANGE, true);
	}

	public void cancelMove() {
		System.out.println("cancel move");
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

	@Override
	public void update(Observable o, Object arg) {
		this.initFromModel();
	}
}