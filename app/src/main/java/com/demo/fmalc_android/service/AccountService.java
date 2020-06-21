package com.demo.fmalc_android.service;

import com.demo.fmalc_android.entity.Account;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AccountService {

    @POST("login")
    Call<JSONObject> login(@Body Account account);

}
