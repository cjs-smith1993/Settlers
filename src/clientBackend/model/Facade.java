package clientBackend.model;

import shared.definitions.PlayerNumber;

public class Facade {
	private Bank bank;
	private Broker broker;
	private Game game;
	private PostOffice postOffice;
	private Scoreboard scoreboard;
	private PlayerNumber clientPlayer;
	
	public boolean CanDiscardCards() {
		// Does the client player have any cards to discard?
		return false;
	}
	
	public boolean CanRollNumber() {
		// Is it the client player's turn?
		// Has the client player already rolled?
		return false;
	}
	
	public boolean CanBuildRoad() {
		// Is it the client player's turn?
		// Does the client player have enough resources?
		// Does the client player have at least one available road?
		return false;
	}
	
	public boolean CanBuildSettlement() {
		// Is it the client player's turn?
		// Does the client player have enough resources?
		// Does the client player have at least one available settlement?
		return false;
	}
	
	public boolean CanBuildCity() {
		// Is it the client player's turn?
		// Does the client player have enough resources?
		// Does the client player have at least one available city?
		return false;
	}
	
	public boolean CanOfferTrade() {
		// Is it the client player's turn?
		// Is it the client player's turn?
		// Does the client player have any cards?
		return false;
	}
	
	public boolean CanMaritimeTrade() {
		// Is it the client player's turn?
		// Does the client player own at least one harbor?
		return false;
	}
	
	public boolean CanFinishTurn() {
		// Is it the client player's turn?
		// Has the client player rolled?
		return false;
	}
	
	public boolean CanBuyDevCard() {
		// Is it the client player's turn?
		// Does the client player have enough resources?
		// Is there at least one more development card in the deck?
		return false;
	}
	
	public boolean CanUseYearOfPlenty() {
		// Is it the client player's turn?
		// Does the client player have a playable Year of Plenty 
		// developement card?
		return false;
	}
	
	public boolean CanUseRoadBuilder() {
		// Is it the client player's turn?
		// Does the client player have a playable Road Builder
		// developement card?
		return false;
	}
	
	public boolean CanUseSoldier() {
		// Is it the client player's turn?
		// Does the client player have an available Soldier
		// developement card?
		return false;
	}
	
	public boolean CanUseMonopoly() {
		// Is it the client player's turn?
		// Does the client player have a playable Monopoly 
		// development card?
		return false;
	}
	
	public boolean CanUseMonument() {
		// Is it the client player's turn?
		// Does the client player have a playable Monument
		// development card?
		return false;
	}
	
	public boolean CanPlaceRobber() {
		// Is it the client player's turn?
		return false;
	}
	
	public Bank getBank() {
		return bank;
	}
	
	public void setBank(Bank bank) {
		this.bank = bank;
	}
	
	public Broker getBroker() {
		return broker;
	}
	
	public void setBroker(Broker broker) {
		this.broker = broker;
	}
	
	public Game getGame() {
		return game;
	}
	
	public void setGame(Game game) {
		this.game = game;
	}
	
	public PostOffice getPostOffice() {
		return postOffice;
	}
	
	public void setPostOffice(PostOffice postOffice) {
		this.postOffice = postOffice;
	}
	
	public Scoreboard getScoreboard() {
		return scoreboard;
	}
	
	public void setScoreboard(Scoreboard scoreboard) {
		this.scoreboard = scoreboard;
	}
}
