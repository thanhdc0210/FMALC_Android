package com.demo.fmalc_android.contract;

import com.demo.fmalc_android.entity.AlertRequestDTO;
import com.demo.fmalc_android.entity.Notification;
import com.demo.fmalc_android.entity.VehicleDetail;
import com.demo.fmalc_android.retrofit.NetworkingUtils;
import com.demo.fmalc_android.service.VehicleService;

import java.util.List;

import okhttp3.ResponseBody;

public  interface AlertContract {
    interface View{
        void sendRequestWhileRunningSuccess(AlertRequestDTO alertRequestDTO);
        void sendRequestWhileRunningFailure(String message);

        void sendNotificationSuccess (String success);
        void sendNotificationFailed (String message);

        void getDetailVehicleSuccess(VehicleDetail vehicleDetail);
        void getDetailVehicleFailed(String message);
    }
    interface Presenter{
//        void findVehicleLicensePlatesAndInspectionForReportInspectionBeforeDelivery(List<Integer> status, String username, String auth);
//
//        void getVehicleRunning(String username, String auth);
        void sendRequestWhileRunning(AlertRequestDTO requestDTO, String auth);
        void sendNotification(Notification notification);
        void getDetailVehicle(int id);

    }

}
