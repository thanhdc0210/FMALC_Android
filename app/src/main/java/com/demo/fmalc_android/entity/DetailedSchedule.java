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
public class DetailedSchedule implements Serializable {

    @SerializedName("scheduleId")
    private Integer scheduleId;

    @SerializedName("licensePlates")
    private String licensePlates;

    @SerializedName("places")
    private List<Place> places;

    @SerializedName("ownerNote")
    private String ownerNote;

    @SerializedName("status")
    private String status;
}
