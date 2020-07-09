package com.demo.fmalc_android.contract;


import com.demo.fmalc_android.entity.DetailedSchedule;

public interface DetailedScheduleContract {
    interface View{
        void findByScheduleIdSuccess(DetailedSchedule consignmentDetail);
        void findByScheduleIdFailure(String message);
    }

    interface Presenter{
        void findByScheduleId(Integer id);
    }
}
