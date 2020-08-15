package com.demo.fmalc_android.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DayOffDriverRequestDTO {
    private String startDate;
    private String endDate;
    private String content;
    private Integer driverId;
    private Integer type;

}
