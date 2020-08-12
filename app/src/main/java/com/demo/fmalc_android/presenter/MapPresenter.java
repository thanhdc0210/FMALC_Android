package com.demo.fmalc_android.presenter;

import com.demo.fmalc_android.contract.MapContract;
import com.demo.fmalc_android.entity.DetailedSchedule;
import com.demo.fmalc_android.retrofit.NetworkingUtils;
import com.demo.fmalc_android.service.ScheduleService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapPresenter implements MapContract.presenter {
    private MapContract.view view;

    public void setView(MapContract.view view) {
        this.view = view;
    }

    ScheduleService scheduleService = NetworkingUtils.getScheduleService();
    @Override
    public void getSchedule(int consignmentId) {
        Call<DetailedSchedule>  call = scheduleService.findScheduleById(consignmentId);
        call.enqueue(new Callback<DetailedSchedule>() {
            @Override
            public void onResponse(Call<DetailedSchedule> call, Response<DetailedSchedule> response) {
                if(response.isSuccessful()){
                    view.getScheduleSuccess(response.body());
                }else{

                    view.getScheduleFailed("Vui lòng kiểm tra lại");
                }
            }

            @Override
            public void onFailure(Call<DetailedSchedule> call, Throwable t) {
                view.getScheduleFailed("Có lỗi xảy ra!");
            }
        });
    }
}
