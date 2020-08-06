package com.demo.fmalc_android.contract;
import com.demo.fmalc_android.entity.ReportIssueRequest;

public interface ReportIssueContract {

    interface View{
        void createReportIssueForDeliveryForSuccess(ReportIssueRequest reportIssueRequest);
        void createReportIssueForDeliveryForFailure(String message);
    }

    interface Presenter {
        void createReportIssueForDelivery(ReportIssueRequest reportIssueRequest, String auth);
    }
}
