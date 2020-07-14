package com.demo.fmalc_android.contract;
import com.demo.fmalc_android.entity.ReportIssueRequest;

public interface ReportIssueContract {

    interface View{
        void createReportIssueBeforeDeliveryForSuccess(ReportIssueRequest reportIssueRequest);
        void createReportIssueBeforeDeliveryForFailure(String message);
    }

    interface Presenter {
        void createReportIssueBeforeDelivery(ReportIssueRequest reportIssueRequest);
    }
}
