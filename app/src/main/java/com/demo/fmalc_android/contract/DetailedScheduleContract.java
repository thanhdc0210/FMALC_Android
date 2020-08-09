package com.demo.fmalc_android.contract;


import com.demo.fmalc_android.entity.DetailedSchedule;
import com.demo.fmalc_android.entity.ListStatusUpdate;

public interface DetailedScheduleContract {
    interface View{
        void findByScheduleIdSuccess(DetailedSchedule consignmentDetail);
        void findByScheduleIdFailure(String message);

        void updateConsDriVehSuccess(ListStatusUpdate statusUpdate);
        void updateConsDriVehFailed(String message);

        void numOfConsignmentSuccess(Integer statusUpdate);
        void numOfConsignmentFailed(String message);
    }

    interface Presenter{
        void findByScheduleId(Integer id);
        void numOfConsignment(Integer id);
        void updateConsDriVeh(ListStatusUpdate statusUpdate, Integer id);
    }
}
