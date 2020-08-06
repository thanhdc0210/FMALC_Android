package com.demo.fmalc_android.contract;

import com.demo.fmalc_android.entity.ReportIssueInformationForUpdating;

public interface ReportIssueForUpdatingContract {
    interface View{
        void updateReportIssueForSuccess();
        void updateReportIssueForFailure(String message);
    }

    interface Presenter{
        void updateReportIssue(ReportIssueInformationForUpdating reportIssueInformationForUpdating, String auth);
    }
}
