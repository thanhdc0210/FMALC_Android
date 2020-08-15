package com.demo.fmalc_android.contract;

import com.demo.fmalc_android.entity.DayOffDriverRequestDTO;
import com.demo.fmalc_android.entity.DayOffResponseDTO;
import com.demo.fmalc_android.entity.FuelRequest;

public interface DayOffContract {
    interface View{
        void checkDayOffForDriverSuccess(DayOffResponseDTO responseDTO);
        void checkDayOffForDriverFailure(String message);

        void updateDayOffForDriverSuccess(DayOffResponseDTO responseDTO);
        void updateDayOffForDriverFailure(String message);


    }

    interface Presenter{
        void checkDayOff(DayOffDriverRequestDTO requestDTO, String auth);
        void updateDayOff(Integer id, DayOffDriverRequestDTO requestDTO, String auth);
    }
}
