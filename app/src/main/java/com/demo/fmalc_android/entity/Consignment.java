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
public class Consignment implements Serializable {

    @SerializedName("consignmentId")
    private Integer consignmentId;

    @SerializedName("ownerName")
    private String ownerName;

    @SerializedName("receivedPlace")
    private ReceivedPlace receivedPlace = new ReceivedPlace();

    @SerializedName("deliveredPlaces")
    private List<DeliveredPlace> deliveredPlaces;

    @SerializedName("licensePlates")
    private String licensePlates; // Biển số xe

    @SerializedName("driverName")
    private String driverName;

    @SerializedName("weight")
    private Double weight; // Khối lượng lô hàng

    @SerializedName("status")
    private String status; // Trạng thái đơn hàng

  }
