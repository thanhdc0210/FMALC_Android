package com.demo.fmalc_android.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportIssueResponse {
    @SerializedName("vehicleLicensePlates")
    private String vehicleLicensePlates;
    @SerializedName("reportIssueContentResponses")
    private List<ReportIssueContentResponse> reportIssueContentResponses;
}
