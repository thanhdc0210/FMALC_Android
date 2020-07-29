package com.demo.fmalc_android.presenter;

import com.demo.fmalc_android.contract.FuelContract;
import com.demo.fmalc_android.entity.FuelRequest;
import com.demo.fmalc_android.retrofit.NetworkingUtils;
import com.demo.fmalc_android.service.FuelService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FuelPresenter implements FuelContract.Presenter {

    FuelContract.View view;

    public void setView(FuelContract.View view) {
        this.view = view;
    }

    FuelService fuelService = NetworkingUtils.getFuelService();

    @Override
    public void saveFuelFilling(FuelRequest fuelRequest) {
        Call<FuelRequest> call = fuelService.saveFuelFilling(fuelRequest);

        call.enqueue(new Callback<FuelRequest>() {
            @Override
            public void onResponse(Call<FuelRequest> call, Response<FuelRequest> response) {
                if (response.code() == 204) {
                    view.saveFuelFillingFailure("Không thể lưu thông tin đổ nhiên liệu");
                } else if (response.code() == 200) {
                    view.saveFuelFillingSuccess();
                }else{
                    view.saveFuelFillingFailure("Có lỗi xảy ra trong quá trình lưu thông tin");
                }
            }

            @Override
            public void onFailure(Call<FuelRequest> call, Throwable t) {
                if (t.getMessage().contains("timed out")){
                    view.saveFuelFillingFailure("Vui lòng kiểm tra lại kết nối mạng");
                }else{
                    view.saveFuelFillingFailure("Server đang gặp sự cố. Xin thử lại sau!");
                }
            }
        });
    }
}
