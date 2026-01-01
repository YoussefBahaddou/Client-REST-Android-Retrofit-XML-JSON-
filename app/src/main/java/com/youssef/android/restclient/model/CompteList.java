package com.youssef.android.restclient.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import java.util.List;

@Root(name = "List", strict = false)
public class AccountList {
    @ElementList(inline = true, entry = "item")
    private List<Account> Accounts;

    public List<Account> getAccounts() {
        return Accounts;
    }

    public void setAccounts(List<Account> Accounts) {
        this.Accounts = Accounts;
    }
}
