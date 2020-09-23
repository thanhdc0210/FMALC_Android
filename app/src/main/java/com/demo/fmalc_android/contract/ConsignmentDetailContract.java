package com.demo.fmalc_android.contract;


//import com.demo.fmalc_android.entity.ConsignmentDetail;
import android.app.Activity;

import com.demo.fmalc_android.entity.DetailedSchedule;
import com.demo.fmalc_android.entity.Location;
import com.demo.fmalc_android.entity.Maintenance;
import com.demo.fmalc_android.entity.Notification;
import com.demo.fmalc_android.entity.Place;
import com.demo.fmalc_android.entity.VehicleDetail;

import java.util.List;

import okhttp3.ResponseBody;

public interface ConsignmentDetailContract {
    interface View{
        void findByConsignmentIdSuccess(DetailedSchedule consignmentDetail); // Dùng id của schedule
        void findByConsignmentIdFailure(String message);

        void trackingLocationSuccess(ResponseBody responseBody);
        void trackingLocationSFailed(String message);
        void getVehicleDetailByLicenseSuccess(VehicleDetail responseBody);
        void getVehicleDetailByLicenseFailed(String responseBody);


        void sendNotificationSuccess(Notification notification);
        void sendNotificationFailed(String notification);

        void updateActualTimeSuccess(Place place);
        void updateActualTimeFailed(String message);

        void updatePlannedTimeSuccess(Maintenance responseBody);
        void updatePlannedTimeFailed(String responseBody);

        void stopTrackingSuccess(String string);
        void stopTrackingFailed(String message);

        void notifyForManagerSuccess(String success);
        void notifyForManagerFailed(String message);

        void getFirstConsignmentSuccess(int idConsignment);
        void getFirstConsignmentFailed(String message);
    }

    interface Presenter{
        void findByConsignmentId(Integer scheduleId); // Dùng id của schedule
        void trackingLocation(Location location);
        void getVehicleDetailByLicense(String license);
        void sendNotification(Notification notification);
        void updateActualTime(Integer placeId, Integer idSchedule);
        void updatePlannedTime(Integer id, Integer km);
        void stopTracking(Integer id);
        void notifyForManager(Notification notification);
        void getFirstConsignment(int idDriver);
    }
}
