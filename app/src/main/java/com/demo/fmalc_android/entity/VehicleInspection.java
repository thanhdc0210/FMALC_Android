package com.demo.fmalc_android.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor

public class VehicleInspection {
    List<String> vehicleLicensePlates;
    List<Inspection> inspections;
}
