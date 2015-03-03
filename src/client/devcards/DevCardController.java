package client.devcards;

import shared.definitions.ResourceType;
import client.base.*;
import clientBackend.model.CatanException;
import clientBackend.model.Facade;

/**
 * "Dev card" controller implementation
 */
public class DevCardController extends Controller implements IDevCardController {

	private IBuyDevCardView buyCardView;
	private IAction soldierAction;
	private IAction roadAction;
	private Facade facade = Facade.getInstance();

	/**
	 * DevCardController constructor
	 *
	 * @param view
	 *            "Play dev card" view
	 * @param buyCardView
	 *            "Buy dev card" view
	 * @param soldierAction
	 *            Action to be executed when the user plays a soldier card. It
	 *            calls "mapController.playSoldierCard()".
	 * @param roadAction
	 *            Action to be executed when the user plays a road building
	 *            card. It calls "mapController.playRoadBuildingCard()".
	 */
	public DevCardController(IPlayDevCardView view, IBuyDevCardView buyCardView,
			IAction soldierAction, IAction roadAction) {

		super(view);

		this.buyCardView = buyCardView;
		this.soldierAction = soldierAction;
		this.roadAction = roadAction;
	}

	public IPlayDevCardView getPlayCardView() {
		return (IPlayDevCardView) super.getView();
	}

	public IBuyDevCardView getBuyCardView() {
		return this.buyCardView;
	}

	@Override
	public void startBuyCard() {
		this.getBuyCardView().showModal();
	}

	@Override
	public void cancelBuyCard() {
		this.getBuyCardView().closeModal();
	}

	@Override
	public void buyCard() {
		try {
			this.facade.buyDevCard(this.facade.getClientPlayerIndex());
		} catch (CatanException e) {
			e.printStackTrace();
		}

		this.getBuyCardView().closeModal();
	}

	@Override
	public void startPlayCard() {
		this.getPlayCardView().showModal();
	}

	@Override
	public void cancelPlayCard() {
		this.getPlayCardView().closeModal();
	}

	@Override
	public void playMonopolyCard(ResourceType resource) {
		try {
			if (this.facade.canUseMonopoly(this.facade.getClientPlayerIndex())) {
				this.facade.useMonopoly(this.facade.getClientPlayerIndex(), resource);
			}
		} catch (CatanException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void playMonumentCard() {
		try {
			if (this.facade.canUseMonument(this.facade.getClientPlayerIndex())) {
				this.facade.useMonument(this.facade.getClientPlayerIndex());
			}
		} catch (CatanException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void playRoadBuildCard() {
		try {
			if (this.facade.canUseRoadBuilding(this.facade.getClientPlayerIndex())) {
				this.facade.setRoadBuildingPlayed(true);
				this.facade.getModel(false);
				this.roadAction.execute();
			}
		} catch (CatanException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void playSoldierCard() {
		try {
			if (this.facade.canUseSoldier(this.facade.getClientPlayerIndex())) {
				this.soldierAction.execute();
			}
		} catch (CatanException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void playYearOfPlentyCard(ResourceType resource1, ResourceType resource2) {
		System.out.println("Played Year of Plenty. Selected resources: " + resource1.toString()
				+ ", " + resource2.toString());
		try {
			if (this.facade.canUseYearOfPlenty(this.facade.getClientPlayerIndex())) {
				this.facade.useYearOfPlenty(this.facade.getClientPlayerIndex(), resource1,
						resource2);
			}
		} catch (CatanException e) {
			e.printStackTrace();
		}
	}
}
