package com.demo.fmalc_android.presenter;

import com.demo.fmalc_android.contract.FuelTypeContract;
import com.demo.fmalc_android.entity.FuelType;
import com.demo.fmalc_android.entity.FuelTypeResponse;
import com.demo.fmalc_android.retrofit.NetworkingUtils;
import com.demo.fmalc_android.service.FuelTypeService;

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
    public void getListFuelTypes(String username, List<Integer> status) {
        Call<FuelTypeResponse> call = fuelTypeService.getFuelTypeResponse(username, status);
        call.enqueue(new Callback<FuelTypeResponse>() {
            @Override
            public void onResponse(Call<FuelTypeResponse> call, Response<FuelTypeResponse> response) {
                if (response.code() == 204){
                    view.getListFuelTypeFailure("Không có thông tin");
                }else if (response.code() == 200){
                    view.getListFuelTypeSuccess(response.body());
                }else{
                    view.getListFuelTypeFailure("Có lỗi xảy ra trong quá trình lấy thông tin");
                }
            }

            @Override
            public void onFailure(Call<FuelTypeResponse> call, Throwable t) {
                if (t.getMessage().contains("timed out")){
                    view.getListFuelTypeFailure("Lỗi kết nối mạng");
                }else {
                    view.getListFuelTypeFailure("Có lỗi xảy ra ở server");
                }
            }
        });
    }
}
