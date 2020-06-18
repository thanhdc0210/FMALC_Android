package com.demo.fmalc_android.contract;

import com.demo.fmalc_android.entity.Account;

public interface LoginContract {

    interface View{
        void loginSuccess();
        void loginFailure(String message);
    }

    interface Presenter{
        void doLogin(String username, String password);
    }
}
