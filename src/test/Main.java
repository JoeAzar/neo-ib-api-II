package test;

import java.io.FileNotFoundException;

import ch.aonyx.broker.ib.api.CallbackObject;
import ch.aonyx.broker.ib.api.ClientCallback;
import ch.aonyx.broker.ib.api.NeoIbApiClient;
import ch.aonyx.broker.ib.api.NeoIbApiClientException;
import ch.aonyx.broker.ib.api.Session;
import ch.aonyx.broker.ib.api.account.AccountUpdateSubscriptionRequest;
import ch.aonyx.broker.ib.api.net.ConnectionCallback;
import ch.aonyx.broker.ib.api.net.ConnectionException;
import ch.aonyx.broker.ib.api.net.ConnectionParameters;

public class Main {

	public static final boolean DEBUG = false; // toggle debug messages
	public static Main instance;
	Session mysession;

	public static void main(final String[] args) throws FileNotFoundException {
		// System.setOut(new PrintStream(new File("test.txt"))); //set logging
		// to file
		instance = new Main();
	}

	private Main() {
		final NeoIbApiClient apiClient = new NeoIbApiClient(new MyClientCallback());
		apiClient.connect(new ConnectionParameters(1), new ConnectionCallback() {
			@Override
			public void onSuccess(final Session session) {
				session.registerListener(new PortfolioListener());
				session.registerListener(new TradeLogListener(session));

				session.subscribe(new AccountUpdateSubscriptionRequest("DU15207"));

				session.start();
			}

			@Override
			public void onFailure(final ConnectionException exception) {
				System.err.println(exception);
			}
		});
	}
}

class MyClientCallback implements ClientCallback {

	@Override
	public void onSuccess(CallbackObject object) {}

	@Override
	public void onFailure(NeoIbApiClientException exception) {}
}
