package com.demo.fmalc_android.contract;

import com.demo.fmalc_android.entity.FuelType;

import java.util.List;

public interface FuelTypeContract {
    interface View{
        void getListFuelTypeSuccess(List<FuelType> fuelTypeList);
        void getListFuelTypeFailure(String message);
    }

    interface Presenter{
        void getListFuelTypes();
    }
}
