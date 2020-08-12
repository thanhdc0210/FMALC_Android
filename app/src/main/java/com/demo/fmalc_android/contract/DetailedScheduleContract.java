package com.demo.fmalc_android.contract;


import com.demo.fmalc_android.entity.DetailedSchedule;
import com.demo.fmalc_android.entity.ListStatusUpdate;

public interface DetailedScheduleContract {
    interface View{
        void findScheduleByConsignment_IdAndDriver_IdSuccess(DetailedSchedule consignmentDetail);
        void findScheduleByConsignment_IdAndDriver_IdFailure(String message);

        void updateConsDriVehSuccess(ListStatusUpdate statusUpdate);
        void updateConsDriVehFailed(String message);

        void numOfConsignmentSuccess(Integer statusUpdate);
        void numOfConsignmentFailed(String message);
    }

    interface Presenter{
        void findScheduleByConsignment_IdAndDriver_Id(Integer consignmentId, Integer driverId);
        void numOfConsignment(Integer id);
        void updateConsDriVeh(ListStatusUpdate statusUpdate, Integer id);
    }
}
