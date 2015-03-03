package client.map.state;

import shared.definitions.PieceType;
import shared.definitions.PlayerNumber;
import shared.locations.EdgeLocation;
import client.map.IMapView;
import client.map.IRobView;
import client.map.MapController;
import clientBackend.model.CatanException;
import clientBackend.model.Facade;
import clientBackend.model.Road;

public class RoadBuildingMapControllerState extends PlayingMapControllerState {

	private EdgeLocation firstRoadLocation;

	public RoadBuildingMapControllerState(
			Facade facade,
			MapController controller,
			IMapView view,
			IRobView robView) {
		super(facade, controller, view, robView);
	}

	public boolean canPlaceRoad(EdgeLocation edge) {
		try {
			if (this.firstRoadLocation != null) {
				Road road = new Road(this.facade.getClientPlayerIndex(), this.firstRoadLocation);
				if (this.facade.getBoard().getRoads().get(this.firstRoadLocation) == null) {
					this.facade.getBoard().placeRoad(road, this.firstRoadLocation, false);
				}
			}

			if (edge.getNormalizedLocation().equals(this.firstRoadLocation)) {
				return false;
			}

			return this.facade.canPlaceRoad(this.facade.getClientPlayerIndex(), edge, false);
		} catch (CatanException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void placeRoad(EdgeLocation edge) {
		if (this.firstRoadLocation == null) {
			this.firstRoadLocation = edge;
			this.view.placeRoad(edge, this.facade.getClientPlayerColor());
			this.startMove(PieceType.ROAD, true, false);
		}
		else {
			try {
				PlayerNumber clientIndex = this.facade.getClientPlayerIndex();
				this.facade.useRoadBuilding(clientIndex, this.firstRoadLocation, edge);
			} catch (CatanException e) {
				e.printStackTrace();
			}
		}
	}

	public void playRoadBuildingCard() {
		this.startMove(PieceType.ROAD, true, false);
	}

}
