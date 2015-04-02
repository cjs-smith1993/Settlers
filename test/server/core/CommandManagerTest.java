package server.core;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Test;

import client.backend.CatanSerializer;
import server.commands.moves.AbstractMovesCommand;
import server.commands.moves.MovesRollNumberCommand;
import shared.dataTransportObjects.DTOMovesRollNumber;
import shared.definitions.PlayerNumber;

public class CommandManagerTest {

	@Test
	public void test() {
		int gameId = 0;
		int userId = 0;
		
		CommandManager manager = CommandManager.getInstance();
		Collection<AbstractMovesCommand> commands = manager.getAllCommands(0);
		assertNull(commands);
		
		DTOMovesRollNumber dto = new DTOMovesRollNumber(PlayerNumber.ONE, userId);
		String DTOjson = CatanSerializer.getInstance().serializeObject(dto);
		MovesRollNumberCommand command = new MovesRollNumberCommand(DTOjson);
		command.setGameId(gameId);
		command.save();
		commands = manager.getAllCommands(gameId);
		assertEquals(commands.size(), 1);
		assertEquals(commands.iterator().next(), command);
	}

}
