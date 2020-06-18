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
@AllArgsConstructor
@NoArgsConstructor
public class Place implements Serializable {
    @SerializedName("latitude")
    private Double latitude;
    @SerializedName("longitude")
    private Double longitude;
    @SerializedName("address")
    private String address;
    @SerializedName("name")
    private String name;
    @SerializedName("plannedTime")
    private Timestamp plannedTime;
    @SerializedName("type")
    private String type; // Giao hàng hay nhận hàng
    @SerializedName("priority")
    private Integer priority; // trình tự giao nhận hàng
    @SerializedName("actualTime")
    private Timestamp actualTime;

        public Place(String address, String name, Timestamp plannedTime) {
            this.address = address;
            this.name = name;
            this.plannedTime = plannedTime;
        }
}
