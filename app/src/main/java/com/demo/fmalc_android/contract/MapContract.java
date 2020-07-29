package com.demo.fmalc_android.contract;

import com.demo.fmalc_android.entity.DetailedSchedule;

public interface MapContract {
    interface view{
        void getScheduleSuccess(DetailedSchedule detailedSchedule);
        void getScheduleFailed(String message);
    }
    interface presenter{
        void getSchedule(int id);
    }
}
