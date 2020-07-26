package com.demo.fmalc_android.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDetail implements Serializable {
    private int status;
    private String vehicleName;
    private String dateOfManufacture;
    private Integer id;
    private int driverLicense;
    private Integer kilometerRunning;
    private String licensePlates;
    private double weight;
    private int vehicleType;
    private double averageFuel;
    private double maximumCapacity;
}
