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
//    private static LoginResponse loginResponse;

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
                    if(response.code() == 204){
                        view.loginFailure("Tài khoản không tồn tại");
                    }else if (response.code() == 200){
                        view.loginSuccess(response.body());
                    }else if (response.code() == 401){
                        view.loginFailure("Username hoặc Password không hợp lệ");
                    }else{
                        view.loginFailure("Có lỗi xảy ra trong quá trình đăng nhập");
                    }
                }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                if (t.getMessage().contains("timed out")){
                    view.loginFailure("Vui lòng kiểm tra lại kết nối mạng");
                }else {
                    view.loginFailure("Server đang gặp sự cố. Xin thử lại sau! ");
                }
            }
        });
    }
}
