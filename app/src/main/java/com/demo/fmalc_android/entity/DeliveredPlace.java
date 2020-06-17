package com.demo.fmalc_android.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveredPlace implements Serializable {

    @SerializedName("plannedDeliveredTime")
    private Timestamp plannedDeliveredTime;

    @SerializedName("deliveredPlaceName")
    private String deliveredPlaceName;

    @SerializedName("deliveredPlaceAddress")
    private String deliveredPlaceAddress;

    @SerializedName("longitude")
    private Double longitude;

    @SerializedName("latitude")
    private Double latitude;

    public DeliveredPlace(Timestamp plannedDeliveredTime, String deliveredPlaceName, String deliveredPlaceAddress) {
        this.plannedDeliveredTime = plannedDeliveredTime;
        this.deliveredPlaceName = deliveredPlaceName;
        this.deliveredPlaceAddress = deliveredPlaceAddress;
    }
}
