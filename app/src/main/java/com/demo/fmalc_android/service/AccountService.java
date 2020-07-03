package com.demo.fmalc_android.service;

import com.demo.fmalc_android.entity.Account;
import com.demo.fmalc_android.entity.LoginResponse;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AccountService {

    @POST("login")
    Call<LoginResponse> login(@Body Account account);

}
