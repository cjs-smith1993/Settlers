package server.handlers;

import java.io.IOException;
import java.util.Collection;

import server.commands.CommandResponse;
import server.commands.ICommand;
import server.util.CookieConverter;
import server.util.ExchangeUtils;
import server.util.RequestType;
import server.util.StatusCode;

import com.google.gson.JsonParseException;
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

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		this.exchangeUtils = new ExchangeUtils(exchange);

		String commandName = this.exchangeUtils.getCommandName();
		RequestType requestType = this.exchangeUtils.getRequestType();
		String blob = requestType == RequestType.POST ? this.exchangeUtils.getRequestBody() : null;

		try {
			ICommand command = this.getCommand(commandName, blob);
			Collection<String> cookiesList = exchange.getRequestHeaders().get("Cookie");
			String cookie = cookiesList != null ? cookiesList.iterator().next() : null;
			CommandResponse response = this.processCommand(command, cookie);
			this.sendResponse(response);
		} catch (JsonParseException e) {
			this.sendResponse(CommandResponse.getMalformedCommand());
		}
	}

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
	 * Authenticates if necessary, using the provided cookies and executes the
	 * given command
	 *
	 * @param command
	 *            an ICommand to execute
	 * @param cookieString
	 *            a String containing the request's cookies
	 * @return
	 */
	protected abstract CommandResponse processCommand(ICommand command, String cookieString);

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
			String userCookie = CookieConverter.generateUserCookie(commandResponse.getUserCert());
			this.exchangeUtils.setCookie(userCookie);
		}
		else if (commandResponse.getGameCert() != null) {
			String gameCookie = CookieConverter.generateGameCookie(commandResponse.getGameCert());
			this.exchangeUtils.setCookie(gameCookie);
		}
	}

	/**
	 * Parses a CommandResponse object and sends back an HTTP response
	 *
	 * @param response
	 *            the response from a given ICommand
	 * @throws IOException
	 */
	protected void sendResponse(CommandResponse response) throws IOException {
		StatusCode status = response.getStatus();
		String body = response.getBody();

		this.exchangeUtils.setContentType(response.getResponseType().getName());
		this.setCookie(response);
		this.exchangeUtils.sendResponseHeaders(status.getCode(), body.length());
		this.exchangeUtils.writeResponseBody(body);
		this.exchangeUtils.close();
	}

}
