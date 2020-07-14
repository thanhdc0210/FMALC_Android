package com.demo.fmalc_android.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Issue {
    private Integer id;
    private String inspectionName;
    private String image;
    private String note;

    public Issue(Integer id) {
        this.id = id;
    }
}
