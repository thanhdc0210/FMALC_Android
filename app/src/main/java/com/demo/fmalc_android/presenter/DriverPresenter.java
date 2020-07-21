package com.demo.fmalc_android.presenter;

import com.demo.fmalc_android.contract.DriverContract;
import com.demo.fmalc_android.entity.DriverInformation;
import com.demo.fmalc_android.retrofit.NetworkingUtils;
import com.demo.fmalc_android.service.DriverService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverPresenter implements DriverContract.Presenter {

    DriverContract.View view;

    public void setView(DriverContract.View view) {
        this.view = view;
    }

    DriverService driverService = NetworkingUtils.getDriverService();




    @Override
    public void getDriverInformation(Integer id) {
        Call<DriverInformation> call = driverService.getDriverInformationById(id);
        call.enqueue(new Callback<DriverInformation>() {
            @Override
            public void onResponse(Call<DriverInformation> call, Response<DriverInformation> response) {
                if(!response.isSuccessful()){
                    view.getDriverInformationFailure("Không thể lấy thông tin tài xế");
                }else {
                    if(response.code() == 200 ) {
                        DriverInformation driverInformation = response.body();
                        view.getDriverInformationSuccessful(driverInformation);
                    } else {
                        view.getDriverInformationFailure("Không thể lấy thông tin tài xế");
                    }
                }
            }

            @Override
            public void onFailure(Call<DriverInformation> call, Throwable t) {
                view.getDriverInformationFailure("Không thể lấy thông tin tài xế "+ t.getMessage());
            }
        });
    }
}
