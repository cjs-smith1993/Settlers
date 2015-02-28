package client.map.state;

import shared.locations.HexLocation;
import client.data.RobPlayerInfo;
import client.map.IMapView;
import client.map.IRobView;
import clientBackend.model.Facade;

public class RobbingMapControllerState extends DefaultMapControllerState {

	public RobbingMapControllerState(Facade facade, IMapView view, IRobView robView) {
		super(facade, view, robView);
	}

	public boolean canPlaceRobber(HexLocation hex) {
		return this.facade.canPlaceRobber(this.facade.getClientPlayerIndex(), hex);
	}

	public void placeRobber(HexLocation hex) {
		if (this.canPlaceRobber(hex)) {
			this.view.placeRobber(hex);
			this.robView.showModal();
		}
	}

	public void robPlayer(RobPlayerInfo victim) {

	}
}