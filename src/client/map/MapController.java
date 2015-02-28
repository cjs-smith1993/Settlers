package client.map;

import java.util.*;

import shared.definitions.*;
import shared.locations.*;
import client.base.*;
import client.data.*;
import client.map.state.*;
import clientBackend.model.Facade;

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

		this.setState();
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
	}

	public boolean canPlaceRoad(EdgeLocation edgeLoc) {
		return this.state.canPlaceRoad(edgeLoc);
	}

	public boolean canPlaceSettlement(VertexLocation vertLoc) {
		return this.state.canPlaceSettlement(vertLoc);
	}

	public boolean canPlaceCity(VertexLocation vertLoc) {
		return this.state.canPlaceCity(vertLoc);
	}

	public boolean canPlaceRobber(HexLocation hexLoc) {
		return this.state.canPlaceRobber(hexLoc);
	}

	public void placeRoad(EdgeLocation edgeLoc) {
		this.state.placeRoad(edgeLoc);
	}

	public void placeSettlement(VertexLocation vertLoc) {
		this.state.placeSettlement(vertLoc);
	}

	public void placeCity(VertexLocation vertLoc) {
		this.state.placeCity(vertLoc);
	}

	public void placeRobber(HexLocation hexLoc) {
		this.state.placeRobber(hexLoc);
	}

	public void startMove(PieceType pieceType, boolean isFree, boolean allowDisconnected) {
		this.state.startMove(pieceType, isFree, allowDisconnected);
	}

	public void cancelMove() {
		this.state.cancelMove();
	}

	public void playSoldierCard() {
		this.state.playSoldierCard();
	}

	public void playRoadBuildingCard() {
		this.state.playRoadBuildingCard();
	}

	public void robPlayer(RobPlayerInfo victim) {
		this.state.robPlayer(victim);
	}

	public void setState() {
		IMapView view = this.getView();
		IRobView robView = this.getRobView();

		System.out.println(this.facade.getModelState());
		CatanState state = this.facade.getModelState();

		switch (state) {
		case FIRST_ROUND:
		case SECOND_ROUND:
			this.state = new SetupMapControllerState(this.facade, view, robView);
			break;
		case PLAYING:
			this.state = new PlayingMapControllerState(this.facade, view, robView);
			break;
		case ROBBING:
			this.state = new RobbingMapControllerState(this.facade, view, robView);
			break;
		case ROLLING:
		case DISCARDING:
		default:
			this.state = new DefaultMapControllerState(this.facade, view, robView);
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		this.setState();
		this.initFromModel();
	}
}