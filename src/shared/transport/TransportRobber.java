package shared.transport;

import shared.model.Robber;

public class TransportRobber {
	public int x;
	public int y;
	
	public TransportRobber() {}
	
	public TransportRobber(Robber robber) {
		this.x = robber.getLocation().getX();
		this.y = robber.getLocation().getY();
	}
}
