package com.demo.fmalc_android.presenter;

import com.demo.fmalc_android.contract.LoginContract;
import com.demo.fmalc_android.entity.Account;
import com.demo.fmalc_android.entity.GlobalVariable;
import com.demo.fmalc_android.retrofit.NetworkingUtils;
import com.demo.fmalc_android.service.AccountService;

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
        Call<JSONObject> call = accountService.login(account);

        call.enqueue(new Callback<JSONObject>() {
            @SneakyThrows
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                    if(!response.isSuccessful()){
                        view.loginFailure("Đăng nhập thất bại");
                    }else {
                        GlobalVariable globalVariable = new GlobalVariable();
                        JSONObject jsonObject = response.body();
                        globalVariable.setUsername(username);
                    }
                }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                view.loginFailure("Đã xảy ra lỗi trong quá trình đăng nhập");
            }
        });
    }
}
