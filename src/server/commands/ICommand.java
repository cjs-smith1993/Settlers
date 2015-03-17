package server.commands;

import server.util.CommandResponse;

public interface ICommand {

	public CommandResponse execute();
}
