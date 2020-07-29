package com.demo.fmalc_android.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportIssueRequest {
    @SerializedName("username")
    private String username;
    @SerializedName("vehicleLicensePlates")
    private String vehicleLicensePlates;
    @SerializedName("reportIssueContentRequests")
    private Map<Integer, ReportIssueContentRequest> reportIssueContentRequests;
    @SerializedName("type")
    private Integer type;
}
