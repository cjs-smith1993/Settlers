package server.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;

import com.sun.net.httpserver.HttpExchange;

public class ExchangeUtils {

	private HttpExchange exchange;

	public ExchangeUtils(HttpExchange exchange) {
		this.exchange = exchange;
	}

	public String getURI(String prefix) {
		String fullURI = this.exchange.getRequestURI().getPath();
		String URI = fullURI.substring(prefix.length());
		return URI;
	}

	public String getRequestBody() throws IOException {
		String body = "";
		try (Scanner s = new Scanner(this.exchange.getRequestBody())) {
			s.useDelimiter("\\A");
			body += s.next();
		}
		return body;
	}

	public void setContentType(String type) {
		this.exchange.getResponseHeaders().add("Content-Type", type);
	}

	public void setCookie(String cookieText) {
		this.exchange.getResponseHeaders().add("Set-cookie", cookieText);
	}

	public void sendResponseHeaders(int status, int length) {
		this.sendResponseHeaders(status, length);

	}

	public void writeResponseBody(String responseBody)
			throws IOException {
		OutputStream os = this.exchange.getResponseBody();
		os.write(responseBody.getBytes());
		os.close();
	}

	public void close() {
		this.exchange.close();
	}

}
