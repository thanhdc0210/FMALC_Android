package com.demo.fmalc_android.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ListStatusUpdate implements Serializable {

    @SerializedName("driver_status")
    private int driver_status;
    @SerializedName("vehicle_status")
    private int vehicle_status;
    @SerializedName("consignment_status")
    private int consignment_status;
}
