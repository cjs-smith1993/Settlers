package client.map;

import java.util.*;

import shared.definitions.*;
import shared.locations.*;
import client.base.*;
import client.data.*;
import clientBackend.model.Board;
import clientBackend.model.Facade;

/**
 * Implementation for the map controller
 */
public class MapController extends Controller implements IMapController, Observer {

	private IRobView robView;
	private Facade facade;

	public MapController(IMapView view, IRobView robView) {

		super(view);

		this.setRobView(robView);

		this.facade = Facade.getInstance();

		this.initFromModel();
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
		Board board = this.facade.getBoard();
		board.getRobberLocation();

		//<temp>

		Random rand = new Random();

		for (int x = 0; x <= 3; ++x) {

			int maxY = 3 - x;
			for (int y = -3; y <= maxY; ++y) {
				int r = rand.nextInt(HexType.values().length);
				HexType hexType = HexType.values()[r];
				HexLocation hexLoc = new HexLocation(x, y);
				this.getView().addHex(hexLoc, hexType);
				this.getView().placeRoad(new EdgeLocation(hexLoc, EdgeDirection.NorthWest),
						CatanColor.RED);
				this.getView().placeRoad(new EdgeLocation(hexLoc, EdgeDirection.SouthWest),
						CatanColor.BLUE);
				this.getView().placeRoad(new EdgeLocation(hexLoc, EdgeDirection.South),
						CatanColor.ORANGE);
				this.getView().placeSettlement(
						new VertexLocation(hexLoc, VertexDirection.NorthWest), CatanColor.GREEN);
				this.getView().placeCity(new VertexLocation(hexLoc, VertexDirection.NorthEast),
						CatanColor.PURPLE);
			}

			if (x != 0) {
				int minY = x - 3;
				for (int y = minY; y <= 3; ++y) {
					int r = rand.nextInt(HexType.values().length);
					HexType hexType = HexType.values()[r];
					HexLocation hexLoc = new HexLocation(-x, y);
					this.getView().addHex(hexLoc, hexType);
					this.getView().placeRoad(new EdgeLocation(hexLoc, EdgeDirection.NorthWest),
							CatanColor.RED);
					this.getView().placeRoad(new EdgeLocation(hexLoc, EdgeDirection.SouthWest),
							CatanColor.BLUE);
					this.getView().placeRoad(new EdgeLocation(hexLoc, EdgeDirection.South),
							CatanColor.ORANGE);
					this.getView()
							.placeSettlement(new VertexLocation(hexLoc, VertexDirection.NorthWest),
									CatanColor.GREEN);
					this.getView().placeCity(new VertexLocation(hexLoc, VertexDirection.NorthEast),
							CatanColor.PURPLE);
				}
			}
		}

		PortType portType = PortType.BRICK;
		this.getView().addPort(new EdgeLocation(new HexLocation(0, 3), EdgeDirection.North),
				portType);
		this.getView().addPort(new EdgeLocation(new HexLocation(0, -3), EdgeDirection.South),
				portType);
		this.getView().addPort(new EdgeLocation(new HexLocation(-3, 3), EdgeDirection.NorthEast),
				portType);
		this.getView().addPort(new EdgeLocation(new HexLocation(-3, 0), EdgeDirection.SouthEast),
				portType);
		this.getView().addPort(new EdgeLocation(new HexLocation(3, -3), EdgeDirection.SouthWest),
				portType);
		this.getView().addPort(new EdgeLocation(new HexLocation(3, 0), EdgeDirection.NorthWest),
				portType);

		this.getView().placeRobber(new HexLocation(0, 0));

		this.getView().addNumber(new HexLocation(-2, 0), 2);
		this.getView().addNumber(new HexLocation(-2, 1), 3);
		this.getView().addNumber(new HexLocation(-2, 2), 4);
		this.getView().addNumber(new HexLocation(-1, 0), 5);
		this.getView().addNumber(new HexLocation(-1, 1), 6);
		this.getView().addNumber(new HexLocation(1, -1), 8);
		this.getView().addNumber(new HexLocation(1, 0), 9);
		this.getView().addNumber(new HexLocation(2, -2), 10);
		this.getView().addNumber(new HexLocation(2, -1), 11);
		this.getView().addNumber(new HexLocation(2, 0), 12);

		//</temp>
	}

	public boolean canPlaceRoad(EdgeLocation edgeLoc) {
		return true;
	}

	public boolean canPlaceSettlement(VertexLocation vertLoc) {
		return true;
	}

	public boolean canPlaceCity(VertexLocation vertLoc) {
		return true;
	}

	public boolean canPlaceRobber(HexLocation hexLoc) {
		return true;
	}

	public void placeRoad(EdgeLocation edgeLoc) {
		this.getView().placeRoad(edgeLoc, CatanColor.ORANGE);
	}

	public void placeSettlement(VertexLocation vertLoc) {
		this.getView().placeSettlement(vertLoc, CatanColor.ORANGE);
	}

	public void placeCity(VertexLocation vertLoc) {
		this.getView().placeCity(vertLoc, CatanColor.ORANGE);
	}

	public void placeRobber(HexLocation hexLoc) {
		this.getView().placeRobber(hexLoc);
		this.getRobView().showModal();
	}

	public void startMove(PieceType pieceType, boolean isFree, boolean allowDisconnected) {
		this.getView().startDrop(pieceType, CatanColor.ORANGE, true);
	}

	public void cancelMove() {

	}

	public void playSoldierCard() {

	}

	public void playRoadBuildingCard() {

	}

	public void robPlayer(RobPlayerInfo victim) {

	}

	@Override
	public void update(Observable o, Object arg) {
		this.initFromModel();
	}

}
