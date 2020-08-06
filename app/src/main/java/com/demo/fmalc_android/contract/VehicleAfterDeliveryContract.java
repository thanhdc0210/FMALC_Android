package com.demo.fmalc_android.contract;

import com.demo.fmalc_android.entity.ReportIssueRequest;
import com.demo.fmalc_android.entity.VehicleInspection;

import java.util.List;

public interface VehicleAfterDeliveryContract {
    interface View{
        void getListLicensePlateAndInspectionAfterDeliverySuccess(VehicleInspection vehicleInspection);
        void getListLicensePlateAndInspectionAfterDeliveryFailure(String message);
    }

    interface Presenter {
        void getListLicensePlateAndInspectionAfterDelivery(String username, String auth);
    }
}
