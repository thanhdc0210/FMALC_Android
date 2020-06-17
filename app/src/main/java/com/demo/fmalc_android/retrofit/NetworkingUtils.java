package com.demo.fmalc_android.retrofit;

import com.demo.fmalc_android.service.AccountService;
import com.demo.fmalc_android.service.ConsignmentService;

public class NetworkingUtils {

    private static AccountService accountService;
    private static ConsignmentService consignmentService;

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
}
