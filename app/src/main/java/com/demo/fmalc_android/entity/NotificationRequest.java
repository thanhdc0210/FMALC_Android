package com.demo.fmalc_android.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class NotificationRequest {

    private NotificationData notificationData;

    private String to;
}
