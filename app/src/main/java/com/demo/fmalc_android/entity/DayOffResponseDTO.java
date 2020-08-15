package com.demo.fmalc_android.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DayOffResponseDTO {

    private String startDate;
    private String endDate;
    private int dayOffId;

}
