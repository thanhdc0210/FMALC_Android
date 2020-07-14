package com.demo.fmalc_android.contract;

import com.demo.fmalc_android.entity.Account;
import com.demo.fmalc_android.entity.LoginResponse;

public interface LoginContract {

    interface View{
        void loginSuccess(LoginResponse loginResponse);
        void loginFailure(String message);
    }

    interface Presenter{
        void doLogin(String username, String password);
    }
}
