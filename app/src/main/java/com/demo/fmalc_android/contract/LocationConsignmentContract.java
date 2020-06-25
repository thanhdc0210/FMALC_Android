package com.demo.fmalc_android.contract;

import com.demo.fmalc_android.entity.Consignment;
import com.demo.fmalc_android.entity.Location;

import java.util.List;

import okhttp3.ResponseBody;

public interface LocationConsignmentContract {
    interface View{
        void trackingLocationSuccess(ResponseBody responseBody);
        void trackingLocationSFailed(String message);
    }

    interface Presenter{
        void trackingLocation(Location location);
    }
}
