package com.demo.fmalc_android.contract;

import com.demo.fmalc_android.entity.DriverInformation;

public interface DriverContract {
    interface View{
        void getDriverInformationSuccessful(DriverInformation driverInformation);
        void getDriverInformationFailure(String message);
    }

    interface Presenter{
        void getDriverInformation(Integer id);
    }
}
