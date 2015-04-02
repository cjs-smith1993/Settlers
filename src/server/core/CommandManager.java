package server.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import server.commands.moves.AbstractMovesCommand;

public class CommandManager {
	
	private static CommandManager instance;
	private Map<Integer, Collection<AbstractMovesCommand>> commands;
	
	private CommandManager() {
		this.commands = new HashMap<Integer, Collection<AbstractMovesCommand>>();
	}
	
	public static CommandManager getInstance() {
		if (instance == null) {
			instance = new CommandManager();
		}
		return instance;
	}
	
	public Collection<AbstractMovesCommand> getAllCommands(int gameId) {
		return this.commands.get(gameId);
	}
	
	public void addCommand(int gameId, AbstractMovesCommand command) {
		Collection<AbstractMovesCommand> gameCommands = this.commands.get(gameId);
		if (gameCommands == null) {
			gameCommands = new ArrayList<AbstractMovesCommand>();
		}
		
		gameCommands.add(command);
		this.commands.put(gameId, gameCommands);
		return;
	}
}
