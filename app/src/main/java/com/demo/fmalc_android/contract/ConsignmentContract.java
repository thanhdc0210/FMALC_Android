package com.demo.fmalc_android.contract;

import com.demo.fmalc_android.entity.StatusRequest;

public interface ConsignmentContract {
    interface View{
        void loginSuccess();
        void loginFailure(String message);
    }

    interface Presenter{
        void findByConsignmentStatusAndUsernameForFleetManager(StatusRequest statusRequest, String token);
    }
}
