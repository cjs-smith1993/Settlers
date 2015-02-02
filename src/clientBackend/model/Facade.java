package clientBackend.model;

public class Facade {
	private Bank bank;
	private Broker broker;
	private Game game;
	private PostOffice postOffice;
	private Scoreboard scoreboard;
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
