package com.demo.fmalc_android.entity;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FuelRequest {
    @SerializedName("fuelTypeId")
    private Integer fuelTypeId;
    @SerializedName("kmOld")
    private Integer kmOld;
    @SerializedName("unitPriceAtFillingTime")
    private Double unitPriceAtFillingTime;
    @SerializedName("volume")
    private Double volume;
    @SerializedName("vehicleLicensePlates")
    private String vehicleLicensePlates;

    @Override
    public String toString() {
        return "FuelRequest{" +
                "fuelTypeId=" + fuelTypeId +
                ", kmOld=" + kmOld +
                ", unitPriceAtFillingTime=" + unitPriceAtFillingTime +
                ", volume=" + volume +
                ", vehicleLicensePlates='" + vehicleLicensePlates + '\'' +
                '}';
    }
}
