package com.demo.fmalc_android.presenter;

import com.demo.fmalc_android.contract.AlertContract;
import com.demo.fmalc_android.contract.VehicleContract;
import com.demo.fmalc_android.entity.AlertRequestDTO;
import com.demo.fmalc_android.entity.Notification;
import com.demo.fmalc_android.entity.VehicleDetail;
import com.demo.fmalc_android.retrofit.NetworkingUtils;
import com.demo.fmalc_android.service.NotificationService;
import com.demo.fmalc_android.service.VehicleService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlertPresenter implements AlertContract.Presenter {

    AlertContract.View view;

//    public AlertPresenter(AlertContract.View view) {
//        this.view = view;
//    }



    public void setView(AlertContract.View view) {
        this.view = view;
    }

    VehicleService vehicleService = NetworkingUtils.getVehicleService();
    NotificationService notificationService = NetworkingUtils.getNotificationService();
    @Override
    public void sendRequestWhileRunning(AlertRequestDTO requestDTO, String auth) {
        Call<AlertRequestDTO> call = vehicleService.sendRequestWhileRunning(requestDTO,auth);
        call.enqueue(new Callback<AlertRequestDTO>() {
            @Override
            public void onResponse(Call<AlertRequestDTO> call, Response<AlertRequestDTO> response) {
                if (response.code() == 204){
                    view.sendRequestWhileRunningFailure("Bạn đang không chạy xe");
                }else if (response.code() == 200){
                    view.sendRequestWhileRunningSuccess(response.body());
                }else{
                    view.sendRequestWhileRunningFailure("Có lỗi xảy ra trong quá trình gửi yêu cầu");
                }
            }

            @Override
            public void onFailure(Call<AlertRequestDTO> call, Throwable t) {
                if (t.getMessage().contains("timed out")){
                    view.sendRequestWhileRunningFailure("Vui lòng kiểm tra lại kết nối mạng");
                }else if (t.getMessage().contains("Unable to resolve host")) {
                    view.sendRequestWhileRunningFailure("Mất kết nối mạng");
                }else{
                    view.sendRequestWhileRunningFailure("Xin thử lại sau ít phút");
                }
            }
        });
    }

    @Override
    public void sendNotification(Notification notification) {
        Call<ResponseBody> call = notificationService.takeDayOff(notification);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    view.sendNotificationSuccess("Cảnh báo của bạn đã được gửi đến quản lý");
                }else{
                    view.sendNotificationFailed("Cảnh báo của bạn chưa được gửi đến quản lý");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                view.sendNotificationFailed("Có lỗi xảy xa trong quá trình thực hiện");
            }
        });
    }

    @Override
    public void getDetailVehicle(int id) {
        Call<VehicleDetail> call =vehicleService.getDetailVehicle(id);
        call.enqueue(new Callback<VehicleDetail>() {
            @Override
            public void onResponse(Call<VehicleDetail> call, Response<VehicleDetail> response) {
                if(response.isSuccessful()){
                    view.getDetailVehicleSuccess(response.body());
                }else{
                    view.getDetailVehicleFailed("");
                }
            }

            @Override
            public void onFailure(Call<VehicleDetail> call, Throwable t) {
                view.getDetailVehicleFailed("Có lỗi trong quá trình thực hiện");
            }
        });
    }
}
