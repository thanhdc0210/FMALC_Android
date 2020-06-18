package com.demo.fmalc_android.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Place implements Serializable {
    private Double latitude;
    private Double longitude;
    private String address;
    private String name;
    private Timestamp plannedTime;
    private String type; // Giao hàng hay nhận hàng
    private Integer priority; // trình tự giao nhận hàng
    private Timestamp actualTime;
}
