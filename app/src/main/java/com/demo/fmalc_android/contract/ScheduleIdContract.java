package com.demo.fmalc_android.contract;

public interface ScheduleIdContract {
    interface View{
        void findScheduleIdByConsignmentIdAndDriverIdSuccess(Integer scheduleId);
        void findScheduleIdByConsignmentIdAndDriverIdFailure(String message);
    }

    interface Presenter{
        void findScheduleIdByConsignmentIdAndDriverId(Integer consignmentId, Integer driverId, String auth);
    }
}
