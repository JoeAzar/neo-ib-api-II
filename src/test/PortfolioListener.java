package test;

import ch.aonyx.broker.ib.api.Session;
import ch.aonyx.broker.ib.api.account.PortfolioUpdateEvent;
import ch.aonyx.broker.ib.api.account.PortfolioUpdateEventListener;

public class PortfolioListener implements PortfolioUpdateEventListener {

	Session session;
	
	public PortfolioListener(Session session) {
		this.session = session;
		// may need to send mkt data req for each instrument in port to get realtime data
	}
	
	@Override
	public void notify(PortfolioUpdateEvent event) {
		System.out.println(event);
	}
}
