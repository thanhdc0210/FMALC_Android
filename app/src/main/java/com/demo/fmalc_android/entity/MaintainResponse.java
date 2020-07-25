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
public class MaintainResponse implements Serializable {
    private Integer maintainTypeId;
    private Integer maintainId;
    private String content;
    private String maintainTypeName;
    private String licensePlates;
    private Date maintainDate;
}
