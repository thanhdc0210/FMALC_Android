package com.demo.fmalc_android.entity;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMobileResponse {
    private Timestamp time;
    private String content;
    private boolean status;
    private String username;
    private Integer notificationId;
    private Integer type;
    private Integer scheduleId;
}
