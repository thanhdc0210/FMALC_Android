package com.demo.fmalc_android.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlertRequestDTO {

    private String content;
    private int level;
    private int driverId;
    private int vehicleId;

}
