package com.demo.fmalc_android.contract;

import com.demo.fmalc_android.entity.FuelRequest;

public interface FuelContract {
    interface View{
        void saveFuelFillingSuccess();
        void saveFuelFillingFailure(String message);
    }

    interface Presenter{
        void saveFuelFilling(FuelRequest fuelRequest, String auth);
    }
}
