package com.demo.fmalc_android.presenter;

import com.demo.fmalc_android.contract.LoginContract;
import com.demo.fmalc_android.entity.Account;
import com.demo.fmalc_android.entity.GlobalVariable;
import com.demo.fmalc_android.entity.LoginResponse;
import com.demo.fmalc_android.retrofit.NetworkingUtils;
import com.demo.fmalc_android.service.AccountService;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import lombok.SneakyThrows;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View view;

    public void setView(LoginContract.View view) {
        this.view = view;
    }

    AccountService accountService = NetworkingUtils.getAccountApiInstance();

    @Override
    public void doLogin(String username, String password) {
        Account account = new Account(username, password);
        Call<LoginResponse> call = accountService.login(account);

        call.enqueue(new Callback<LoginResponse>() {
            @SneakyThrows
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if(!response.isSuccessful()){
                        view.loginFailure("Đăng nhập thất bại");
                    }else {
                      GlobalVariable globalVariable = new GlobalVariable();
                        LoginResponse loginResponse = response.body();
                        globalVariable.setUsername(loginResponse.getUsername());
                        globalVariable.setToken(loginResponse.getToken());
                        globalVariable.setRole(loginResponse.getRole());
                        view.loginSuccess();
                    }
                }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                view.loginFailure("Đã xảy ra lỗi trong quá trình đăng nhập " + t.getMessage());
            }
        });
    }
}
