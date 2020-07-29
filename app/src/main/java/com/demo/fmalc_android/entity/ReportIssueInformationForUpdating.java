package com.demo.fmalc_android.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportIssueInformationForUpdating {
    @SerializedName("username")
    private String username;

    @SerializedName("reportIssueIdList")
    private List<Integer> reportIssueIdList;
}
