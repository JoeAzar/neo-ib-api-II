package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import ch.aonyx.broker.ib.api.CallbackObject;
import ch.aonyx.broker.ib.api.ClientCallback;
import ch.aonyx.broker.ib.api.NeoIbApiClient;
import ch.aonyx.broker.ib.api.NeoIbApiClientException;
import ch.aonyx.broker.ib.api.Session;
import ch.aonyx.broker.ib.api.account.AccountUpdateSubscriptionRequest;
import ch.aonyx.broker.ib.api.account.PortfolioUpdateEvent;
import ch.aonyx.broker.ib.api.account.PortfolioUpdateEventListener;
import ch.aonyx.broker.ib.api.contract.Contract;
import ch.aonyx.broker.ib.api.contract.ContractSpecificationRequest;
import ch.aonyx.broker.ib.api.contract.SecurityType;
import ch.aonyx.broker.ib.api.data.CompositeTickEvent;
import ch.aonyx.broker.ib.api.data.CompositeTickEventListener;
import ch.aonyx.broker.ib.api.data.MarketDataSubscriptionRequest;
import ch.aonyx.broker.ib.api.net.ConnectionCallback;
import ch.aonyx.broker.ib.api.net.ConnectionException;
import ch.aonyx.broker.ib.api.net.ConnectionParameters;

public class Main {
	public static void main(final String[] args) throws FileNotFoundException {
		System.setOut(new PrintStream(new File("test.txt")));
		new Main();
	}

	private Main() {
		final NeoIbApiClient apiClient = new NeoIbApiClient(new MyClientCallback());
		apiClient.connect(new ConnectionParameters(1), new ConnectionCallback() {
			@Override
			public void onSuccess(final Session session) {
				session.registerListener(new MyCompositeTickEventListener());
				session.registerListener(new MyPortfolioUpdateEventListener());
				
//				Contract contract = getContract("SPY");
//				session.request(new ContractSpecificationRequest(contract));
//				session.subscribe(new MarketDataSubscriptionRequest(contract));
				session.subscribe(new AccountUpdateSubscriptionRequest("DU15130"));

				session.start();
			}

			private Contract getContract(final String symbol) {
				final Contract contract = new Contract();
				contract.setSecurityType(SecurityType.STOCK);
				contract.setSymbol(symbol);
				contract.setExchange("SMART");
				contract.setCurrencyCode("USD");
				return contract;
			}

			@Override
			public void onFailure(final ConnectionException exception) {
				System.out.println(exception);
			}
		});
	}
}

class MyClientCallback implements ClientCallback {

	@Override
	public void onSuccess(CallbackObject object) {
		System.out.println("THIS: " + object);

	}

	@Override
	public void onFailure(NeoIbApiClientException exception) {
		// TODO Auto-generated method stub

	}
}


class MyCompositeTickEventListener implements CompositeTickEventListener {

	@Override
	public void notify(CompositeTickEvent event) {
		System.out.println("here1");
	}
}

class MyPortfolioUpdateEventListener implements PortfolioUpdateEventListener {

	@Override
	public void notify(PortfolioUpdateEvent event) {
		System.out.println("here2");
	}
}

