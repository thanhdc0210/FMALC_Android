package com.demo.fmalc_android.entity;

import android.app.Application;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GlobalVariable extends Application {
    private String token;
    private String username;
    private String role;
    private Integer id;

    private List<Place> placeList;
    private int idSchedule;
    private int idDriver;
    private List<Place> places;
    private DetailedSchedule consignmentDetail;
    private int idScheduleNow;
    public static final double latitudePark=10.8418079;
    public static final double longitudePark=106.8091882;
}
