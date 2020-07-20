package com.demo.fmalc_android.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FuelTypeResponse {
    @SerializedName("vehicleLicensePlate")
    private String vehicleLicensePlate;

    @SerializedName("fuelTypeList")
    private List<FuelType> fuelTypeList;
}
