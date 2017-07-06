package test;

import ch.aonyx.broker.ib.api.account.PortfolioUpdateEvent;
import ch.aonyx.broker.ib.api.account.PortfolioUpdateEventListener;

public class PortfolioListener implements PortfolioUpdateEventListener {

	@Override
	public void notify(PortfolioUpdateEvent event) {
		System.out.println(event);
	}
}
