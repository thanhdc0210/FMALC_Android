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
                if(response.code() == 204){
                    view.getDriverInformationFailure("Dữ liệu bạn yêu cầu hiện không có");
                }else {
                    if(response.code() == 200 ) {
                        view.getDriverInformationSuccessful(response.body());
                    } else {
                        view.getDriverInformationFailure("Không thể lấy thông tin tài xế");
                    }
                }
            }

            @Override
            public void onFailure(Call<DriverInformation> call, Throwable t) {
                if (t.getMessage().contains("timed out")){
                    view.getDriverInformationFailure("Vui lòng kiểm tra lại kết nối mạng");
                }else {
                    view.getDriverInformationFailure("Server đang gặp sự cố. Xin thử lại sau!");
                }
            }
        });
    }
}
