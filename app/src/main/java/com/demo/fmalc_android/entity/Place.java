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
public class Place implements Serializable {

    @SerializedName("planned_received_time")
    private Timestamp plannedReceivedTime;

    @SerializedName("received_place_name")
    private String receivedPlaceName;

    @SerializedName("received_place_address")
    private String receivedPlaceAddress;

    @SerializedName("longitude")
    private Double longitude;

    @SerializedName("latitude")
    private Double latitude;

    public Place(Timestamp plannedReceivedTime, String receivedPlaceName, String receivedPlaceAddress) {
        this.plannedReceivedTime = plannedReceivedTime;
        this.receivedPlaceName = receivedPlaceName;
        this.receivedPlaceAddress = receivedPlaceAddress;
    }
}
