package com.demo.fmalc_android.contract;

import com.demo.fmalc_android.entity.VehicleInspection;

import java.util.List;

public interface VehicleContract {
    interface View{
        void getListLicensePlateAndInspectionSuccess(VehicleInspection vehicleInspection);
        void getListLicensePlateAndInspectionFailure(String message);
    }

    interface Presenter{
        void getListLicensePlate(List<Integer> status, String username);
    }
}
