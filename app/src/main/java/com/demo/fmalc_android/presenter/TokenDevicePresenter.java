package com.demo.fmalc_android.presenter;

import com.demo.fmalc_android.contract.TokenDeviceContract;
import com.demo.fmalc_android.entity.DriverInformation;
import com.demo.fmalc_android.retrofit.NetworkingUtils;
import com.demo.fmalc_android.service.DriverService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TokenDevicePresenter implements TokenDeviceContract.Presenter {

    TokenDeviceContract.View view;

    public void setView(TokenDeviceContract.View view) {
        this.view = view;
    }

    DriverService driverService = NetworkingUtils.getDriverService();

    @Override
    public void updateTokenDevice(Integer id, String tokenDevice) {
        Call<String> call = driverService.updateTokenDevice(id, tokenDevice);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() == 204){
                    view.updateTokenDeviceFailure("Không thể lưu token của thiết bị");
                }else if (response.code() == 200){
                    view.updateTokenDeviceSuccess();
                }else{
                    view.updateTokenDeviceFailure("Có lỗi xảy ra trong quá trình lưu dữ liệu");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if (t.getMessage().contains("timed out")){
                    view.updateTokenDeviceFailure("Vui lòng kiểm tra lại kết nối mạng");
                }else{
                    view.updateTokenDeviceFailure("Server đang gặp sự cố. Xin thử lại sau!");
                }
            }
        });
    }
}
