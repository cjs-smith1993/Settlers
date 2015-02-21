package client.resources;

import java.util.*;

import shared.definitions.PropertyType;
import shared.definitions.ResourceType;
import client.base.*;
import clientBackend.model.Facade;

/**
 * Implementation for the resource bar controller
 */
public class ResourceBarController extends Controller implements IResourceBarController, Observer {

	private Map<ResourceBarElement, IAction> elementActions;
	private Facade facade = Facade.getInstance();
	
	public ResourceBarController(IResourceBarView view) {

		super(view);
		
		// TODO: Remove me.
		update(null, null);
		
		elementActions = new HashMap<ResourceBarElement, IAction>();
		
		elementActions.put(ResourceBarElement.ROAD, new IAction() {

			@Override
			public void execute() {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	public IResourceBarView getView() {
		return (IResourceBarView)super.getView();
	}

	/**
	 * Sets the action to be executed when the specified resource bar element is clicked by the user
	 * 
	 * @param element The resource bar element with which the action is associated
	 * @param action The action to be executed
	 */
	public void setElementAction(ResourceBarElement element, IAction action) {

		elementActions.put(element, action);
	}

	@Override
	public void buildRoad() {
		executeElementAction(ResourceBarElement.ROAD);
	}

	@Override
	public void buildSettlement() {
		executeElementAction(ResourceBarElement.SETTLEMENT);
	}

	@Override
	public void buildCity() {
		executeElementAction(ResourceBarElement.CITY);
	}

	@Override
	public void buyCard() {
		executeElementAction(ResourceBarElement.BUY_CARD);
	}

	@Override
	public void playCard() {
		executeElementAction(ResourceBarElement.PLAY_CARD);
	}
	
	private void executeElementAction(ResourceBarElement element) {
		if (elementActions.containsKey(element)) {
			
			IAction action = elementActions.get(element);
			action.execute();
		}
	}

	/**
	 * @param Observable The observable object.
	 * @param Object Should be null.
	 */
	@Override
	public void update(Observable o, Object arg) {
		IResourceBarView view = getView();
		
		setResourceCount(view);
		
		int roadCount = facade.getHoldingCount(PropertyType.ROAD);
		int settlementCount = facade.getHoldingCount(PropertyType.SETTLEMENT);
		int cityCount = facade.getHoldingCount(PropertyType.CITY);
		
		setHoldingCount(view, roadCount, settlementCount, cityCount);
		setEnabledState(roadCount, settlementCount, cityCount);
	}
	
	private void setHoldingCount(IResourceBarView view, int roadCount, int settlementCount, int cityCount) {
		view.setElementAmount(ResourceBarElement.ROAD, roadCount);
		view.setElementAmount(ResourceBarElement.SETTLEMENT, settlementCount);
		view.setElementAmount(ResourceBarElement.CITY, cityCount);
	}
	
	private void setResourceCount(IResourceBarView view) {
		view.setElementAmount(ResourceBarElement.BRICK, facade.getResourceCount(ResourceType.BRICK));
		view.setElementAmount(ResourceBarElement.ORE, facade.getResourceCount(ResourceType.ORE));
		view.setElementAmount(ResourceBarElement.SHEEP, facade.getResourceCount(ResourceType.SHEEP));
		view.setElementAmount(ResourceBarElement.WHEAT, facade.getResourceCount(ResourceType.WHEAT));
		view.setElementAmount(ResourceBarElement.WOOD, facade.getResourceCount(ResourceType.WOOD));
	}
	
	private void setEnabledState(int roadCount, int settlementCount, int cityCount) {
		IResourceBarView view = getView();
		
		if (roadCount > 0) {
			view.setElementEnabled(ResourceBarElement.ROAD, true);
		}
		else {
			view.setElementEnabled(ResourceBarElement.ROAD, false);
		}
		
		if (settlementCount > 0) {
			view.setElementEnabled(ResourceBarElement.SETTLEMENT, true);
		}
		else {
			view.setElementEnabled(ResourceBarElement.SETTLEMENT, false);
		}
		
		if (cityCount > 0) {
			view.setElementEnabled(ResourceBarElement.CITY, true);
		}
		else {
			view.setElementEnabled(ResourceBarElement.CITY, false);
		}
	}
}

