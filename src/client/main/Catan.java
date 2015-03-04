package client.main;

import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;

import serverCommunication.ServerProxy;
import serverTester.CLTester;
import client.catan.*;
import client.login.*;
import client.join.*;
import client.misc.*;
import client.base.*;
import clientBackend.ServerPoller;
import clientBackend.model.Facade;

/**
 * Main entry point for the Catan program
 */
@SuppressWarnings("serial")
public class Catan extends JFrame
{

	private CatanPanel catanPanel;

	public Catan()
	{

		client.base.OverlayView.setWindow(this);

		this.setTitle("Settlers of Catan");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		this.catanPanel = new CatanPanel();
		this.setContentPane(this.catanPanel);

		this.display();
	}

	private void display()
	{
		this.pack();
		this.setVisible(true);
	}

	//
	// Main
	//

	public static void main(final String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		SwingUtilities.invokeLater(new Runnable() {
			public void run()
			{
				setupClientBackend(args);

				new Catan();

				PlayerWaitingView playerWaitingView = new PlayerWaitingView();
				final PlayerWaitingController playerWaitingController = new PlayerWaitingController(
						playerWaitingView);
				playerWaitingView.setController(playerWaitingController);

				JoinGameView joinView = new JoinGameView();
				NewGameView newGameView = new NewGameView();
				SelectColorView selectColorView = new SelectColorView();
				MessageView joinMessageView = new MessageView();
				final JoinGameController joinController = new JoinGameController(
						joinView,
						newGameView,
						selectColorView,
						joinMessageView);
				joinController.setJoinAction(new IAction() {
					@Override
					public void execute()
					{
						playerWaitingController.start();
					}
				});
				joinView.setController(joinController);
				newGameView.setController(joinController);
				selectColorView.setController(joinController);
				joinMessageView.setController(joinController);

				LoginView loginView = new LoginView();
				MessageView loginMessageView = new MessageView();
				LoginController loginController = new LoginController(
						loginView,
						loginMessageView);
				loginController.setLoginAction(new IAction() {
					@Override
					public void execute()
					{
						joinController.start();
					}
				});
				loginView.setController(loginController);
				loginView.setController(loginController);

				loginController.start();
			}
		});
	}

	private static void setupClientBackend(String args[]) {
		String hostname = args.length > 0 ? args[0] : "localhost";
		int port = args.length > 1 ? Integer.parseInt(args[1]) : 8081;

		ArrayList<String> argsList = new ArrayList<String>(Arrays.asList(args));
		boolean testerOn = argsList.contains("true");

		ServerProxy proxy = ServerProxy.getInstance(hostname, port);
		Facade facade = Facade.getInstance();
		facade.setProxy(proxy);

		if (testerOn) {
			CLTester tester = new CLTester(proxy);
			Thread thread = new Thread(tester);
			thread.start();
		}

		ServerPoller poller = new ServerPoller();
		poller.initializeTimer();
	}
}
