package com.demo.fmalc_android.retrofit;

import com.demo.fmalc_android.entity.FuelType;
import com.demo.fmalc_android.service.AccountService;
import com.demo.fmalc_android.service.DriverService;
import com.demo.fmalc_android.service.FuelTypeService;
import com.demo.fmalc_android.service.MaintenanceService;
import com.demo.fmalc_android.service.ReportIssueService;
import com.demo.fmalc_android.service.ScheduleService;
import com.demo.fmalc_android.service.SearchingService;
import com.demo.fmalc_android.service.VehicleService;

public class NetworkingUtils {

    private static AccountService accountService;
    private static ScheduleService scheduleService;
    private static VehicleService vehicleService;
    private static ReportIssueService reportIssueService;
    private static FuelTypeService fuelTypeService;
    private static DriverService driverService;
    private static MaintenanceService maintenanceService;
    private static SearchingService searchingService;

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

    public static FuelTypeService getFuelTypeService(){
        if (fuelTypeService == null)
            fuelTypeService = RetrofitInstance.getInstance().create(FuelTypeService.class);
        return fuelTypeService;
    }

    public static DriverService getDriverService(){
        if (driverService == null)
            driverService = RetrofitInstance.getInstance().create(DriverService.class);
        return driverService;
    }
    public static MaintenanceService getMaintenanceService(){
        if (maintenanceService == null)
            maintenanceService = RetrofitInstance.getInstance().create(MaintenanceService.class);
        return maintenanceService;
    }

    public static SearchingService getSearchingService(){
        if (searchingService == null)
            searchingService = RetrofitInstance.getInstance().create(SearchingService.class);
        return searchingService;
    }
}
