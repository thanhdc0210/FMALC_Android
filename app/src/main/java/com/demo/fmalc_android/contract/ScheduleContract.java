package com.demo.fmalc_android.contract;

import com.demo.fmalc_android.entity.ListStatusUpdate;
import com.demo.fmalc_android.entity.Schedule;

import java.util.List;

public interface ScheduleContract {
    interface View{
        void findByConsignmentStatusAndUsernameForSuccess(List<Schedule> scheduleList);
        void findByConsignmentStatusAndUsernameForFailure(String message);


    }

    interface Presenter{
        void findByConsignmentStatusAndUsername(List<Integer> status, String username);

    }
}
