package com.demo.fmalc_android.presenter;

import com.demo.fmalc_android.contract.NotificationMobileContract;
import com.demo.fmalc_android.entity.NotificationMobileResponse;
import com.demo.fmalc_android.retrofit.NetworkingUtils;
import com.demo.fmalc_android.service.NotificationService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationMobilePresenter implements NotificationMobileContract.Presenter {

    NotificationMobileContract.View view;

    public void setView(NotificationMobileContract.View view) {
        this.view = view;
    }

    NotificationService notificationService = NetworkingUtils.getNotificationService();

    @Override
    public void findNotificationByDriverId(Integer id) {
        Call<List<NotificationMobileResponse>> call =notificationService.findNotificationByDriverId(id);
        call.enqueue(new Callback<List<NotificationMobileResponse>>() {
            @Override
            public void onResponse(Call<List<NotificationMobileResponse>> call, Response<List<NotificationMobileResponse>> response) {
                if (response.code() == 204){
                    view.findNotificationByDriverIdFailure("Bạn mới đăng nhập hệ thống hiện chưa có thông báo");
                }else if (response.code() == 200){
                    view.findNotificationByDriverIdSuccess(response.body());
                }else {
                    view.findNotificationByDriverIdFailure("Không thể tìm được dữ liệu ");
                }
            }

            @Override
            public void onFailure(Call<List<NotificationMobileResponse>> call, Throwable t) {
                if (t.getMessage().contains("timed out")){
                    view.findNotificationByDriverIdFailure("Vui lòng kiểm tra lại kết nối mạng");
                }else{
                    view.findNotificationByDriverIdFailure("Server đang gặp sự cố. Xin thử lại sau!");
                }
            }
        });
    }
}
