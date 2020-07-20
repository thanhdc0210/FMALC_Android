package com.demo.fmalc_android.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FuelType implements Serializable {

    @SerializedName("id")
    private Integer id;

    @SerializedName("currentPrice")
    private Double price;

    @SerializedName("type")
    private String type;



}
