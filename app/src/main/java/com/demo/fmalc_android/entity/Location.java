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
public class Location implements Serializable {

//    @SerializedName("id")
//    private int id;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("address")
    private String address;
    @SerializedName("time")
    private String time;
    @SerializedName("schedule")
    private int schedule;
}
