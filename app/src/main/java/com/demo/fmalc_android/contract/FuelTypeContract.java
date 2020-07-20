package com.demo.fmalc_android.contract;

import com.demo.fmalc_android.entity.FuelType;
import com.demo.fmalc_android.entity.FuelTypeResponse;

import java.util.List;

public interface FuelTypeContract {
    interface View{
        void getListFuelTypeSuccess(FuelTypeResponse fuelTypeResponse);
        void getListFuelTypeFailure(String message);
    }

    interface Presenter{
        void getListFuelTypes(String username, List<Integer> status);
    }
}
