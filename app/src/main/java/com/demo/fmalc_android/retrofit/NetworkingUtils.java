package com.demo.fmalc_android.retrofit;

import com.demo.fmalc_android.service.AccountService;
import com.demo.fmalc_android.service.ReportIssueService;
import com.demo.fmalc_android.service.ScheduleService;
import com.demo.fmalc_android.service.VehicleService;

public class NetworkingUtils {

    private static AccountService accountService;
    private static ScheduleService scheduleService;
    private static VehicleService vehicleService;
    private static ReportIssueService reportIssueService;


    public static AccountService getAccountApiInstance() {
        if (accountService == null)
            accountService = RetrofitInstance.getInstance().create(AccountService.class);

        return accountService;
    }

    public static ScheduleService getScheduleService(){
        if (scheduleService == null)
            scheduleService = RetrofitInstance.getInstance().create(ScheduleService.class);

        return scheduleService;

    }

    public static VehicleService getVehicleService(){
        if (vehicleService == null)
            vehicleService = RetrofitInstance.getInstance().create(VehicleService.class);

        return vehicleService;

    }

    public static ReportIssueService getReportIssueService(){
        if (reportIssueService == null)
            reportIssueService = RetrofitInstance.getInstance().create(ReportIssueService.class);

        return reportIssueService;
    }
}
