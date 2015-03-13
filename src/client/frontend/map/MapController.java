package client.frontend.map;

import java.util.*;

import client.backend.ClientFacade;
import client.frontend.base.*;
import client.frontend.data.*;
import client.frontend.map.state.*;
import shared.definitions.*;
import shared.locations.*;

/**
 * Implementation for the map controller
 */
public class MapController extends Controller implements IMapController, Observer {

	private IRobView robView;
	private ClientFacade facade;
	private DefaultMapControllerState state;
	private boolean isModalShowing = false;
	private HexLocation robberLocation;

	public MapController(IMapView view, IRobView robView) {
		super(view);

		this.setRobView(robView);

		this.facade = ClientFacade.getInstance();
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

	public boolean isModalShowing() {
		return this.isModalShowing;
	}

	public void setModalShowing(boolean visible) {
		this.isModalShowing = visible;
	}

	public HexLocation getRobberLocation() {
		return this.robberLocation;
	}

	public void setRobberLocation(HexLocation hex) {
		this.robberLocation = hex;
	}

	public void setState() {
		IMapView view = this.getView();
		IRobView robView = this.getRobView();

		CatanState state = this.facade.getModelState();

		switch (state) {
		case FIRST_ROUND:
		case SECOND_ROUND:
			this.state = new SetupMapControllerState(this.facade, this, view, robView);
			break;
		case PLAYING:
			if (this.facade.roadBuildingPlayed()) {
				this.state = new RoadBuildingMapControllerState(this.facade, this, view, robView);
			}
			else {
				this.state = new PlayingMapControllerState(this.facade, this, view, robView);
			}
			break;
		case ROBBING:
			this.state = new RobbingMapControllerState(this.facade, this, view, robView);
			break;
		case ROLLING:
		case DISCARDING:
		default:
			this.state = new DefaultMapControllerState(this.facade, this, view, robView);
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		this.setState();
		this.initFromModel();
	}
}