package client.login;

import javax.swing.JOptionPane;

import client.base.*;
import client.misc.*;
import clientBackend.model.Facade;

/**
 * Implementation for the login controller
 */
public class LoginController extends Controller implements ILoginController {

	private IMessageView messageView;
	private IAction loginAction;
	private Facade facade;

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
		this.facade = Facade.getInstance();
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

		// TODO: register new user (which, if successful, also logs them in)
		LoginView myView = (LoginView) this.getView();
		String username = myView.getRegisterUsername();
		String password = myView.getRegisterPassword();
		String rePassword = myView.getRegisterPasswordRepeat();

		if (!password.equals(rePassword)) {
			JOptionPane.showMessageDialog(null,
					"The passwords you entered do not match.",
					"Register Error",
					JOptionPane.INFORMATION_MESSAGE);
		}
		else if (!(this.facade.register(username, password))) {
			JOptionPane
					.showMessageDialog(
							null,
							"Could not register not sure if you are already registered or if the info just failed.",
							"Register Error",
							JOptionPane.INFORMATION_MESSAGE);
		}
		else {
			// If register succeeded
			this.getLoginView().closeModal();
			this.loginAction.execute();
		}
	}

}
