package com.demo.fmalc_android.presenter;

import com.demo.fmalc_android.contract.DetailedScheduleContract;
import com.demo.fmalc_android.entity.DetailedSchedule;
import com.demo.fmalc_android.retrofit.NetworkingUtils;
import com.demo.fmalc_android.service.ScheduleService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailedSchedulePresenter implements DetailedScheduleContract.Presenter {

    DetailedScheduleContract.View view;

    public void setView(DetailedScheduleContract.View view) {
        this.view = view;
    }

    ScheduleService scheduleService = NetworkingUtils.getScheduleService();


    public void findByScheduleId(Integer id) {
        Call<DetailedSchedule> call = scheduleService.findByScheduleId(id);
        call.enqueue(new Callback<DetailedSchedule>() {
            @Override
            public void onResponse(Call<DetailedSchedule> call, Response<DetailedSchedule> response) {
                if (!response.isSuccessful()) {
                    view.findByScheduleIdFailure("Không tìm thấy lịch trình");
                } else {
                    DetailedSchedule consignmentDetail = response.body();
                    view.findByScheduleIdSuccess(consignmentDetail);
                }
            }

            @Override
            public void onFailure(Call<DetailedSchedule> call, Throwable t) {
                view.findByScheduleIdFailure("Có lỗi xảy ra trong quá trình lấy thông tin");

            }
        });
    }
}
