package client.discard;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import shared.definitions.*;
import client.base.*;
import client.misc.*;
import clientBackend.model.Facade;


/**
 * Discard controller implementation
 */
public class DiscardController extends Controller implements IDiscardController, Observer {

	private IWaitView waitView;
	private Facade facade;
	
	private int numberToDiscard;
	private int currentNumberToDiscard;
	private Map<ResourceType, Integer> maxResources;
	private Map<ResourceType, Integer> resourcesToDiscard;
	
	/**
	 * DiscardController constructor
	 * 
	 * @param view View displayed to let the user select cards to discard
	 * @param waitView View displayed to notify the user that they are waiting for other players to discard
	 */
	public DiscardController(IDiscardView view, IWaitView waitView) {
		
		super(view);
		
		this.waitView = waitView;
		
		this.facade = Facade.getInstance();
		this.facade.addObserver(this);
		
	}

	public IDiscardView getDiscardView() {
		return (IDiscardView)super.getView();
	}
	
	public IWaitView getWaitView() {
		return waitView;
	}

	@Override
	public void increaseAmount(ResourceType resource) {
		int currentNumber = this.resourcesToDiscard.get(resource);
		currentNumber++;
		this.currentNumberToDiscard++;
		this.resourcesToDiscard.put(resource, currentNumber);
		this.getDiscardView().setResourceDiscardAmount(resource, currentNumber);
		
		if (currentNumber == this.maxResources.get(resource)) {// are we now at the max amount for this resource?
			this.getDiscardView().setResourceAmountChangeEnabled(resource, false, true);
		}
		
		if (this.currentNumberToDiscard == this.numberToDiscard) {
			for (ResourceType type : ResourceType.values()) {
				this.getDiscardView().setResourceAmountChangeEnabled(resource, false, resourcesToDiscard.get(type) > 0);
			}
		}
	}

	@Override
	public void decreaseAmount(ResourceType resource) {
		int currentNumber = this.resourcesToDiscard.get(resource);
		currentNumber--;
		this.currentNumberToDiscard--;
		this.resourcesToDiscard.put(resource, currentNumber);
		this.getDiscardView().setResourceDiscardAmount(resource, currentNumber);
		
		if (currentNumber == this.maxResources.get(resource)) {// are we now down to zero for this resource?
			this.getDiscardView().setResourceAmountChangeEnabled(resource, true, false);
		}
	}

	@Override
	public void discard() {
		
		getDiscardView().closeModal();
	}

	public void initFromModel() {
		PlayerNumber clientPlayer = facade.getClientPlayerIndex();
		if (this.facade.needsToDiscardCards(clientPlayer)) {
			
			this.numberToDiscard = this.facade.getNumberToDiscard(clientPlayer);
			this.currentNumberToDiscard = 0;
			
			// Get max number of each resource to discard
			for (ResourceType type : ResourceType.values()) {
				int max = this.facade.getResourceCount(clientPlayer, type);
				this.maxResources.put(type, max);
				this.getDiscardView().setResourceAmountChangeEnabled(type, max > 0, false);
			}
			
			// Set max number of each resource on the view
			for (ResourceType type : ResourceType.values()) {
				this.getDiscardView().setResourceMaxAmount(type, maxResources.get(type));
			}
			
			// Initialize discarding list to 0 for each resource
			for (ResourceType type : ResourceType.values()) {
				this.resourcesToDiscard.put(type, 0);
			}
			
			this.getDiscardView().showModal();
			
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		this.initFromModel();
	}

}

