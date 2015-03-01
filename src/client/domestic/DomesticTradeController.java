package client.domestic;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import shared.definitions.*;
import client.base.*;
import client.data.PlayerInfo;
import client.misc.*;
import clientBackend.model.CatanException;
import clientBackend.model.Facade;
import clientBackend.model.ResourceInvoice;


/**
 * Domestic trade controller implementation
 */
public class DomesticTradeController extends Controller implements IDomesticTradeController {

	private static final boolean tradeReceiver = false;
	private IDomesticTradeOverlay tradeOverlay;
	private IWaitView waitOverlay;
	private IAcceptTradeOverlay acceptOverlay;
	private Facade facade;
	private int brick = 0;
	private int wood = 0;
	private int wheat = 0;
	private int ore = 0;
	private int sheep = 0;
	private PlayerNumber clientNumber;
	
	private  PlayerNumber tradeReciever = PlayerNumber.BANK;
	private Map<ResourceType, Integer> sendingMap = new HashMap<ResourceType, Integer>();
	private Map<ResourceType, Integer> receivingMap = new HashMap<ResourceType, Integer>();
	
	private ResourceType sendingResource = ResourceType.NONE;
	private ResourceType recievingResource = ResourceType.NONE;
	private int sendingNumber = 0;
	private int recievingNumber = 0;

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
		
		facade = Facade.getInstance();
		setTradeOverlay(tradeOverlay);
		setWaitOverlay(waitOverlay);
		setAcceptOverlay(acceptOverlay);
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

	@Override
	public void startTrade() {
		clientNumber = facade.getClientPlayerIndex();
		Vector<PlayerInfo> playerInfo = new Vector<PlayerInfo>();
		PlayerInfo[] info = new PlayerInfo[3];
		Vector<PlayerInfo> otherPlayers = new Vector<PlayerInfo>();
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
		
		this.getTradeOverlay().setPlayers(otherPlayers.toArray(info));
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
		System.out.printf("decreaseing a resource number");
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
		System.out.printf("increaseing a resource number");
		enableTradeButton();
	}

	@Override
	public void sendTradeOffer() {
		ResourceInvoice invoice = makeTradeInvoice();
		
		try {
			facade.offerTrade(invoice);
		} catch (CatanException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getTradeOverlay().closeModal();
//		getWaitOverlay().showModal();
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
		return invoice;
	}

	@Override
	public void setPlayerToTradeWith(PlayerNumber playerIndex) {
		tradeReciever = playerIndex;
		System.out.printf("setting the player to trade with");
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
		System.out.printf("set resource to receive");
		enableTradeButton();
	}

	@Override
	public void setResourceToSend(ResourceType resource) {
		
		if(!sendingMap.containsKey(resource)) {
			sendingMap.put(resource, 0);
			this.getTradeOverlay().setResourceAmountChangeEnabled(resource, true, false);
			if(receivingMap.containsKey(resource)) {
				receivingMap.remove(resource);
				this.getTradeOverlay().setResourceAmount(resource, "0");
			}
		}
		
		System.out.printf("set resource to send");
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
		System.out.printf("unset resource");
		enableTradeButton();
	}

	@Override
	public void cancelTrade() {

		getTradeOverlay().closeModal();
	}

	@Override
	public void acceptTrade(boolean willAccept) {

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
}

