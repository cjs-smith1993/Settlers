package server.commands.moves;

import server.CommandResponse;
import server.cookies.GameCookie;
import server.cookies.UserCookie;

/**
 * Represents the notion of executing the appropriate action for a given server
 * endpoint that begins with /moves/
 */
public interface IMovesCommand {

    /**
     * Executes the command specified in the provided JSON
     * 
     * @param user
     *            An encapsulation of a user cookie
     * @param game
     *            A game cookie
     * @param json
     *            A JSON blob containing the required information for the
     *            desired command
     * @return TODO
     */
    public CommandResponse execute(UserCookie user, GameCookie game, String json);
}