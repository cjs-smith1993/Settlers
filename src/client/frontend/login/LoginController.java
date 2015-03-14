package client.frontend.login;

import javax.swing.JOptionPane;

import client.backend.ClientModelFacade;
import client.frontend.base.*;
import client.frontend.misc.*;

/**
 * Implementation for the login controller
 */
public class LoginController extends Controller implements ILoginController {

	private IMessageView messageView;
	private IAction loginAction;
	private ClientModelFacade facade;

	/**
	 * LoginController constructor
	 *
	 * @param view
	 *            Login view
	 * @param messageView
	 *            Message view (used to display error messages that occur during
	 *            the login process)
	 */
	public LoginController(ILoginView view, IMessageView messageView) {
		super(view);
		this.facade = ClientModelFacade.getInstance();
		this.messageView = messageView;
	}

	public ILoginView getLoginView() {
		return (ILoginView) super.getView();
	}

	public IMessageView getMessageView() {
		return this.messageView;
	}

	/**
	 * Sets the action to be executed when the user logs in
	 *
	 * @param value
	 *            The action to be executed when the user logs in
	 */
	public void setLoginAction(IAction value) {
		this.loginAction = value;
	}

	/**
	 * Returns the action to be executed when the user logs in
	 *
	 * @return The action to be executed when the user logs in
	 */
	public IAction getLoginAction() {
		return this.loginAction;
	}

	@Override
	public void start() {
		this.getLoginView().showModal();
	}

	@Override
	public void signIn() {

		LoginView myView = (LoginView) this.getView();
		String username = myView.getLoginUsername();
		String password = myView.getLoginPassword();

		if (!(this.facade.login(username, password))) {
			JOptionPane.showMessageDialog(null,
					"Could not Log on.",
					"Login Error",
					JOptionPane.INFORMATION_MESSAGE);
		}
		else {
			// If log in succeeded
			this.getLoginView().closeModal();
			this.loginAction.execute();
		}
	}

	@Override
	public void register() {
		LoginView myView = (LoginView) this.getView();
		String username = myView.getRegisterUsername();
		String password = myView.getRegisterPassword();
		String rePassword = myView.getRegisterPasswordRepeat();

		String errorMessage = null;
		if (username.length() < 3) {
			errorMessage = "Username must be at least 3 characters";
		}
		else if (password.length() < 5) {
			errorMessage = "Password must be at least 5 characters";
		}
		else if (!password.equals(rePassword)) {
			errorMessage = "The passwords you entered do not match.";
		}
		else if (!(this.facade.register(username, password))) {
			errorMessage = "Could not register user";
		}

		if (errorMessage != null) {
			JOptionPane.showMessageDialog(null, errorMessage, "Register Error",
					JOptionPane.INFORMATION_MESSAGE);
		}
		else {
			this.getLoginView().closeModal();
			this.loginAction.execute();
		}
	}

}
