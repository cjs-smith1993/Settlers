package client.discard;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import shared.definitions.*;
import client.base.*;
import client.misc.*;
import clientBackend.model.CatanException;
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
		this.currentNumberToDiscard = 0;
		
		this.maxResources = new HashMap<ResourceType, Integer>();
		this.resourcesToDiscard = new HashMap<ResourceType, Integer>();
		// Initialize discarding list to 0 for each resource
		for (ResourceType type : ResourceType.values()) {
			this.resourcesToDiscard.put(type, 0);
		}
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
		
		this.determineDiscardButton();
		this.determineUpDownArrows(resource);
	}

	@Override
	public void decreaseAmount(ResourceType resource) {
		int currentNumber = this.resourcesToDiscard.get(resource);
		currentNumber--;
		this.currentNumberToDiscard--;
		this.resourcesToDiscard.put(resource, currentNumber);
		this.getDiscardView().setResourceDiscardAmount(resource, currentNumber);
		
		this.determineDiscardButton();
		this.determineUpDownArrows(resource);
	}
	
	/**
	 * Determines the status of the "discard" button
	 */
	private void determineDiscardButton() {
		// If the player has selected the number of cards they need to discard, disable all up arrows
		if (this.currentNumberToDiscard >= this.numberToDiscard) {
			this.getDiscardView().setDiscardButtonEnabled(true);
			for (ResourceType type : ResourceType.values()) {
				this.getDiscardView().setResourceAmountChangeEnabled(type, false, resourcesToDiscard.get(type) > 0);
			}
		}
		else {
			this.getDiscardView().setDiscardButtonEnabled(false);
		}
		this.getDiscardView().setStateMessage("Discard: " + this.currentNumberToDiscard + "/" + this.numberToDiscard);
	}
	
	/**
	 * Determines whether the up/down arrows should be visible
	 * @param type
	 */
	private void determineUpDownArrows(ResourceType type) {
		int currentlyDiscarding = this.resourcesToDiscard.get(type);
		int maxToDiscard = this.maxResources.get(type);
		this.getDiscardView().setResourceAmountChangeEnabled(type, currentlyDiscarding < maxToDiscard, currentlyDiscarding > 0);
	}

	/**
	 * Reset resourcesToDiscard to 0
	 */
	private void resetDiscardCards() {
		for (ResourceType type : ResourceType.values()) {
			this.resourcesToDiscard.put(type, 0);
		}
	}
	
	@Override
	public void discard() {
		
		getDiscardView().closeModal();
		
		try {
			this.facade.discardCards(this.facade.getClientPlayerIndex(), 
					this.resourcesToDiscard.get(ResourceType.BRICK), 
					this.resourcesToDiscard.get(ResourceType.ORE),
					this.resourcesToDiscard.get(ResourceType.SHEEP),
					this.resourcesToDiscard.get(ResourceType.WHEAT),
					this.resourcesToDiscard.get(ResourceType.WOOD));
			this.resetDiscardCards();
		} catch (CatanException e) {
			e.printStackTrace();
		}
	}

	public void initFromModel() {
		PlayerNumber clientPlayer = facade.getClientPlayerIndex();
		if (this.facade.needsToDiscardCards(clientPlayer)) {
			
			this.numberToDiscard = this.facade.getNumberToDiscard(clientPlayer);
			
			// Get max number of each resource to discard
			for (ResourceType type : ResourceType.values()) {
				int max = this.facade.getResourceCount(clientPlayer, type);
				this.maxResources.put(type, max);
				this.getDiscardView().setResourceAmountChangeEnabled(type, max > 0, false);// Get max number of each resource to discard
				this.getDiscardView().setResourceMaxAmount(type, maxResources.get(type));// Set max number of each resource on the view
				this.getDiscardView().setResourceDiscardAmount(type, resourcesToDiscard.get(type));// Set current number of resources to discard on the view
				this.determineUpDownArrows(type);
			}
			
			this.determineDiscardButton();
			
			if (!this.getDiscardView().isModalShowing()) {// If the view is not already visible
				System.out.println("DiscardView is showing now");
				this.getDiscardView().showModal();
			}
			
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		this.initFromModel();
	}

}

