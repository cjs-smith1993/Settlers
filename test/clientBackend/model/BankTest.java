package clientBackend.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import clientBackend.transport.TransportBank;
import clientBackend.transport.TransportDeck;
import shared.definitions.DevCardType;
import shared.definitions.ResourceType;

public class BankTest {

	Bank myBank;
	Bank transBank;

	@Test
	public void testBank() {
		this.myBank = new Bank();
		for (ResourceType type : ResourceType.values()) {
			if (type != ResourceType.ALL && type != ResourceType.NONE) {
				assertTrue(this.myBank.getResourceCardCount(type) == 25);
			}
		}
		for (DevCardType type : DevCardType.values()) {
			switch (type) {
			case SOLDIER:
				assertTrue(this.myBank.getDevelopmentCardCount(type) == 14);
				break;
			case MONUMENT:
				assertTrue(this.myBank.getDevelopmentCardCount(type) == 5);
				break;
			default:
				assertTrue(this.myBank.getDevelopmentCardCount(type) == 2);
				break;
			}

		}
	}

	@Test
	public void testBankTransportDeckTransportBank() {
		//TransportDeck devDeck, TransportBank resDeck
		TransportDeck devDeck = new TransportDeck();
		TransportBank resDeck = new TransportBank();
		devDeck.monopoly = 1;
		devDeck.monument = 3;
		devDeck.roadBuilding = 2;
		devDeck.soldier = 10;
		devDeck.yearOfPlenty = 2;
		resDeck.brick = 12;
		resDeck.ore = 15;
		resDeck.sheep = 8;
		resDeck.wheat = 20;
		resDeck.wood = 5;
		this.transBank = new Bank(devDeck, resDeck);
		for (ResourceType type : ResourceType.values()) {
			switch (type) {
			case BRICK:
				assertTrue(this.transBank.getResourceCardCount(type) == 12);
				break;
			case WOOD:
				assertTrue(this.transBank.getResourceCardCount(type) == 5);
				break;
			case WHEAT:
				assertTrue(this.transBank.getResourceCardCount(type) == 20);
				break;
			case ORE:
				assertTrue(this.transBank.getResourceCardCount(type) == 15);
				break;
			case SHEEP:
				assertTrue(this.transBank.getResourceCardCount(type) == 8);
				break;
			default:
				break;
			}
		}
		for (DevCardType type : DevCardType.values()) {
			switch (type) {
			case SOLDIER:
				assertTrue(this.transBank.getDevelopmentCardCount(type) == 10);
				break;
			case MONUMENT:
				assertTrue(this.transBank.getDevelopmentCardCount(type) == 3);
				break;
			case MONOPOLY:
				assertTrue(this.transBank.getDevelopmentCardCount(type) == 1);
				break;
			default:
				assertTrue(this.transBank.getDevelopmentCardCount(type) == 2);
				break;
			}
		}
	}

	//	@Test
	//	@Ignore
	//	public void testDrawDevelopmentCard() {
	//		fail("Not yet implemented");
	//	}

	@Test
	public void testAddDevelopmentCardCollection() {
		for (DevCardType type : DevCardType.values())
		{
			this.myBank = new Bank();
			int initSize = this.myBank.getPlayedCards().size();
			Collection<DevelopmentCard> newCards = new ArrayList<DevelopmentCard>();
			newCards.add(new DevelopmentCard(type));
			if (this.myBank.addDevelopmentCardCollection(type, newCards)) {
				assertTrue("The card had the right type and was added", initSize == this.myBank
						.getPlayedCards().size() - 1);
			}
			else
			{
				assertTrue("The card was not added", initSize == this.myBank.getPlayedCards()
						.size());
			}
		}
	}
}
