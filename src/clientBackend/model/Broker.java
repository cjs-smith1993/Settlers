package clientBackend.model;

import java.util.List;
import java.util.Map;

import shared.definitions.*;

/**
 * Responsible for the transfer of cards between and among the bank and players' holdings 
 * */
public class Broker {
	private Map<PlayerNumber, PlayerHoldings> holdings;
	
	/** TODO
	 * Initiates a transfer of cards between or among the bank and players' holdings
	 * @param	invoices	a list of invoices to process
	 * @return				indicates success of invoices
	 * */
	public boolean processInvoices(List<ResourceInvoice> invoices) {
		return false;
	}
	
	/** TODO
	 * Transfers the correct cards from a player's holdings to the bank
	 * for the given property
	 * @param playerNum
	 * @param propertyType
	 * @return
	 */
	public boolean purchase(PlayerNumber playerNum, Property propertyType) {
		return false;
	}

}
