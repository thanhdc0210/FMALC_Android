package com.demo.fmalc_android.contract;

import com.demo.fmalc_android.entity.Notification;
import com.demo.fmalc_android.entity.NotificationData;
import com.demo.fmalc_android.entity.NotificationMobileResponse;

import java.util.List;

public interface NotificationMobileContract {
    interface View{
        void findNotificationByDriverIdSuccess(List<NotificationMobileResponse> notificationMobileResponses);
        void findNotificationByDriverIdFailure(String message);

        void takeDayOffSuccess(boolean status);
        void takeDayOffFailure(String message);
    }

    interface Presenter{
        void findNotificationByDriverId(Integer id, String auth);

        void takeDayOff(Notification notification);
    }
}
