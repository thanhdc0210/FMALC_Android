package com.demo.fmalc_android.presenter;

import com.demo.fmalc_android.contract.FuelTypeContract;
import com.demo.fmalc_android.contract.VehicleContract;
import com.demo.fmalc_android.entity.FuelType;
import com.demo.fmalc_android.entity.VehicleInspection;
import com.demo.fmalc_android.retrofit.NetworkingUtils;
import com.demo.fmalc_android.service.FuelTypeService;
import com.demo.fmalc_android.service.VehicleService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FuelTypePresenter implements FuelTypeContract.Presenter {

    FuelTypeContract.View view;

    public void setView(FuelTypeContract.View view) {
        this.view = view;
    }

    FuelTypeService fuelTypeService = NetworkingUtils.getFuelTypeService();



    @Override
    public void getListFuelTypes() {
        Call<List<FuelType>> call = fuelTypeService.getFuelTypeList();
        call.enqueue(new Callback<List<FuelType>>() {
            @Override
            public void onResponse(Call<List<FuelType>> call, Response<List<FuelType>> response) {
                if (!response.isSuccessful()) {
                    view.getListFuelTypeFailure("Không thể lấy danh sách nhiên liệu");
                } else {
                    if (response.code() == 200) {
                        List<FuelType> fuelTypeList = response.body();
                        view.getListFuelTypeSuccess(fuelTypeList);
                    } else {
                        view.getListFuelTypeFailure("Không thể lấy danh sách nhiên liệu");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<FuelType>> call, Throwable t) {
                view.getListFuelTypeFailure("Có lỗi trong quá trình lấy thông tin "+t.getMessage());
            }
        });

    }
}
