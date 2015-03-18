package server.handlers;

import java.io.IOException;

import server.commands.ICommand;
import server.factories.UserCommandFactory;
import server.util.CommandResponse;
import server.util.CookieCreator;
import server.util.ExchangeUtils;
import server.util.StatusCode;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * The HttpHandler for all "/user/" calls to the server
 *
 * @author kevinjreece
 */
public class UserHandler implements HttpHandler {

	private final String prefix = "/user/";

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		ExchangeUtils utils = new ExchangeUtils(exchange);

		String URI = utils.getURI(this.prefix);
		String blob = utils.getRequestBody();

		ICommand cmd = UserCommandFactory.getInstance().getCommand(URI, blob);
		CommandResponse response = cmd.execute();

		StatusCode status = response.getStatus();
		String body = response.getBody();

		utils.setContentType("text/html");
		String userCookie = CookieCreator.generateUserCookie(response.getUserCert());
		utils.setCookie(userCookie);
		utils.sendResponseHeaders(status.getCode(), body.length());

		utils.writeResponseBody(body);
		utils.close();
	}

}
