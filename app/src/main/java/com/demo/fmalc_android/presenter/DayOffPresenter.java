package com.demo.fmalc_android.presenter;

import com.demo.fmalc_android.contract.DayOffContract;
import com.demo.fmalc_android.contract.DriverContract;
import com.demo.fmalc_android.contract.FuelContract;
import com.demo.fmalc_android.entity.DayOffDriverRequestDTO;
import com.demo.fmalc_android.entity.DayOffResponseDTO;
import com.demo.fmalc_android.entity.FuelRequest;
import com.demo.fmalc_android.retrofit.NetworkingUtils;
import com.demo.fmalc_android.service.DayOffService;
import com.demo.fmalc_android.service.FuelService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DayOffPresenter implements DayOffContract.Presenter {

    DayOffContract.View view;

    public void setView(DayOffContract.View view) {
        this.view = view;
    }

    DayOffService dayOffService = NetworkingUtils.getDayOffService();


    @Override
    public void checkDayOff(DayOffDriverRequestDTO requestDTO, String auth) {
        Call<DayOffResponseDTO> call = dayOffService.checkDayOff(requestDTO, auth);
        call.enqueue(new Callback<DayOffResponseDTO>() {
            @Override
            public void onResponse(Call<DayOffResponseDTO> call, Response<DayOffResponseDTO> response) {
                if(response.code() == 204){
                    view.checkDayOffForDriverFailure("Thông tin không hợp lệ");
                }else {
                    if(response.code() == 200 ) {
                        view.checkDayOffForDriverSuccess(response.body());
                    } else {
                        view.checkDayOffForDriverFailure("Thông tin không hợp lệ");
                    }
                }
            }

            @Override
            public void onFailure(Call<DayOffResponseDTO> call, Throwable t) {
                if (t.getMessage().contains("timed out")){
                    view.checkDayOffForDriverFailure("Vui lòng kiểm tra lại kết nối mạng");
                }else if (t.getMessage().contains("Unable to resolve host")) {
                    view.checkDayOffForDriverFailure("Mất kết nối mạng");
                }else{
                    view.checkDayOffForDriverFailure("Xin thử lại sau ít phút");
                }
            }
        });
    }

    @Override
    public void updateDayOff(Integer id, DayOffDriverRequestDTO requestDTO, String auth) {
        Call<DayOffResponseDTO> call = dayOffService.updateDayOff(id,requestDTO, auth);
        call.enqueue(new Callback<DayOffResponseDTO>() {
            @Override
            public void onResponse(Call<DayOffResponseDTO> call, Response<DayOffResponseDTO> response) {
                if(response.isSuccessful()){
                   if(response.code()==200){
                       view.updateDayOffForDriverSuccess(response.body());
                   }else{
                       view.updateDayOffForDriverFailure("Thông tin không hợp lệ");
                   }
                }else{
                    view.updateDayOffForDriverFailure("Thông tin không hợp lệ");
                }
//                if(response.code() == 204){
//
//                }else {
//                    if(response.code() == 200 ) {
//                        view.updateDayOffForDriverSuccess(response.body());
//                    } else {
//
//                    }
//                }
            }

            @Override
            public void onFailure(Call<DayOffResponseDTO> call, Throwable t) {
                view.updateDayOffForDriverFailure("Thông tin không hợp lệ");
            }
        });
    }
}
