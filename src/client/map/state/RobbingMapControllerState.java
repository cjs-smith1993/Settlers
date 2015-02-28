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

	public void robPlayer(RobPlayerInfo victim) {
		System.out.println("rob " + victim);
	}
}
