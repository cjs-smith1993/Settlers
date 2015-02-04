package clientBackend.model;

import clientBackend.transport.TransportModel;
import shared.definitions.PlayerNumber;
import shared.definitions.ResourceType;

public class Facade {
	private Bank bank;
	private Broker broker;
	private Game game;
	private PostOffice postOffice;
	private Scoreboard scoreboard;
	private PlayerNumber clientPlayer;
	
	public void initializeModel(TransportModel model) {
		scoreboard = new Scoreboard(model.players, model.turnTracker);
		postOffice = new PostOffice(model.chat.lines, model.log.lines);
		
		// TODO: Initialize other objects.
	}
	
	public boolean CanDiscardCards(PlayerNumber player, int discardAmount) {
		boolean canDiscard = broker.canDiscardCards(player, discardAmount);
		
		return canDiscard;
	}
	
	public boolean CanRollNumber(PlayerNumber player) {
		// Is it the client player's turn?
		// Has the client player already rolled?
		if (!game.getCurrentPlayerHasRolled()
				&& game.getCurrentPlayer() == player) {
			return true;
		}
		
		return false;
	}
	
	public boolean CanBuildRoad(PlayerNumber player) {
		// Has player rolled yet?
		// Is it the client player's turn?
		// Does the client player have enough resources?
		// Does the client player have at least one available road?
		
		if (game.getCurrentPlayerHasRolled() 
				&& game.getCurrentPlayer() == player
				&& broker.hasNecessaryResourceAmount(player, ResourceType.BRICK, 1)
				&& broker.hasNecessaryResourceAmount(player, ResourceType.WOOD, 1)) {
			return true;
		}
		
		return false;
	}
	
	public boolean CanBuildSettlement() {
		// Has player rolled yet?
		// Is it the client player's turn?
		// Does the client player have enough resources?
		// Does the client player have at least one available settlement?
		return false;
	}
	
	public boolean CanBuildCity() {
		// Has player rolled yet?
		// Is it the client player's turn?
		// Does the client player have enough resources?
		// Does the client player have at least one available city?
		return false;
	}
	
	public boolean CanOfferTrade() {
		// Has player rolled yet?
		// Is it the client player's turn?
		// Is it the client player's turn?
		// Does the client player have any cards?
		return false;
	}
	
	public boolean CanMaritimeTrade() {
		// Has player rolled yet?
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
		// Has player rolled yet?
		// Is it the client player's turn?
		// Does the client player have enough resources?
		// Is there at least one more development card in the deck?
		return false;
	}
	
	public boolean CanUseYearOfPlenty() {
		// Has player rolled yet?
		// Is it the client player's turn?
		// Does the client player have a playable Year of Plenty 
		// developement card?
		return false;
	}
	
	public boolean CanUseRoadBuilder() {
		// Has player rolled yet?
		// Is it the client player's turn?
		// Does the client player have a playable Road Builder
		// developement card?
		return false;
	}
	
	public boolean CanUseSoldier() {
		// Has player rolled yet?
		// Is it the client player's turn?
		// Does the client player have an available Soldier
		// developement card?
		return false;
	}
	
	public boolean CanUseMonopoly() {
		// Has player rolled yet?
		// Is it the client player's turn?
		// Does the client player have a playable Monopoly 
		// development card?
		return false;
	}
	
	public boolean CanUseMonument() {
		// Has player rolled yet?
		// Is it the client player's turn?
		// Does the client player have a playable Monument
		// development card?
		return false;
	}
	
	public boolean CanPlaceRobber() {
		// Has player rolled yet?
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
