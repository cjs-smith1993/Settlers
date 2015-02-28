package client.discard;

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
		
	}

	@Override
	public void decreaseAmount(ResourceType resource) {
		
	}

	@Override
	public void discard() {
		
		getDiscardView().closeModal();
	}
	
	public void initFromModel() {
		if (facade.needsToDiscardCards(facade.getClientPlayer().getPlayerIndex())) {
			int brick = facade.getResourceCount(ResourceType.BRICK);
			System.out.println("brick: " + brick);
			int ore = facade.getResourceCount(ResourceType.ORE);
			System.out.println("ore: " + ore);
			int wheat = facade.getResourceCount(ResourceType.WHEAT);
			System.out.println("wheat: " + wheat);
			int sheep = facade.getResourceCount(ResourceType.SHEEP);
			System.out.println("sheep: " + sheep);
			int wood = facade.getResourceCount(ResourceType.WOOD);
			System.out.println("wood: " + wood);
			
			this.getDiscardView().showModal();
			
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		this.initFromModel();
	}

}

