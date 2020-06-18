package com.demo.fmalc_android.entity;

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
public class Consignment implements Serializable {
    private Integer consignmentId;
    private String ownerName;
    private List<Place> places;
    private String licensePlates; // Biển số xe
    private String driverName;
    private Double weight; // Khối lượng lô hàng
    private String status;
}
