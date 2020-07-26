package com.demo.fmalc_android.contract;


import com.demo.fmalc_android.entity.ConsignmentDetail;
import com.demo.fmalc_android.entity.Location;
import com.demo.fmalc_android.entity.Notification;
import com.demo.fmalc_android.entity.Place;
import com.demo.fmalc_android.entity.VehicleDetail;

import java.util.List;

import okhttp3.ResponseBody;

public interface ConsignmentDetailContract {
    interface View{
        void findByConsignmentIdSuccess(ConsignmentDetail consignmentDetail);
        void findByConsignmentIdFailure(String message);

        void trackingLocationSuccess(ResponseBody responseBody);
        void trackingLocationSFailed(String message);
        void getVehicleDetailByLicenseSuccess(VehicleDetail responseBody);
        void getVehicleDetailByLicenseFailed(String responseBody);


        void sendNotificationSuccess(Notification notification);
        void sendNotificationFailed(String notification);
    }

    interface Presenter{
        void findByConsignmentId(Integer id);
        void trackingLocation(Location location);
        void getVehicleDetailByLicense(String license);
        void sendNotification(Notification notification);
    }
}
