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

        void checkConsignmentInDaySuccess(Integer numberConsignment);
        void checkConsignmentInDayFailed(String message);

        void getScheduleRunningForDriverSuccess(DetailedSchedule detailedSchedule);
        void getScheduleRunningForDriverFailed(String message);
    }

    interface Presenter{
        void findByScheduleId(Integer id);
        void numOfConsignment(Integer id);
        void updateConsDriVeh(ListStatusUpdate statusUpdate, Integer id);
        void checkConsignmentInDay(Integer id);
        void getScheduleRunningForDriver(Integer id);
    }
}
