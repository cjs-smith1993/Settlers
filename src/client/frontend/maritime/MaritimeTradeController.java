package client.frontend.maritime;

import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import client.backend.ClientFacade;
import client.frontend.base.*;
import shared.definitions.*;
import shared.model.CatanException;
import shared.model.ResourceInvoice;


/**
 * Implementation for the maritime trade controller
 */
public class MaritimeTradeController extends Controller implements IMaritimeTradeController, Observer {

	private IMaritimeTradeOverlay tradeOverlay;
	private ClientFacade facade;
	private int giveAmount = 0;
	private ResourceType giveResource;
	private ResourceType getResource;
	
	public MaritimeTradeController(IMaritimeTradeView tradeView, IMaritimeTradeOverlay tradeOverlay) {
		
		super(tradeView);

		setTradeOverlay(tradeOverlay);
		
		facade = ClientFacade.getInstance();
		facade.addObserver(this);
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
				if(facade.getResourceCount(playerIndex, type) >= bestRatio) 
					enabledResources.add(type);
				break;
			}
		}
		
		getTradeOverlay().setTradeEnabled(false);
		this.getTradeOverlay().hideGetOptions();
		getTradeOverlay().showGiveOptions(enabledResources.toArray(enabled) );
		if(this.getTradeOverlay().isModalShowing()) {
			this.getTradeOverlay().closeModal();
		}
		
		getTradeOverlay().showModal();
	}

	@Override
	public void makeTrade() {
		PlayerNumber playerIndex = facade.getClientPlayerIndex();
		
		ResourceInvoice invoice = new ResourceInvoice(playerIndex, PlayerNumber.BANK);
		for(ResourceType type: ResourceType.values()) {
			if(type == giveResource) {
				switch(type) {
				case BRICK:
					invoice.setBrick(giveAmount);
					break;
				case WOOD:
					invoice.setWood(giveAmount);
					break;
				case WHEAT:
					invoice.setWheat(giveAmount);
					break;
				case ORE:
					invoice.setOre(giveAmount);
					break;
				case SHEEP:
					invoice.setSheep(giveAmount);
					break;
				default:
					break;
				}
			}
			if(type == getResource) {
				switch(type) {
				case BRICK:
					invoice.setBrick(-1);
					break;
				case WOOD:
					invoice.setWood(-1);
					break;
				case WHEAT:
					invoice.setWheat(-1);
					break;
				case ORE:
					invoice.setOre(-1);
					break;
				case SHEEP:
					invoice.setSheep(-1);
					break;
				default:
					break;
				}
			}
		}
		try {
			facade.maritimeTrade(playerIndex, giveAmount, giveResource, getResource);
		} catch (CatanException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getTradeOverlay().closeModal();
	}

	@Override
	public void cancelTrade() {

		getTradeOverlay().closeModal();
	}

	@Override
	public void setGetResource(ResourceType resource) {
		getResource = resource;
		this.getTradeOverlay().selectGetOption(resource, 1);
		this.getTradeOverlay().setTradeEnabled(true);
		//System.out.printf("set get resource");
	}

	@Override
	public void setGiveResource(ResourceType resource) {
		giveResource = resource;
		PlayerNumber playerIndex = facade.getClientPlayerIndex();
		int amount = facade.getBestMaritimeTradeRatio(playerIndex, resource);
		giveAmount = amount;
		Vector<ResourceType> enabledResources = new Vector<ResourceType>(); 
		ResourceType[] enabled = new ResourceType[5];
		
		enabledResources.add(ResourceType.BRICK);
		enabledResources.add(ResourceType.WOOD);
		enabledResources.add(ResourceType.WHEAT);
		enabledResources.add(ResourceType.ORE);
		enabledResources.add(ResourceType.SHEEP);
		this.getTradeOverlay().showGetOptions(enabledResources.toArray(enabled));
		
		this.getTradeOverlay().selectGiveOption(resource, amount);
		
		//System.out.printf("set give resource");
	}

	@Override
	public void unsetGetValue() {
		this.getTradeOverlay().hideGetOptions();
		Vector<ResourceType> enabledResources = new Vector<ResourceType>(); 
		ResourceType[] enabled = new ResourceType[5];
		
		enabledResources.add(ResourceType.BRICK);
		enabledResources.add(ResourceType.WOOD);
		enabledResources.add(ResourceType.WHEAT);
		enabledResources.add(ResourceType.ORE);
		enabledResources.add(ResourceType.SHEEP);
		this.getTradeOverlay().showGetOptions(enabledResources.toArray(enabled));
		this.getTradeOverlay().setTradeEnabled(false);
		//System.out.printf("unset get resource");
	}

	@Override
	public void unsetGiveValue() {
		this.getTradeOverlay().hideGiveOptions();
		this.unsetGetValue();
		this.startTrade();
		//this.getTradeOverlay().setTradeEnabled(false);
		//System.out.printf("unset give resource");
	}

	@Override
	public void update(Observable o, Object arg) {
		MaritimeTradeView view = (MaritimeTradeView) super.getView();
		if (facade.isClientTurn() && (this.facade.getModelState() == CatanState.PLAYING)) {
			view.enableMaritimeTrade(true);
		}
		else {
			view.enableMaritimeTrade(false);
		}
		
	}

}

