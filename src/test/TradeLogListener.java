package test;

import ch.aonyx.broker.ib.api.Session;
import ch.aonyx.broker.ib.api.execution.ExecutionReportEvent;
import ch.aonyx.broker.ib.api.execution.ExecutionReportEventListener;
import ch.aonyx.broker.ib.api.execution.ExecutionReportFilter;
import ch.aonyx.broker.ib.api.execution.ExecutionReportRequest;

public class TradeLogListener implements ExecutionReportEventListener {

	public TradeLogListener(Session s) {
		//request updates here
		ExecutionReportFilter erf = new ExecutionReportFilter();
		erf.setAccountNumber("DU15207");
		s.request(new ExecutionReportRequest(erf));
	}
	
	@Override
	public void notify(ExecutionReportEvent event) {
		System.out.println(event.getContract() + " " + event.getExecutionReport());
		
	}
}