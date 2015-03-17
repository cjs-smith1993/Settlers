package server.handlers;

import java.io.IOException;
import java.util.ArrayList;

import server.commands.ICommand;
import server.factories.UserCommandFactory;
import server.util.CommandResponse;
import server.util.ExchangeUtils;

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
		String body = response.getBody();

		ArrayList<String> headers = new ArrayList<String>();
		headers.add("text/html");
		exchange.getResponseHeaders().put("Content-Type", headers);
		exchange.sendResponseHeaders(200, body.length());
		utils.writeResponseBody(body);
		exchange.close();
	}

}
