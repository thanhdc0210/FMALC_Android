package com.demo.fmalc_android.presenter;

import com.demo.fmalc_android.contract.NotificationMobileContract;
import com.demo.fmalc_android.entity.Notification;
import com.demo.fmalc_android.entity.NotificationMobileResponse;
import com.demo.fmalc_android.retrofit.NetworkingUtils;
import com.demo.fmalc_android.service.NotificationService;

import java.util.List;

import okhttp3.ResponseBody;
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
    public void findNotificationByUsername(String username, String auth) {
        Call<List<NotificationMobileResponse>> call =notificationService.findNotificationByUsername(username, auth);
        call.enqueue(new Callback<List<NotificationMobileResponse>>() {
            @Override
            public void onResponse(Call<List<NotificationMobileResponse>> call, Response<List<NotificationMobileResponse>> response) {
                if (response.code() == 204) {
                    view.findNotificationByUsernameFailure("Bạn mới đăng nhập hệ thống hiện chưa có thông báo");
                } else if (response.code() == 200) {
                    view.findNotificationByUsernameSuccess(response.body());
                } else {
                    view.findNotificationByUsernameFailure("Không thể tìm được dữ liệu ");
                }
            }

            @Override
            public void onFailure(Call<List<NotificationMobileResponse>> call, Throwable t) {
                if (t.getMessage().contains("timed out")) {
                    view.findNotificationByUsernameFailure("Vui lòng kiểm tra lại kết nối mạng");
                } else {
                    view.findNotificationByUsernameFailure("Server đang gặp sự cố. Xin thử lại sau!");
                }
            }
        });
    }

    @Override
    public void updateStatus(Integer id, String username, String auth) {
        Call<Boolean> call = notificationService.updateStatus(id, username, auth);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.code() == 204) {
                    view.updateStatusFailure("");
                } else if (response.isSuccessful()) {
                    view.updateStatusSuccess();
                } else {
                    view.updateStatusFailure("");
                }
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                if (t.getMessage().contains("timed out")) {
                    view.updateStatusFailure("");
                } else {
                    view.updateStatusFailure("");

                }
            }
        });
    }

    public void takeDayOff(Notification notification) {
        Call<ResponseBody> call = notificationService.takeDayOff(notification);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 204) {
                    view.takeDayOffFailure("Không thể gửi");
                } else if (response.code() == 200) {
                    view.takeDayOffSuccess(true);
                } else {
                    view.takeDayOffFailure("Có lỗi xảy ra trong quá trình gửi thông tin");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (t.getMessage().contains("timed out")) {
                    view.takeDayOffFailure("Vui lòng kiểm tra lại kết nối mạng");
                } else {
                    view.takeDayOffFailure("Server đang gặp sự cố. Xin thử lại sau!");

                }
            }
        });
    }
}
