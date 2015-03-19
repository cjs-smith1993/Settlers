package server.handlers;

import java.io.IOException;

import server.commands.CommandResponse;
import server.commands.ICommand;
import server.util.CookieCreator;
import server.util.ExchangeUtils;
import server.util.StatusCode;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * The superclass for each of the HttpHandlers. This class performs all
 * necessary work to handle an HTTP request. The subclasses only need to
 * override the getCommand method to provide commands appropriate for their
 * requests.
 *
 */
public abstract class AbstractHandler implements HttpHandler {
	ExchangeUtils exchangeUtils;

	/**
	 * Returns an ICommand object appropriate for this handler
	 *
	 * @param commandName
	 *            the name of the desired command
	 * @param json
	 *            a JSON blob containing the necessary information for the
	 *            command
	 * @return an ICommand object appropriate for this handler
	 */
	protected abstract ICommand getCommand(String commandName, String json);

	/**
	 * Sets the appropriate cookie on the HttpResponse if there is an applicable
	 * userCert on the commandResponse. At most one cookie will be set per
	 * response
	 *
	 * @param commandResponse
	 *            the response from a given ICommand
	 */
	protected void setCookie(CommandResponse commandResponse) {
		if (commandResponse.getUserCert() != null) {
			String userCookie = CookieCreator.generateUserCookie(commandResponse.getUserCert());
			this.exchangeUtils.setCookie(userCookie);
		}
		else if (commandResponse.getGameCert() != null) {
			String gameCookie = CookieCreator.generateGameCookie(commandResponse.getGameCert());
			this.exchangeUtils.setCookie(gameCookie);
		}
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		this.exchangeUtils = new ExchangeUtils(exchange);

		String commandName = this.exchangeUtils.getCommandName();
		String blob = this.exchangeUtils.getRequestBody();

		ICommand command = this.getCommand(commandName, blob);
		CommandResponse response = command.execute();

		StatusCode status = response.getStatus();
		String body = response.getBody();

		this.exchangeUtils.setContentType(response.getResponseType().getName());
		this.setCookie(response);
		this.exchangeUtils.sendResponseHeaders(status.getCode(), body.length());
		this.exchangeUtils.writeResponseBody(body);
		this.exchangeUtils.close();
	}
}
