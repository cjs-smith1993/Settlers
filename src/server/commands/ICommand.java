package server.commands;

import server.CommandResponse;

public interface ICommand {
	public CommandResponse execute();
}
