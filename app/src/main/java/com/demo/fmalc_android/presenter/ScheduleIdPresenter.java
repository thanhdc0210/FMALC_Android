package com.demo.fmalc_android.presenter;

import com.demo.fmalc_android.contract.ScheduleContract;
import com.demo.fmalc_android.contract.ScheduleIdContract;
import com.demo.fmalc_android.retrofit.NetworkingUtils;
import com.demo.fmalc_android.service.ScheduleService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleIdPresenter implements ScheduleIdContract.Presenter {

    ScheduleIdContract.View view;

    public void setView(ScheduleIdContract.View view) {
        this.view = view;
    }

    ScheduleService scheduleService = NetworkingUtils.getScheduleService();

    @Override
    public void findScheduleIdByConsignmentIdAndDriverId(Integer consignmentId, Integer driverId, String auth) {
        Call<Integer> call = scheduleService.findScheduleIdByConsignmentIdAndDriverId(consignmentId, driverId, auth);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.code() == 204){
                    view.findScheduleIdByConsignmentIdAndDriverIdFailure(""+response.code());
                }else if (response.code() == 200){
                    view.findScheduleIdByConsignmentIdAndDriverIdSuccess(response.body());
                }else{
                    view.findScheduleIdByConsignmentIdAndDriverIdFailure(""+response.code());
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                if (t.getMessage().contains("timed out")){
                    view.findScheduleIdByConsignmentIdAndDriverIdFailure("Vui lòng kiểm tra lại kết nối mạng");
                }else {
                    view.findScheduleIdByConsignmentIdAndDriverIdFailure("Server đang gặp sự cố. Xin thử lại sau!");
                }
            }
        });
    }
}
