package test;

import java.util.HashSet;
import java.util.Set;

import ch.aonyx.broker.ib.api.account.ManagedAccountListEvent;
import ch.aonyx.broker.ib.api.account.ManagedAccountListEventListener;

public class AccListListener implements ManagedAccountListEventListener{
	Set<String> accounts = new HashSet<String>();
	
	@Override
	public void notify(ManagedAccountListEvent event) {
		String[] s = event.getCommaSeparatedAccountList().split(",");
		for (int i = 0; i < s.length; i++) {
			accounts.add(s[i]);
		}
	}

}
