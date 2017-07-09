package test;

import java.io.FileNotFoundException;

import ch.aonyx.broker.ib.api.CallbackObject;
import ch.aonyx.broker.ib.api.ClientCallback;
import ch.aonyx.broker.ib.api.NeoIbApiClient;
import ch.aonyx.broker.ib.api.NeoIbApiClientException;
import ch.aonyx.broker.ib.api.Session;
import ch.aonyx.broker.ib.api.account.AccountUpdateSubscriptionRequest;
import ch.aonyx.broker.ib.api.account.ManagedAccountListRequest;
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
				PortfolioListener pl = new PortfolioListener(session);
				session.registerListener(pl);
				
				TradeLogListener tll = new TradeLogListener(session);
				session.registerListener(tll);
				Thread tllt = new Thread(tll);
				
				AccListListener all = new AccListListener();
				session.registerListener(all);
				session.start();
				
				ManagedAccountListRequest r = new ManagedAccountListRequest();
				session.request(r);
				
				//wait for account numbers to populate
				while(all.accounts.isEmpty()){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				for (String accnum : all.accounts) {
					session.subscribe(new AccountUpdateSubscriptionRequest(accnum));
				}
				tll.setAccounts(all.accounts);
				tllt.start();
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
