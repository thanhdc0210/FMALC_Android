package com.demo.fmalc_android.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public  class DriverInformation  implements Serializable {
    private Integer id;
    private Integer accountId;
    private Integer status;
    private String driverStatus;
    private Integer driverLicense;
    private String driverLicenseStr;
    private String identityNo;
    private String name;
    private String phoneNumber;
    private Date licenseExpires;
    private String no;
    private Date dateOfBirth;
    private Float workingHour;
    private String image;
    private Boolean isActive;
}
