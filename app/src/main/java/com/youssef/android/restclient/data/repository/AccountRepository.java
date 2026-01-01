package com.youssef.android.restclient.data.repository;

import com.youssef.android.restclient.data.api.*
import com.youssef.android.restclient.model.Account;
import ma.projet.restclient.entities.AccountList;
import com.youssef.android.restclient.config.*

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountRepository {
    private AccountService AccountService;
    private String format;

    public AccountRepository(String converterType) {
        AccountService = RetrofitClient.getClient(converterType).create(AccountService.class);
        this.format = converterType;
    }

    public void getAllAccount(Callback<List<Account>> callback) {
        if ("JSON".equals(format)) {
            Call<List<Account>> call = AccountService.getAllAccountJson();
            call.enqueue(callback);
        } else {
            Call<AccountList> call = AccountService.getAllAccountXml();
            call.enqueue(new Callback<AccountList>() {
                @Override
                public void onResponse(Call<AccountList> call, Response<AccountList> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Account> Accounts = response.body().getAccounts();
                        callback.onResponse(null, Response.success(Accounts));
                    }
                }

                @Override
                public void onFailure(Call<AccountList> call, Throwable t) {
                }
            });
        }
    }

    public void getAccountById(Long id, Callback<Account> callback) {
        Call<Account> call = AccountService.getAccountById(id);
        call.enqueue(callback);
    }

    public void addAccount(Account Account, Callback<Account> callback) {
        Call<Account> call = AccountService.addAccount(Account);
        call.enqueue(callback);
    }

    public void updateAccount(Long id, Account Account, Callback<Account> callback) {
        Call<Account> call = AccountService.updateAccount(id, Account);
        call.enqueue(callback);
    }

    public void deleteAccount(Long id, Callback<Void> callback) {
        Call<Void> call = AccountService.deleteAccount(id);
        call.enqueue(callback);
    }
}
