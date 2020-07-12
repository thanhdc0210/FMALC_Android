package com.demo.fmalc_android.contract;
import com.demo.fmalc_android.entity.ReportIssueRequest;

public interface ReportIssueContract {

    interface View{
        void createReportIssueForSuccess(ReportIssueRequest reportIssueRequest);
        void createReportIssueForFailure(String message);
    }

    interface Presenter {
        void createReportIssue(ReportIssueRequest reportIssueRequest);
    }
}
