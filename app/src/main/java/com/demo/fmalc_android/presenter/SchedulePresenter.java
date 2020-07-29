package com.demo.fmalc_android.presenter;

import com.demo.fmalc_android.contract.ScheduleContract;
import com.demo.fmalc_android.entity.ListStatusUpdate;
import com.demo.fmalc_android.entity.Schedule;
import com.demo.fmalc_android.retrofit.NetworkingUtils;
import com.demo.fmalc_android.service.ScheduleService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SchedulePresenter implements ScheduleContract.Presenter {

    ScheduleContract.View view;

    public void setView(ScheduleContract.View view) {
        this.view = view;
    }

    ScheduleService scheduleService = NetworkingUtils.getScheduleService();

    @Override
    public void findByConsignmentStatusAndUsername(List<Integer> status, String username) {
        Call<List<Schedule>> call = scheduleService.findByConsignmentStatusAndUsernameForDriver(status, username);
        call.enqueue(new Callback<List<Schedule>>() {
            @Override
            public void onResponse(Call<List<Schedule>> call, Response<List<Schedule>> response) {
                if (response.code() == 204){
                    view.findByConsignmentStatusAndUsernameForFailure("Dữ liệu bạn yêu cầu hiện không có");
                }else if (response.code() == 200){
                    view.findByConsignmentStatusAndUsernameForSuccess(response.body());
                }else{
                    view.findByConsignmentStatusAndUsernameForFailure("Có lỗi xảy ra trong quá trình lấy dữ liệu");
                }
            }

            @Override
            public void onFailure(Call<List<Schedule>> call, Throwable t) {
                if (t.getMessage().contains("timed out")){
                    view.findByConsignmentStatusAndUsernameForFailure("Vui lòng kiểm tra lại kết nối mạng");
                }else {
                    view.findByConsignmentStatusAndUsernameForFailure("Server đang gặp sự cố. Xin thử lại sau!");
                }
            }
        });
    }


}
