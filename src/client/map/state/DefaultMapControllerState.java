package client.map.state;

import client.data.RobPlayerInfo;
import shared.definitions.PieceType;
import shared.locations.*;

public class DefaultMapControllerState {

	public void initFromModel() {

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
