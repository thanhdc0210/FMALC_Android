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


    public void findScheduleByConsignment_IdAndDriver_Id(Integer consignmentId, Integer driverId) {
        Call<DetailedSchedule> call = scheduleService.findScheduleByConsignment_IdAndDriver_Id(consignmentId, driverId);
        call.enqueue(new Callback<DetailedSchedule>() {
            @Override
            public void onResponse(Call<DetailedSchedule> call, Response<DetailedSchedule> response) {
                if (response.code() == 204){
                    view.findScheduleByConsignment_IdAndDriver_IdFailure("Dữ liệu bạn yêu cầu hiện không có");
                }if (response.code() == 200) {
                    view.findScheduleByConsignment_IdAndDriver_IdSuccess(response.body());
                } else {
                    view.findScheduleByConsignment_IdAndDriver_IdFailure("Có lỗi xảy ra trong quá trình lấy dữ liệu");
                }
            }

            @Override
            public void onFailure(Call<DetailedSchedule> call, Throwable t) {
                if (t.getMessage().contains("timed out")){
                    view.findScheduleByConsignment_IdAndDriver_IdFailure("Vui lòng kiểm tra lại kết nối mạng");
                }else{
                    view.findScheduleByConsignment_IdAndDriver_IdFailure("Server đang gặp sự cố. Xin thử lại sau!");
                }
            }
        });
    }

    @Override
    public void numOfConsignment(Integer id) {
        Call<Integer> call = scheduleService.countConsignment(id);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(!response.isSuccessful()){

                }else{
                    view.numOfConsignmentSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

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
