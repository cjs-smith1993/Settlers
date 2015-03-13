package client.frontend.domestic;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import client.backend.ClientFacade;
import client.frontend.base.*;
import client.frontend.data.PlayerInfo;
import client.frontend.misc.*;
import shared.definitions.*;
import shared.model.CatanException;
import shared.model.ResourceInvoice;


/**
 * Domestic trade controller implementation
 */
public class DomesticTradeController extends Controller implements IDomesticTradeController, Observer {

	@SuppressWarnings("unused")
	private static final boolean tradeReceiver = false;
	private IDomesticTradeOverlay tradeOverlay;
	private IWaitView waitOverlay;
	private IAcceptTradeOverlay acceptOverlay;
	private ClientFacade facade;
	private int brick = 0;
	private int wood = 0;
	private int wheat = 0;
	private int ore = 0;
	private int sheep = 0;
	private PlayerNumber clientNumber;
	
	private  PlayerNumber tradeReciever = PlayerNumber.BANK;
	private Map<ResourceType, Integer> sendingMap = new HashMap<ResourceType, Integer>();
	private Map<ResourceType, Integer> receivingMap = new HashMap<ResourceType, Integer>();
	
	private Vector<PlayerInfo> otherPlayers = new Vector<PlayerInfo>();
	private PlayerInfo[] info = new PlayerInfo[3];
	private ResourceInvoice tradingInvoice;
	private boolean setup;

	/**
	 * DomesticTradeController constructor
	 * 
	 * @param tradeView Domestic trade view (i.e., view that contains the "Domestic Trade" button)
	 * @param tradeOverlay Domestic trade overlay (i.e., view that lets the user propose a domestic trade)
	 * @param waitOverlay Wait overlay used to notify the user they are waiting for another player to accept a trade
	 * @param acceptOverlay Accept trade overlay which lets the user accept or reject a proposed trade
	 */
	public DomesticTradeController(IDomesticTradeView tradeView, IDomesticTradeOverlay tradeOverlay,
									IWaitView waitOverlay, IAcceptTradeOverlay acceptOverlay) {

		super(tradeView);
		
		facade = ClientFacade.getInstance();
		facade.addObserver(this);
		setTradeOverlay(tradeOverlay);
		setWaitOverlay(waitOverlay);
		setAcceptOverlay(acceptOverlay);
		setup = true;
	}
	
	public IDomesticTradeView getTradeView() {
		
		return (IDomesticTradeView)super.getView();
	}

	public IDomesticTradeOverlay getTradeOverlay() {
		return tradeOverlay;
	}

	public void setTradeOverlay(IDomesticTradeOverlay tradeOverlay) {
		this.tradeOverlay = tradeOverlay;
	}

	public IWaitView getWaitOverlay() {
		return waitOverlay;
	}

	public void setWaitOverlay(IWaitView waitView) {
		this.waitOverlay = waitView;
	}

	public IAcceptTradeOverlay getAcceptOverlay() {
		return acceptOverlay;
	}

	public void setAcceptOverlay(IAcceptTradeOverlay acceptOverlay) {
		this.acceptOverlay = acceptOverlay;
	}

	private void init() {
		clientNumber = facade.getClientPlayerIndex();
		otherPlayers.clear();
		Vector<PlayerInfo> playerInfo = new Vector<PlayerInfo>();
		sendingMap = new HashMap<ResourceType, Integer>();
		receivingMap = new HashMap<ResourceType, Integer>();
		tradeReciever = PlayerNumber.BANK;

		playerInfo.addAll(facade.getPlayers());
		for(PlayerInfo Info: playerInfo) {
			if(Info.getId() != facade.getClientPlayer().getId()) {
				otherPlayers.add(Info);
			}
		}
		
		for(ResourceType type: ResourceType.values()) {
			switch(type) {
			case BRICK:
				brick = facade.getResourceCount(clientNumber, type);
				break;
			case WOOD:
				wood = facade.getResourceCount(clientNumber, type);
				break;
			case WHEAT:
				wheat = facade.getResourceCount(clientNumber, type);
				break;
			case ORE:
				ore = facade.getResourceCount(clientNumber, type);
				break;
			case SHEEP:
				sheep = facade.getResourceCount(clientNumber, type);
				break;
			default:
				break;
			}
		}
	}
	
