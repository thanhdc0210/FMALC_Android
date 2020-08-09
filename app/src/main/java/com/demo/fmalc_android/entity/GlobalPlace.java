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
public class GlobalPlace extends Application {
    private List<Place> placeList;
    private int idSchedule;
    private int idDriver;
    private List<Place> places;
}
