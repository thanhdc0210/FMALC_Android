package com.demo.fmalc_android.retrofit;

import com.demo.fmalc_android.entity.Location;
import com.demo.fmalc_android.service.AccountService;
import com.demo.fmalc_android.service.ConsignmentService;
import com.demo.fmalc_android.service.LocationConsignmentService;

public class NetworkingUtils {

    private static AccountService accountService;
    private static ConsignmentService consignmentService;
    private static LocationConsignmentService locationConsignmentService;

    public static AccountService getAccountApiInstance() {
        if (accountService == null)
            accountService = RetrofitInstance.getInstance().create(AccountService.class);

        return accountService;
    }

    public static ConsignmentService getConsignmentService(){
        if (consignmentService == null)
            consignmentService = RetrofitInstance.getInstance().create(ConsignmentService.class);

        return consignmentService;

    }

    public static LocationConsignmentService getLocationConsignmentService() {
        if(locationConsignmentService==null){
            locationConsignmentService = RetrofitInstance.getInstance().create(LocationConsignmentService.class);
        }
        return locationConsignmentService;
    }
}
