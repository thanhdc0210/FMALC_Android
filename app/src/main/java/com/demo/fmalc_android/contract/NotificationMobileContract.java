package com.demo.fmalc_android.contract;

import com.demo.fmalc_android.entity.NotificationMobileResponse;

import java.util.List;

public interface NotificationMobileContract {
    interface View{
        void findNotificationByDriverIdSuccess(List<NotificationMobileResponse> notificationMobileResponses);
        void findNotificationByDriverIdFailure(String message);

        void updateStatusSuccess();
        void updateStatusFailure(String message);
    }

    interface Presenter{
        void findNotificationByDriverId(String username, String auth);
        void updateStatus(Integer id, String auth);
    }
}
