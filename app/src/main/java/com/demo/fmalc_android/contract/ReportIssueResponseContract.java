package com.demo.fmalc_android.contract;

import com.demo.fmalc_android.entity.ReportIssueResponse;

import java.util.List;

public interface ReportIssueResponseContract {
    interface View{
        void getIssueInformationOfAVehicleSuccess(ReportIssueResponse reportIssueResponse);
        void getIssueInformationOfAVehicleFailure(String message);
    }

    interface Presenter{
        void getIssueInformationOfAVehicle(String username, List<Integer> status, String auth);
    }
}
