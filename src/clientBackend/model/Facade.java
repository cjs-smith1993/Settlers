package clientBackend.model;

import clientBackend.transport.TransportModel;
import shared.definitions.DevCardType;
import shared.definitions.PlayerNumber;
import shared.definitions.PropertyType;
import shared.definitions.ResourceType;

public class Facade {
	private Bank bank;
	private Broker broker;
	private Game game;
	private PostOffice postOffice;
	private Scoreboard scoreboard;
	private PlayerNumber clientPlayer;
	private int version;
	
	private final int ROBBER_ROLL = 7;
	
 	public void initializeModel(TransportModel model) {
		scoreboard = new Scoreboard(model.players, model.turnTracker);
		postOffice = new PostOffice(model.chat.lines, model.log.lines);
		
		// TODO: Initialize other objects.
	}
	
	public boolean CanDiscardCards(PlayerNumber player, int discardAmount) {
		return broker.canDiscardCards(player, discardAmount);
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
	
	/**
	 * Determines if a player has the resources to build a road
	 * @param player
	 * @return if the player has the resources to build a road
	 * @throws CatanException 
	 */
	public boolean canBuildRoad(PlayerNumber player) throws CatanException {
		// Has player rolled yet?
		// Is it the client player's turn?
		// Does the client player have enough resources?
		// Does the client player have at least one available road?
		
		if (game.getCurrentPlayerHasRolled() 
				&& game.getCurrentPlayer() == player
				&& broker.canPurchase(player, PropertyType.ROAD)
				&& game.hasRoad(player)) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Determines if a player has the resources to build a settlement
	 * @param player
	 * @return if the player has the resources to build a settlement
	 * @throws CatanException 
	 */
	public boolean canBuildSettlement(PlayerNumber player) throws CatanException {
		// Has player rolled yet?
		// Is it the client player's turn?
		// Does the client player have enough resources?
		// Does the client player have at least one available settlement?
		
		if (game.getCurrentPlayerHasRolled() 
				&& game.getCurrentPlayer() == player
				&& broker.canPurchase(player, PropertyType.SETTLEMENT)
				&& game.hasSettlement(player)) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Determines if a player has the resources to build a city
	 * @param player
	 * @return if the player has the resources to build a city
	 * @throws CatanException
	 */
	public boolean canBuildCity(PlayerNumber player) throws CatanException {
		// Has player rolled yet?
		// Is it the client player's turn?
		// Does the client player have enough resources?
		// Does the client player have at least one available city?
		
		if (game.getCurrentPlayerHasRolled() 
				&& game.getCurrentPlayer() == player
				&& broker.canPurchase(player, PropertyType.CITY)
				&& game.hasSettlement(player)) {
			return true;
		}
		
		return false;
	}
	
	public boolean canOfferTrade(PlayerNumber player) {
		// Has player rolled yet?
		// Is it the client player's turn?
		// Does the client player have any cards?
		
		if (game.getCurrentPlayerHasRolled()
				&& game.getCurrentPlayer() == player
				&& broker.hasResourceCard(player)) {
			return true;
		}
		
		return false;
	}
	
	public boolean canMaritimeTrade(PlayerNumber player) throws CatanException {
		// Has player rolled yet?
		// Is it the client player's turn?
		// Does the client player hane any cards?
		// Does the client player own at least one harbor?
		
		if (canOfferTrade(player)
				&& broker.hasHarbor(player)) {
			return true;
		}
		
		return false;
	}
	
	public boolean canFinishTurn(PlayerNumber player) {
		// Is it the client player's turn?
		// Has the client player rolled?
		
		if (game.getCurrentPlayerHasRolled()
				&& game.getCurrentPlayer() == player) {
			return true;
		}
		
		return false;
	}
	
	public boolean canBuyDevCard(PlayerNumber player) throws CatanException {
		// Has player rolled yet?
		// Is it the client player's turn?
		// Does the client player have enough resources?
		// Is there at least one more development card in the deck?
		
		if (game.getCurrentPlayerHasRolled() 
				&& game.getCurrentPlayer() == player
				&& broker.canPurchase(player, PropertyType.DEVELOPMENT_CARD)
				&& broker.hasDevelopmentCard(player)) {
			return true;
		}
		
		return false;
	}
	
	public boolean canUseYearOfPlenty(PlayerNumber player) throws CatanException {
		// Has player rolled yet?
		// Is it the client player's turn?
		// Does the client player have a playable Year of Plenty 
		// developement card?
		
		if (game.getCurrentPlayerHasRolled()
				&& game.getCurrentPlayer() == player
				&& broker.canPlayDevelopmentCard(player, DevCardType.YEAR_OF_PLENTY)) {
			return true;
		}
		
		return false;
	}
	
	public boolean canUseRoadBuilder(PlayerNumber player) throws CatanException {
		// Has player rolled yet?
		// Is it the client player's turn?
		// Does the client player have a playable Road Builder
		// developement card?
		
		if (game.getCurrentPlayerHasRolled()
				&& game.getCurrentPlayer() == player
				&& broker.canPlayDevelopmentCard(player, DevCardType.ROAD_BUILD)) {
			return true;
		}
		
		return false;
	}
	
	public boolean canUseSoldier(PlayerNumber player) throws CatanException {
		// Has player rolled yet?
		// Is it the client player's turn?
		// Does the client player have an available Soldier
		// developement card?
		
		if (game.getCurrentPlayerHasRolled()
				&& game.getCurrentPlayer() == player
				&& broker.canPlayDevelopmentCard(player, DevCardType.SOLDIER)) {
			return true;
		}
		
		return false;
	}
	
	public boolean canUseMonopoly(PlayerNumber player) throws CatanException {
		// Has player rolled yet?
		// Is it the client player's turn?
		// Does the client player have a playable Monopoly 
		// development card?
		
		if (game.getCurrentPlayerHasRolled()
				&& game.getCurrentPlayer() == player
				&& broker.canPlayDevelopmentCard(player, DevCardType.MONOPOLY)) {
			return true;
		}
		
		return false;
	}
	
	public boolean canUseMonument(PlayerNumber player) throws CatanException {
		// Has player rolled yet?
		// Is it the client player's turn?
		// Does the client player have a playable Monument
		// development card?
		
		if (game.getCurrentPlayerHasRolled()
				&& game.getCurrentPlayer() == player
				&& broker.canPlayDevelopmentCard(player, DevCardType.MONUMENT)) {
			return true;
		}
		
		return false;
	}
	
	public boolean canPlaceRobber(PlayerNumber player) {
		// Has player rolled yet?
		// Is it the client player's turn?
		// Was a 7 rolled?
		
		if (game.getCurrentPlayerHasRolled()
				&& game.getCurrentPlayer() == player
				&& game.getLastDiceRoll() == ROBBER_ROLL) {
			return true;
		}
		
		return false;
	}
	
	public PlayerNumber getClientPlayer() {
		return clientPlayer;
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
	
	public int getVersion () {
		return version;
	}
}
