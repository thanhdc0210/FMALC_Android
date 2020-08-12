package com.demo.fmalc_android.contract;

import com.demo.fmalc_android.entity.Notification;
import com.demo.fmalc_android.entity.NotificationData;
import com.demo.fmalc_android.entity.NotificationMobileResponse;

import java.util.List;

public interface NotificationMobileContract {
    interface View{
        void findNotificationByUsernameSuccess(List<NotificationMobileResponse> notificationMobileResponses);
        void findNotificationByUsernameFailure(String message);

        void updateStatusSuccess();
        void updateStatusFailure(String message);

        void takeDayOffSuccess(boolean status);
        void takeDayOffFailure(String message);
    }

    interface Presenter{
        void findNotificationByUsername(String username, String auth);
        
        void updateStatus(Integer notificationId, String username, String auth);

        void takeDayOff(Notification notification);
    }
}
