package com.youssef.android.restclient.data.api;

import com.youssef.android.restclient.model.Account;
import ma.projet.restclient.entities.AccountList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AccountService {
    @GET("banque/Accounts")
    @Headers("Accept: application/json")
    Call<List<Account>> getAllAccountJson();

    @GET("banque/Accounts")
    @Headers("Accept: application/xml")
    Call<AccountList> getAllAccountXml();

    @GET("banque/Accounts/{id}")
    Call<Account> getAccountById(@Path("id") Long id);

    @POST("banque/Accounts")
    Call<Account> addAccount(@Body Account Account);

    @PUT("banque/Accounts/{id}")
    Call<Account> updateAccount(@Path("id") Long id, @Body Account Account);

    @DELETE("banque/Accounts/{id}")
    Call<Void> deleteAccount(@Path("id") Long id);
}