	@Override
	public void startTrade() {
		
		this.init();
		if(setup) {
			this.getTradeOverlay().setPlayers(otherPlayers.toArray(info));
			setup = false;
		}
		this.getTradeOverlay().setPlayerSelectionEnabled(true);
		getTradeOverlay().showModal();
	}

	@Override
	public void decreaseResourceAmount(ResourceType resource) {
		this.getTradeOverlay().setResourceAmountChangeEnabled(resource, true, true);
		if(sendingMap.containsKey(resource)) {
			int curCount = sendingMap.get(resource).intValue();
			curCount--;
			sendingMap.remove(resource);
			sendingMap.put(resource, curCount);
			if(curCount == 0) {
				this.getTradeOverlay().setResourceAmountChangeEnabled(resource, true, false);
			}
		}
		if(receivingMap.containsKey(resource)) {
			int curCount = receivingMap.get(resource).intValue();
			curCount--;
			receivingMap.remove(resource);
			receivingMap.put(resource, curCount);
			if(curCount == 0) {
				this.getTradeOverlay().setResourceAmountChangeEnabled(resource, true, false);
			}
		}
		//System.out.printf("decreaseing a resource number");
		enableTradeButton();

	}

	@Override
	public void increaseResourceAmount(ResourceType resource) {
		this.getTradeOverlay().setResourceAmountChangeEnabled(resource, true, true);
		if(sendingMap.containsKey(resource)) {
			int curCount = sendingMap.get(resource).intValue();
			curCount++;
			switch(resource) {
			case BRICK:
				sendingMap.remove(resource);
				sendingMap.put(resource, curCount);
				if(curCount == brick) {
					this.getTradeOverlay().setResourceAmountChangeEnabled(resource, false, true);
				}
				break;
			case WOOD:
				sendingMap.remove(resource);
				sendingMap.put(resource, curCount);
				if(curCount == wood) {
					this.getTradeOverlay().setResourceAmountChangeEnabled(resource, false, true);
					}
				break;
			case WHEAT:
				sendingMap.remove(resource);
				sendingMap.put(resource, curCount);
				if(curCount == wheat) {
					this.getTradeOverlay().setResourceAmountChangeEnabled(resource, false, true);
					}
				break;
			case ORE:
				sendingMap.remove(resource);
				sendingMap.put(resource, curCount);
				if(curCount == ore) {
					this.getTradeOverlay().setResourceAmountChangeEnabled(resource, false, true);
					}
				break;
			case SHEEP:
				sendingMap.remove(resource);
				sendingMap.put(resource, curCount);
				if(curCount == sheep) {
					this.getTradeOverlay().setResourceAmountChangeEnabled(resource, false, true);
					}
				break;
				default:
					break;
			}
			
		}
		if(receivingMap.containsKey(resource)) {
			int curCount = receivingMap.get(resource).intValue();
			curCount++;
			receivingMap.remove(resource);
			receivingMap.put(resource, curCount);
		}
		//System.out.printf("increaseing a resource number");
		enableTradeButton();
	}

	@Override
	public void sendTradeOffer() {
		ResourceInvoice invoice = makeTradeInvoice();
		
		this.getTradeOverlay().closeModal();
		this.getWaitOverlay().showModal();
		this.getTradeOverlay().reset();
		tradeReciever = PlayerNumber.BANK;
		try {
			facade.offerTrade(invoice);
		} catch (CatanException e) {
			e.printStackTrace();
		}
	}

	private ResourceInvoice makeTradeInvoice() {
		ResourceInvoice invoice = new ResourceInvoice(clientNumber, tradeReciever);
		
		for(ResourceType type : sendingMap.keySet()) {
			switch(type) {
			case BRICK:
				invoice.setBrick(sendingMap.get(type).intValue());
				break;
			case WOOD:
				invoice.setWood(sendingMap.get(type).intValue());
				break;
			case WHEAT:
				invoice.setWheat(sendingMap.get(type).intValue());
				break;
			case ORE:
				invoice.setOre(sendingMap.get(type).intValue());
				break;
			case SHEEP:
				invoice.setSheep(sendingMap.get(type).intValue());
				break;
				default:
					break;
			}
		}
		for(ResourceType type : receivingMap.keySet()) {
			switch(type) {
			case BRICK:
				invoice.setBrick(-receivingMap.get(type).intValue());
				break;
			case WOOD:
				invoice.setWood(-receivingMap.get(type).intValue());
				break;
			case WHEAT:
				invoice.setWheat(-receivingMap.get(type).intValue());
				break;
			case ORE:
				invoice.setOre(-receivingMap.get(type).intValue());
				break;
			case SHEEP:
				invoice.setSheep(-receivingMap.get(type).intValue());
				break;
				default:
					break;
			}
		}
		tradingInvoice = invoice;
		return invoice;
	}

