package com.demo.fmalc_android.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportIssue {
    private Integer id;
    private String note;
    private String image;

    public ReportIssue(String note, String image) {
        this.note = note;
        this.image = image;
    }

    public ReportIssue(Integer id, String note) {
        this.id = id;
        this.note = note;
    }
}
