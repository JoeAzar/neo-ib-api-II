package test;

import ch.aonyx.broker.ib.api.CallbackObject;
import ch.aonyx.broker.ib.api.ClientCallback;
import ch.aonyx.broker.ib.api.NeoIbApiClient;
import ch.aonyx.broker.ib.api.NeoIbApiClientException;
import ch.aonyx.broker.ib.api.Session;
import ch.aonyx.broker.ib.api.contract.Contract;
import ch.aonyx.broker.ib.api.contract.ContractSpecificationEvent;
import ch.aonyx.broker.ib.api.contract.ContractSpecificationEventListener;
import ch.aonyx.broker.ib.api.contract.ContractSpecificationRequest;
import ch.aonyx.broker.ib.api.contract.SecurityType;
import ch.aonyx.broker.ib.api.data.CompositeTickEvent;
import ch.aonyx.broker.ib.api.data.CompositeTickEventListener;
import ch.aonyx.broker.ib.api.data.MarketDataSubscriptionRequest;
import ch.aonyx.broker.ib.api.data.TickGenericEvent;
import ch.aonyx.broker.ib.api.data.TickGenericEventListener;
import ch.aonyx.broker.ib.api.data.TickSizeEvent;
import ch.aonyx.broker.ib.api.data.TickSizeEventListener;
import ch.aonyx.broker.ib.api.net.ConnectionCallback;
import ch.aonyx.broker.ib.api.net.ConnectionException;
import ch.aonyx.broker.ib.api.net.ConnectionParameters;

public class Main {
	public static void main(final String[] args) {
		new Main();
	}

	private Main() {
		final NeoIbApiClient apiClient = new NeoIbApiClient(new MyClientCallback());
		apiClient.connect(new ConnectionParameters(1), new ConnectionCallback() {
			@Override
			public void onSuccess(final Session session) {
				System.out.println("something");
				session.registerListener(new MyContractSpecificationEventListener());
				session.registerListener(new MyTickSizeEventListener());
				session.registerListener(new MyTickGenericEventListener());
				session.registerListener(new MyCompositeTickEventListener());

				Contract contract = getContract("SPY");
				session.request(new ContractSpecificationRequest(contract));
				session.subscribe(new MarketDataSubscriptionRequest(contract));

				session.start();
				System.out.println("started");
			}

			private Contract getContract(final String symbol) {
				final Contract contract = new Contract();
				contract.setSecurityType(SecurityType.STOCK);
				contract.setSymbol(symbol);
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFailure(NeoIbApiClientException exception) {
		// TODO Auto-generated method stub
		
	}
}

class MyContractSpecificationEventListener implements ContractSpecificationEventListener {

	@Override
	public void notify(ContractSpecificationEvent event) {
		System.out.println("here1");
		
	}
}
class MyTickSizeEventListener implements TickSizeEventListener {

	@Override
	public void notify(TickSizeEvent event) {
		// TODO Auto-generated method stub
		System.out.println("here2");
	}
}
class MyTickGenericEventListener implements TickGenericEventListener {

	@Override
	public void notify(TickGenericEvent event) {
		// TODO Auto-generated method stub
		System.out.println("here3");
	}
}
class MyCompositeTickEventListener implements CompositeTickEventListener {

	@Override
	public void notify(CompositeTickEvent event) {
		// TODO Auto-generated method stub
		System.out.println("here4");
	}
}
