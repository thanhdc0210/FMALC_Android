package com.demo.fmalc_android.entity;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportIssueContentRequest {
    @SerializedName("inspectionId")
    private Integer inspectionId;
    @SerializedName("content")
    private String content;
    @SerializedName("imageUrl")
    private String imageUrl;
}
