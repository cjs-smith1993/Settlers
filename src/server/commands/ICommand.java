package server.commands;

import server.certificates.GameCertificate;
import server.certificates.UserCertificate;

public interface ICommand {

	/**
	 * Executes the command
	 *
	 * @return a CommandResponse representing the result of the command's
	 *         execution
	 */
	public CommandResponse execute();

	/**
	 * Authenticates the command given a UserCertificate
	 *
	 * @param userCert
	 *            a certificate authenticating the user
	 * @return true if the user is authenticated
	 */
	public boolean authenticateUser(UserCertificate userCert);

	/**
	 * Authenticates the command given a GameCertificate
	 *
	 * @param userCert
	 *            a certificate authenticating the user
	 * @return true if the user is authenticated
	 */
	public boolean authenticateGame(GameCertificate gameCert);
}
