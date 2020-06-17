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
public class DetailedConsignment implements Serializable {

    @SerializedName("consignmentId")
    private Integer consignmentId;

    @SerializedName("licensePlates")
    private String licensePlates;

    @SerializedName("deliveredPlaces")
    private List<DeliveredPlace> deliveredPlaces;

    @SerializedName("receivedPlaces")
    private List<ReceivedPlace> receivedPlaces;

    @SerializedName("ownerNote")
    private String ownerNote;
}