	@Override
	public void setPlayerToTradeWith(PlayerNumber playerIndex) {
		tradeReciever = playerIndex;
		//System.out.printf("setting the player to trade with");
		enableTradeButton();
	}

	@Override
	public void setResourceToReceive(ResourceType resource) {
		
		if(!receivingMap.containsKey(resource)) {
			receivingMap.put(resource, 0);
			this.getTradeOverlay().setResourceAmountChangeEnabled(resource, true, false);
			if(sendingMap.containsKey(resource)) {
				sendingMap.remove(resource);
				this.getTradeOverlay().setResourceAmount(resource, "0");
			}
		}
		//System.out.printf("set resource to receive");
		enableTradeButton();
	}

	@Override
	public void setResourceToSend(ResourceType resource) {
		
		if(!sendingMap.containsKey(resource)) {
			sendingMap.put(resource, 0);
			this.getTradeOverlay().setResourceAmountChangeEnabled(resource, true, false);
			switch(resource) {
			case BRICK:
				if(brick == 0) {
					this.getTradeOverlay().setResourceAmountChangeEnabled(resource, false, false);
				}
				break;
			case WOOD:
				if(wood == 0) {
					this.getTradeOverlay().setResourceAmountChangeEnabled(resource, false, false);
				}
				break;
			case WHEAT:
				if(wheat == 0) {
					this.getTradeOverlay().setResourceAmountChangeEnabled(resource, false, false);
				}
				break;
			case ORE:
				if(ore == 0) {
					this.getTradeOverlay().setResourceAmountChangeEnabled(resource, false, false);
				}
				break;
			case SHEEP:
				if(sheep == 0) {
					this.getTradeOverlay().setResourceAmountChangeEnabled(resource, false, false);
				}
				break;
				default:
					break;
			}
			
			if(receivingMap.containsKey(resource)) {
				receivingMap.remove(resource);
				this.getTradeOverlay().setResourceAmount(resource, "0");
			}
		}
		
		//System.out.printf("set resource to send");
		enableTradeButton();
	}

	@Override
	public void unsetResource(ResourceType resource) {
		if(sendingMap.containsKey(resource)) {
			sendingMap.remove(resource);
		}
		if(receivingMap.containsKey(resource)) {
			receivingMap.remove(resource);
		}
		enableTradeButton();
	}

	@Override
	public void cancelTrade() {

		getTradeOverlay().closeModal();
	}

