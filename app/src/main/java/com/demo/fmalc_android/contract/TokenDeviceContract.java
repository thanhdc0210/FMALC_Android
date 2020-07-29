package com.demo.fmalc_android.contract;

public interface TokenDeviceContract {
    interface View{
        void updateTokenDeviceSuccess();
        void updateTokenDeviceFailure(String message);
    }

    interface Presenter{
        void updateTokenDevice(Integer id, String tokenDevice);
    }
}
