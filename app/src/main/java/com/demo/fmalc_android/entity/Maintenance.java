package com.demo.fmalc_android.entity;

import java.util.Date;

import lombok.Data;

@Data
public class Maintenance    {
    private Integer id;
    private int vehicle;
    private int driver;
    private String maintenanceType;
    private Date plannedMaintainDate;
    private Date actualMaintainDate;
}
