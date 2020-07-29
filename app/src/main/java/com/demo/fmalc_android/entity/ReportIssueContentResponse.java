package com.demo.fmalc_android.entity;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportIssueContentResponse {
    @SerializedName("inspectionId")
    private Integer inspectionId;
    @SerializedName("inspectionName")
    private String inspectionName;
    @SerializedName("content")
    private String content;
    @SerializedName("image")
    private String image;
    @SerializedName("reportIssueId")
    private Integer reportIssueId;
}