	private void displayAcceptTrade(ResourceInvoice invoice) {
		
		tradingInvoice = invoice;
		this.init();
		int tradeBrick = Math.abs(invoice.getBrick());
		int tradeWood = Math.abs(invoice.getWood());
		int tradeWheat = Math.abs(invoice.getWheat());
		int tradeOre = Math.abs(invoice.getOre());
		int tradeSheep = Math.abs(invoice.getSheep());
		boolean enoughBrick = false;
		boolean enoughWood = false;
		boolean enoughWheat = false;
		boolean enoughOre = false;
		boolean enoughSheep = false;
		PlayerNumber srcPlayer = invoice.getSourcePlayer();
		
		for(PlayerInfo info :facade.getPlayers()) {
			if(info.getPlayerIndex() == srcPlayer) {
				this.getAcceptOverlay().setPlayerName(info.getName());
				break;
			}
		}
		
		for(ResourceType type: ResourceType.values()) {
			switch(type) {
			case BRICK:
				if(invoice.getBrick() > 0) {
					this.getAcceptOverlay().addGetResource(type, tradeBrick);
					enoughBrick = true;
				}
				else if(invoice.getBrick() < 0) {
					this.getAcceptOverlay().addGiveResource(type, tradeBrick);
					if(brick >= tradeBrick) {
						enoughBrick = true;
					}
				}
				else {
					enoughBrick = true;
				}
				break;
			case WOOD:
				if(invoice.getWood() > 0) {
					this.getAcceptOverlay().addGetResource(type, tradeWood);
					enoughWood = true;
				}
				else if(invoice.getWood() < 0) {
					this.getAcceptOverlay().addGiveResource(type, tradeWood);
					if(wood >= tradeWood) {
						enoughWood = true;
					}
				}
				else {
					enoughWood = true;
				}
				break;
			case WHEAT:
				if(invoice.getWheat() > 0) {
					this.getAcceptOverlay().addGetResource(type, tradeWheat);
					enoughWheat = true;
				}
				else if(invoice.getWheat() < 0) {
					this.getAcceptOverlay().addGiveResource(type, tradeWheat);
					if(wheat >= tradeWheat) {
						enoughWheat = true;
					}
				}
				else {
					enoughWheat = true;
				}
				break;
			case ORE:
				if(invoice.getOre() > 0) {
					this.getAcceptOverlay().addGetResource(type, tradeOre);
					enoughOre = true;
				}
				else if(invoice.getOre() < 0) {
					this.getAcceptOverlay().addGiveResource(type, tradeOre);
					if(ore >= tradeOre) {
						enoughOre = true;
					}
				}
				else {
					enoughOre = true;
				}
				break;
			case SHEEP:
				if(invoice.getSheep() > 0) {
					this.getAcceptOverlay().addGetResource(type, tradeSheep);
					enoughSheep = true;
				}
				else if(invoice.getSheep() < 0) {
					this.getAcceptOverlay().addGiveResource(type, tradeSheep);
					if(sheep >= tradeSheep) {
						enoughSheep = true;
					}
				}
				else {
					enoughSheep = true;
				}
				break;
				default:
					break;
			}
		}
		if(enoughBrick && enoughWood && enoughWheat && enoughOre && enoughSheep) {
			this.getAcceptOverlay().setAcceptEnabled(true);
		}
		else {
			this.getAcceptOverlay().setAcceptEnabled(false);
		}
		this.getAcceptOverlay().showModal();
	}
	
	@Override
	public void acceptTrade(boolean willAccept) {
		try {
			facade.acceptTrade(tradingInvoice, willAccept);
			this.getAcceptOverlay().reset();
			//System.out.printf("hello");
		} catch (CatanException e) {
			e.printStackTrace();
		}
		getAcceptOverlay().closeModal();
	}
	private void enableTradeButton() {
		boolean sendData = false;
		boolean receiveData = false;
		boolean receiver = false;
		for(ResourceType type : sendingMap.keySet()) {
			if(sendingMap.get(type).intValue() > 0) {
				sendData = true;
			}
		}
		for(ResourceType type: receivingMap.keySet()) {
			if(receivingMap.get(type).intValue() > 0) {
				receiveData = true;
			}
		}
		if(tradeReciever != PlayerNumber.BANK) {
			receiver = true;
		}
		if(sendData && receiveData && receiver) {
			this.getTradeOverlay().setStateMessage("Start Trade!");
			this.getTradeOverlay().setTradeEnabled(true);
		}
		else {
			this.getTradeOverlay().setStateMessage("Make a sendable offer!");
			this.getTradeOverlay().setTradeEnabled(false);
		}
			
	}

	@Override
	public void update(Observable o, Object arg) {
		DomesticTradeView view = (DomesticTradeView) super.getView();
		if (facade.isClientTurn() && (this.facade.getModelState() == CatanState.PLAYING)) {
			view.enableDomesticTrade(true);
		}
		else {
			view.enableDomesticTrade(false);
		}
		ResourceInvoice invoice = facade.getOpenOffer();
		PlayerNumber clientNumb = facade.getClientPlayerIndex();
		if(invoice != null && clientNumb == invoice.getDestinationPlayer()) {
			if(!this.getAcceptOverlay().isModalShowing())
				displayAcceptTrade(invoice);
		}
		else if(invoice != null && clientNumb == invoice.getSourcePlayer()) {
			if(!this.getWaitOverlay().isModalShowing())
				this.getWaitOverlay().showModal();
		}
		else {
			if(this.getWaitOverlay().isModalShowing()) {
				this.getWaitOverlay().closeModal();
				if(this.getTradeOverlay().isModalShowing()) {
					this.getTradeOverlay().closeModal();
				}
			}
			/*if(this.getAcceptOverlay().isModalShowing()) {
				this.getAcceptOverlay().closeModal();
			}*/
		}
	}
}

