package com.demo.fmalc_android.contract;

import com.demo.fmalc_android.entity.AlertRequestDTO;
import com.demo.fmalc_android.entity.VehicleInspection;

import java.util.List;

public interface VehicleContract {
    interface View{
        void getListLicensePlateAndInspectionSuccess(VehicleInspection vehicleInspection);
        void getListLicensePlateAndInspectionFailure(String message);

        void getVehicleRunningSuccess(int vehicleId);
        void getVehicleRunningFailure(String message);


        void sendRequestWhileRunningSuccess(String s);
        void sendRequestWhileRunningFailure(String message);

    }

    interface Presenter{
        void getListLicensePlate(String username, String auth);

        void getVehicleRunning(String username, String auth);
        void sendRequestWhileRunning(AlertRequestDTO requestDTO,String auth);


    }
}
