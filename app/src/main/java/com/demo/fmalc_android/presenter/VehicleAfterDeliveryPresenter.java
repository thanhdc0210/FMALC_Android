package com.demo.fmalc_android.presenter;

import com.demo.fmalc_android.contract.VehicleAfterDeliveryContract;
import com.demo.fmalc_android.entity.ReportIssueRequest;
import com.demo.fmalc_android.entity.VehicleInspection;
import com.demo.fmalc_android.retrofit.NetworkingUtils;
import com.demo.fmalc_android.service.VehicleService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VehicleAfterDeliveryPresenter implements VehicleAfterDeliveryContract.Presenter {

    VehicleAfterDeliveryContract.View view;

    public void setView(VehicleAfterDeliveryContract.View view) {
        this.view = view;
    }

    VehicleService vehicleService = NetworkingUtils.getVehicleService();

    @Override
    public void getListLicensePlateAndInspectionAfterDelivery( String username) {
        Call<VehicleInspection> call = vehicleService.findVehicleLicensePlatesAndInspectionForReportInspectionAfterDelivery( username);
        call.enqueue(new Callback<VehicleInspection>() {
            @Override
            public void onResponse(Call<VehicleInspection> call, Response<VehicleInspection> response) {
                if(!response.isSuccessful()){
                    view.getListLicensePlateAndInspectionAfterDeliveryFailure("Không thể lấy thông tin");
                }else {
                    if(response.code() == 200) {
                        VehicleInspection vehicleInspection = response.body();
                        view.getListLicensePlateAndInspectionAfterDeliverySuccess(vehicleInspection);
                    } else {
                        view.getListLicensePlateAndInspectionAfterDeliveryFailure("Không thể lấy thông tin");
                    }
                }
            }

            @Override
            public void onFailure(Call<VehicleInspection> call, Throwable t) {
                view.getListLicensePlateAndInspectionAfterDeliveryFailure("Có lỗi xảy ra trong quá trình lấy thông tin " + t.getMessage());
            }

        });
    }
}
