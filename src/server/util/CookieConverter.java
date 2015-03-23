package server.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import client.backend.CatanSerializer;
import server.certificates.GameCertificate;
import server.certificates.UserCertificate;

public class CookieConverter {

	/**
	 * Generates an HTTP user cookie string from a UserCertificate.
	 *
	 * @param userCert
	 *            the UserCertificate to be converted to a cookie string
	 * @return an HTTP user cookie string
	 */
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

	/**
	 * Generates an HTTP game cookie string from a GameCertificate.
	 *
	 * @param gameCert
	 *            the GameCertificate to be converted to a cookie string
	 * @return an HTTP game cookie string
	 */
	public static String generateGameCookie(GameCertificate gameCert) {
		if (gameCert == null) {
			return "";
		}

		int gameId = gameCert.getGameId();
		String cookie = "catan.game=" + gameId + ";Path=/;";
		return cookie;
	}

	/**
	 * Converts an HTTP user cookie string into a UserCertificate.
	 *
	 * @param cookieString
	 *            the cookie string to be converted to a UserCertificate
	 * @return a UserCertificate object
	 */
	public static UserCertificate parseUserCookie(String cookieString) {
		UserCertificate userCert = null;

		if (cookieString == null) {
			return userCert;
		}

		try {
			String certString = cookieString.replaceAll("catan.user=", "").replaceAll(";Path=/;",
					"");
			String decodedCertString = URLDecoder.decode(certString, "UTF-8");
			CatanSerializer serializer = CatanSerializer.getInstance();
			userCert = (UserCertificate) serializer.deserializeObject(decodedCertString,
					UserCertificate.class);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return userCert;
	}

	/**
	 * Converts an HTTP game cookie string into a UserCertificate.
	 *
	 * @param cookieString
	 *            the cookie string to be converted to a GameCertificates
	 * @return a GameCertificate object
	 */
	public static GameCertificate parseGameCookie(String cookieString) {
		GameCertificate gameCert = null;

		if (cookieString == null) {
			return gameCert;
		}

		try {
			String certString = cookieString.replaceAll("catan.game=", "").replaceAll(";Path=/;",
					"");
			String decodedCertString = URLDecoder.decode(certString, "UTF-8");
			CatanSerializer serializer = CatanSerializer.getInstance();
			gameCert = (GameCertificate) serializer.deserializeObject(decodedCertString,
					GameCertificate.class);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return gameCert;
	}
}
