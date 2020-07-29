package com.demo.fmalc_android.presenter;

import com.demo.fmalc_android.contract.DetailedScheduleContract;
import com.demo.fmalc_android.entity.DetailedSchedule;
import com.demo.fmalc_android.entity.ListStatusUpdate;
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
                if (response.code() == 204){
                    view.findByScheduleIdFailure("Dữ liệu bạn yêu cầu hiện không có");
                }if (response.code() == 200) {
                    view.findByScheduleIdSuccess(response.body());
                } else {
                    view.findByScheduleIdFailure("Có lỗi xảy ra trong quá trình lấy dữ liệu");
                }
            }

            @Override
            public void onFailure(Call<DetailedSchedule> call, Throwable t) {
                if (t.getMessage().contains("timed out")){
                    view.findByScheduleIdFailure("Vui lòng kiểm tra lại kết nối mạng");
                }else{
                    view.findByScheduleIdFailure("Server đang gặp sự cố. Xin thử lại sau!");
                }
            }
        });
    }
    @Override
    public void updateConsDriVeh(ListStatusUpdate statusUpdate, Integer id) {
        Call<ListStatusUpdate> call = scheduleService.updateStatusAndUsernameForDriver(id,statusUpdate);
        call.enqueue(new Callback<ListStatusUpdate>() {
            @Override
            public void onResponse(Call<ListStatusUpdate> call, Response<ListStatusUpdate> response) {
                if(!response.isSuccessful()){
                    view.updateConsDriVehFailed("Bắt đầu thất bại");
                }else{
                    ListStatusUpdate listStatusUpdate = response.body();
                    view.updateConsDriVehSuccess(listStatusUpdate);
                }
            }

            @Override
            public void onFailure(Call<ListStatusUpdate> call, Throwable t) {
                view.updateConsDriVehFailed("Có lỗi xày ra");
            }
        });
    }
}
