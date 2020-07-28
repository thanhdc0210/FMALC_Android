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
                if (response.code() == 204) {
                    view.getListLicensePlateAndInspectionAfterDeliveryFailure("Không thể lưu báo cáo");
                } else if (response.code() == 200) {
                    view.getListLicensePlateAndInspectionAfterDeliverySuccess(response.body());
                }else{
                    view.getListLicensePlateAndInspectionAfterDeliveryFailure("Có lỗi xảy ra trong quá trình báo cáo");
                }
            }

            @Override
            public void onFailure(Call<VehicleInspection> call, Throwable t) {
                if (t.getMessage().contains("timed out")){
                    view.getListLicensePlateAndInspectionAfterDeliveryFailure("Vui lòng kiểm tra lại kết nối mạng");
                }else {
                    view.getListLicensePlateAndInspectionAfterDeliveryFailure("Server đang gặp sự cố. Xin thử lại sau!");
                }
            }

        });
    }
}
