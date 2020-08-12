package com.demo.fmalc_android.entity;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Schedule implements Serializable {

    @SerializedName("scheduleId")
    private Integer scheduleId;

    @SerializedName("ownerName")
    private String ownerName;

    @SerializedName("places")
    private List<Place> places;

    @SerializedName("licensePlates")
    private String licensePlates; // Biển số xe

    @SerializedName("driverName")
    private String driverName;

    @SerializedName("weight")
    private Double weight; // Khối lượng lô hàng

    @SerializedName("status")
    private String status; // Trạng thái đơn hàng

    @SerializedName("isInheritance")
    private Boolean isInheritance;

    @SerializedName("consignmentId")
    private Integer consignmentId;
  }
