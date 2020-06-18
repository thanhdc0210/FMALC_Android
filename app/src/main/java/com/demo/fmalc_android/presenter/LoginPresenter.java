package com.demo.fmalc_android.presenter;

import com.demo.fmalc_android.contract.LoginContract;
import com.demo.fmalc_android.entity.Account;
import com.demo.fmalc_android.entity.GlobalVariable;
import com.demo.fmalc_android.entity.Token;
import com.demo.fmalc_android.retrofit.NetworkingUtils;
import com.demo.fmalc_android.service.AccountService;

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
        Call<Token> call = accountService.login(account);

        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                    if(!response.isSuccessful()){
                        view.loginFailure("Đăng nhập thất bại");
                    }else {
                        GlobalVariable globalVariable = new GlobalVariable();
                        globalVariable.setToken(response.body().getToken());
                        globalVariable.setUsername(username);
                    }
                }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                view.loginFailure("Đã xảy ra lỗi trong quá trình đăng nhập");
            }
        });
    }
}
