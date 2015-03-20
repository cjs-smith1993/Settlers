package server.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import client.backend.CatanSerializer;
import server.certificates.GameCertificate;
import server.certificates.UserCertificate;

public class CookieConverter {

	public static String generateUserCookie(UserCertificate userCert) {
		if (userCert == null) {
			return "";
		}

		String cookie = "";

		try {
			String json = CatanSerializer.getInstance().serializeObject(userCert);
			String encodedUserCert = null;
			encodedUserCert = URLEncoder.encode(json, "UTF-8");
			cookie = "catan.user=" + encodedUserCert + ";Path=/;";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return cookie;
	}

	public static String generateGameCookie(GameCertificate gameCert) {
		if (gameCert == null) {
			return "";
		}

		int gameId = gameCert.getGameId();
		String cookie = "catan.game=" + gameId + ";Path=/;";
		return cookie;
	}
}
