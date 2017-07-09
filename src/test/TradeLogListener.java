package test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ch.aonyx.broker.ib.api.Session;
import ch.aonyx.broker.ib.api.contract.Contract;
import ch.aonyx.broker.ib.api.execution.ExecutionReport;
import ch.aonyx.broker.ib.api.execution.ExecutionReportEvent;
import ch.aonyx.broker.ib.api.execution.ExecutionReportEventListener;
import ch.aonyx.broker.ib.api.execution.ExecutionReportFilter;
import ch.aonyx.broker.ib.api.execution.ExecutionReportRequest;

public class TradeLogListener implements ExecutionReportEventListener, Runnable {

	Session session;
	Map<String, Set<FullExec>> trademap = new HashMap<>();
	Map<String, Set<String>> idmap = new HashMap<>();
	Set<String> accounts;

	public TradeLogListener(Session session) {
		this.session = session;
	}

	@Override
	public void run() {
		while (true) {
			request();
			try {
				Thread.sleep(10000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void request() {
		for (String accstr : accounts) {
			if (!trademap.containsKey(accstr)) {
				trademap.put(accstr, new HashSet<FullExec>());
				idmap.put(accstr, new HashSet<String>());
			}
			ExecutionReportFilter erf = new ExecutionReportFilter();
			erf.setAccountNumber(accstr);
			session.request(new ExecutionReportRequest(erf));
		}
	}

	public void setAccounts(Set<String> accounts) {
		this.accounts = accounts;
	}

	@Override
	public void notify(ExecutionReportEvent event) {
		Set<String> idset = idmap.get(event.getExecutionReport().getAccountNumber());
		Set<FullExec> trades = trademap.get(event.getExecutionReport().getAccountNumber());
		if (idset.contains(event.getExecutionReport().getExecutionId()))
			return;
		FullExec newFE = new FullExec(event);
		boolean added = trades.add(newFE);
		// System.out.println("here");
		if (!added) {
			for (FullExec full : trades) {
				if (full.equals(newFE)) {
					full.er.setFilledQuantity(full.er.getFilledQuantity() + newFE.er.getFilledQuantity());
					idset.add(newFE.er.getExecutionId());
					SQLUtils.updateTL(newFE);
					return;
				}
			}
			System.err.println("Execution " + newFE.er.getExecutionId() + " has conflicting permanent ID but no parent.");
		}
		else {
			idset.add(newFE.er.getExecutionId());
			SQLUtils.insertTL(newFE);
		}
	}
}

class FullExec {
	Contract c;
	ExecutionReport er;

	public FullExec(ExecutionReportEvent event) {
		c = event.getContract();
		er = event.getExecutionReport();
	}

	@Override
	public boolean equals(Object fe) {
		if (!(fe instanceof FullExec))
			return false;
		return er.getPermanentId() == ((FullExec) fe).er.getPermanentId();
	}

	@Override
	public int hashCode() {
		return er.getPermanentId();
	}

	@Override
	public String toString() {
		return c.toString() + " " + er.toString();
	}
}