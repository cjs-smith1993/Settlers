package client.maritime;

import java.util.Vector;

import shared.definitions.*;
import client.base.*;
import clientBackend.model.Facade;


/**
 * Implementation for the maritime trade controller
 */
public class MaritimeTradeController extends Controller implements IMaritimeTradeController {

	private IMaritimeTradeOverlay tradeOverlay;
	private Facade facade;
	
	public MaritimeTradeController(IMaritimeTradeView tradeView, IMaritimeTradeOverlay tradeOverlay) {
		
		super(tradeView);

		setTradeOverlay(tradeOverlay);
		
		facade = Facade.getInstance();
	}
	
	public IMaritimeTradeView getTradeView() {
		
		return (IMaritimeTradeView)super.getView();
	}
	
	public IMaritimeTradeOverlay getTradeOverlay() {
		return tradeOverlay;
	}

	public void setTradeOverlay(IMaritimeTradeOverlay tradeOverlay) {
		this.tradeOverlay = tradeOverlay;
	}

	@Override
	public void startTrade() {
		
		Vector<ResourceType> enabledResources = new Vector<ResourceType>(); 
		ResourceType[] enabled = new ResourceType[5];
		int bestRatio = 4;
		PlayerNumber playerIndex = facade.getClientPlayerIndex();
		
		for(ResourceType type: ResourceType.values()) {
			switch (type) {
			case NONE:
				break;
			case ALL:
				break;
			default:
				bestRatio = facade.getBestMaritimeTradeRatio(playerIndex, type);
				if(facade.getResourceCount(type) >= bestRatio) 
					enabledResources.add(type);
				break;
			}
		}
		
		if(enabledResources.isEmpty())
			getTradeOverlay().setTradeEnabled(false);
		
		getTradeOverlay().showGiveOptions(enabledResources.toArray(enabled) );
		
		getTradeOverlay().showModal();
	}

	@Override
	public void makeTrade() {

		getTradeOverlay().closeModal();
	}

	@Override
	public void cancelTrade() {

		getTradeOverlay().closeModal();
	}

	@Override
	public void setGetResource(ResourceType resource) {
		System.out.printf("set get resource");
	}

	@Override
	public void setGiveResource(ResourceType resource) {
		System.out.printf("set give resource");
	}

	@Override
	public void unsetGetValue() {
		System.out.printf("unset get resource");
	}

	@Override
	public void unsetGiveValue() {
		System.out.printf("unset give resource");
	}

}

