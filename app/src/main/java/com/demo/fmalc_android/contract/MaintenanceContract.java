package com.demo.fmalc_android.contract;

import com.demo.fmalc_android.entity.MaintainResponse;

import java.net.URISyntaxException;
import java.util.List;

import okhttp3.MultipartBody;

public interface MaintenanceContract {
    interface View{
        void getMaintenanceListSuccessful(List<MaintainResponse> maintainResponseList);
        void getMaintenanceListFailure(String message);

        void updateMaintenanceSuccessful(boolean isSuccessful);
        void updateMaintenanceFailure(String message);

    }

    interface Presenter{
        void getMaintenanceList(Integer driverId, String auth);
        void updateMaintenance(Integer driverId, Integer kmOld, MultipartBody.Part file, String auth) throws URISyntaxException;
    }
}
