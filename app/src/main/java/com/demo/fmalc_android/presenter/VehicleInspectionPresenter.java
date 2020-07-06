package com.demo.fmalc_android.presenter;

import com.demo.fmalc_android.contract.ConsignmentContract;
import com.demo.fmalc_android.contract.VehicleContract;
import com.demo.fmalc_android.entity.Consignment;
import com.demo.fmalc_android.entity.VehicleInspection;
import com.demo.fmalc_android.retrofit.NetworkingUtils;
import com.demo.fmalc_android.service.ConsignmentService;
import com.demo.fmalc_android.service.VehicleService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VehicleInspectionPresenter implements VehicleContract.Presenter {

    VehicleContract.View view;

    public void setView(VehicleContract.View view) {
        this.view = view;
    }

    VehicleService vehicleService = NetworkingUtils.getVehicleService();



    @Override
    public void getListLicensePlate(List<Integer> status, String username) {
        Call<VehicleInspection> call = vehicleService.getVehiclePlateAndInspection(status, username);
        call.enqueue(new Callback<VehicleInspection>() {
            @Override
            public void onResponse(Call<VehicleInspection> call, Response<VehicleInspection> response) {
                if(!response.isSuccessful()){
                    view.getListLicensePlateAndInspectionFailure("Không thể lấy thông tin");
                }else {
                  if(response.code() == 200) {
                      VehicleInspection vehicleInspection = response.body();
                      view.getListLicensePlateAndInspectionSuccess(vehicleInspection);
                  } else {
                      view.getListLicensePlateAndInspectionFailure("Không thể lấy thông tin");
                  }
                }
            }

            @Override
            public void onFailure(Call<VehicleInspection> call, Throwable t) {
                view.getListLicensePlateAndInspectionFailure("Có lỗi xảy ra trong quá trình lấy thông tin " + t.getMessage());
            }

        });
    }
}
