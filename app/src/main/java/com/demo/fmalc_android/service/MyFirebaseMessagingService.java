package com.demo.fmalc_android.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.demo.fmalc_android.R;
import com.demo.fmalc_android.activity.ConsignmentDetailActivity;
import com.demo.fmalc_android.activity.DriverHomeActivity;
import com.demo.fmalc_android.activity.MaintainAndIssueActivity;
import com.demo.fmalc_android.entity.GlobalVariable;
import com.demo.fmalc_android.enumType.NotificationTypeEnum;
import com.demo.fmalc_android.myworker.MyWorker;
import com.demo.fmalc_android.retrofit.NetworkingUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.demo.fmalc_android.entity.App.FCM_CHANNEL_ID;

public class MyFirebaseMessagingService extends FirebaseMessagingService{

    private static final String TAG = "MyFirebaseMsgService";
    private Integer schedule_id = null;
    private GlobalVariable globalVariable;

    @Override
    public void onNewToken(String token) {
//        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
//        sendRegistrationToServer(token);
    }

    public void checkPayLoad(RemoteMessage remoteMessage, PendingIntent pendingIntent) {
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String content = remoteMessage.getNotification().getBody();
            Notification notification = new NotificationCompat.Builder(this, FCM_CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_logo)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setContentIntent(pendingIntent)
                    .build();

            notification.flags = Notification.FLAG_AUTO_CANCEL;

            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(1002, notification);
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        globalVariable = (GlobalVariable) getApplicationContext();

        // ...
        // TODO(developer): Handle FCM messages here.
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        if (remoteMessage.getNotification().getTitle().contains(NotificationTypeEnum.TASK_SCHEDULE.getNotificationTypeEnum())){
//            intent = new Intent(this, DriverHomeActivity.class);
//            intent.putExtra("fragment", "HomeFragment");
            Intent intent = new Intent().setClass(this, ConsignmentDetailActivity.class);
//            presenter = new ScheduleFromNotificationPresenter();
//            presenter.findScheduleIdByContentOfNotificationAndDriverId(remoteMessage.getNotification().getBody(), globalVariable.getId(),
//                    globalVariable.getToken());
            ScheduleService scheduleService = NetworkingUtils.getScheduleService();
            Call<Integer> call = scheduleService.findScheduleIdByContentOfNotificationAndDriverId(remoteMessage.getNotification().getBody(), globalVariable.getId(),
                    globalVariable.getToken());
            call.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if (response.code() == 204) {

                    } else if (response.code() == 200) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("schedule_id", response.body());
                        String subString[] = remoteMessage.getNotification().getBody().split("#");
                        String subStringId[] = subString[subString.length - 1].split("\\s");
                        Integer consignmentId =  Integer.valueOf(subStringId[0]);
                        bundle.putInt("consignment_id", consignmentId);
                        intent.putExtras(bundle);
                        taskStackBuilder.addNextIntentWithParentStack(intent);
                        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                        checkPayLoad(remoteMessage, pendingIntent);
                    }else{

                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    if (t.getMessage().contains("timed out")){

                    }else if (t.getMessage().contains("Unable to resolve host")) {

                    }else{

                    }
                }
            });
        }else if (remoteMessage.getNotification().getTitle().contains(NotificationTypeEnum.MAINTAIN_SCHEDULE.getNotificationTypeEnum())){
            Intent intent = new Intent(this, MaintainAndIssueActivity.class);
//            intent.putExtra("fragment", "MaintainFragment");
            taskStackBuilder.addNextIntentWithParentStack(intent);
            PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            checkPayLoad(remoteMessage, pendingIntent);
        }else{
            Intent intent = new Intent(this, DriverHomeActivity.class);
            intent.putExtra("fragment", "NotificationFragment");
            taskStackBuilder.addNextIntentWithParentStack(intent);
            PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            checkPayLoad(remoteMessage, pendingIntent);
        }

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }

        }
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "HANDLE NOWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW");
    }

    /**
     * Schedule async work using WorkManager.
     */
    private void scheduleJob() {
        // [START dispatch_job]
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class)
                .build();
        WorkManager.getInstance().beginWith(work).enqueue();
        // [END dispatch_job]
        Log.d(TAG, "SCHEDULE JOBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }
}
