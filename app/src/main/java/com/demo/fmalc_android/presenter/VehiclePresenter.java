package com.demo.fmalc_android.presenter;

import com.demo.fmalc_android.contract.VehicleContract;
import com.demo.fmalc_android.entity.AlertRequestDTO;
import com.demo.fmalc_android.entity.VehicleInspection;
import com.demo.fmalc_android.retrofit.NetworkingUtils;
import com.demo.fmalc_android.service.VehicleService;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VehiclePresenter implements VehicleContract.Presenter {

    VehicleContract.View view;

    public void setView(VehicleContract.View view) {
        this.view = view;
    }

    VehicleService vehicleService = NetworkingUtils.getVehicleService();



    @Override
    public void findVehicleLicensePlatesAndInspectionForReportInspectionBeforeDelivery(List<Integer> status, String username, String auth) {
        Call<VehicleInspection> call = vehicleService.findVehicleLicensePlatesAndInspectionForReportInspectionBeforeDelivery(status, username, auth);

        call.enqueue(new Callback<VehicleInspection>() {
            @Override
            public void onResponse(Call<VehicleInspection> call, Response<VehicleInspection> response) {
                if (response.code() == 204){
                    view.findVehicleLicensePlatesAndInspectionForReportInspectionBeforeDeliveryFailure("Dữ liệu bạn yêu cầu hiện không có");
                }else if (response.code() == 200){
                    view.findVehicleLicensePlatesAndInspectionForReportInspectionBeforeDeliverySuccess(response.body());
                }else{
                    view.findVehicleLicensePlatesAndInspectionForReportInspectionBeforeDeliveryFailure("Có lỗi xảy ra trong quá trình lấy dữ liệu " + response.code());
                }
            }

            @Override
            public void onFailure(Call<VehicleInspection> call, Throwable t) {
                if (t.getMessage().contains("timed out")){
                    view.findVehicleLicensePlatesAndInspectionForReportInspectionBeforeDeliveryFailure("Vui lòng kiểm tra lại kết nối mạng");
                }else if (t.getMessage().contains("Unable to resolve host")) {
                    view.findVehicleLicensePlatesAndInspectionForReportInspectionBeforeDeliveryFailure("Mất kết nối mạng");
                }else{
                    view.findVehicleLicensePlatesAndInspectionForReportInspectionBeforeDeliveryFailure("Xin thử lại sau ít phút");
                }
            }

        });
    }

    @Override
    public void getVehicleRunning(String username, String auth) {
        Call<Integer> call = vehicleService.getVehicleRunning(username, auth);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.code() == 204){
                    view.getVehicleRunningFailure("Bạn đang không chạy xe");
                }else if (response.code() == 200){
                    view.getVehicleRunningSuccess(response.body());
                }else{
                    view.getVehicleRunningFailure("Có lỗi xảy ra trong quá trình lấy dữ liệu");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                if (t.getMessage().contains("timed out")){
                    view.getVehicleRunningFailure("Vui lòng kiểm tra lại kết nối mạng");
                }else if (t.getMessage().contains("Unable to resolve host")) {
                    view.getVehicleRunningFailure("Mất kết nối mạng");
                }else{
                    view.getVehicleRunningFailure("Xin thử lại sau ít phút");
                }
            }

        });
    }

//    @Override
//    public void sendRequestWhileRunning(AlertRequestDTO alertRequest,String auth) {
//        Call<ResponseBody> call = vehicleService.sendRequestWhileRunning(alertRequest,auth);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call call, Response response) {
//                if (response.code() == 204){
//                    view.sendRequestWhileRunningFailure("Bạn đang không chạy xe");
//                }else if (response.code() == 200){
//                    view.sendRequestWhileRunningSuccess("sd");
//                }else{
//                    view.sendRequestWhileRunningFailure("Có lỗi xảy ra trong quá trình gửi yêu cầu");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                if (t.getMessage().contains("timed out")){
//                    view.sendRequestWhileRunningFailure("Vui lòng kiểm tra lại kết nối mạng");
//                }else if (t.getMessage().contains("Unable to resolve host")) {
//                    view.sendRequestWhileRunningFailure("Mất kết nối mạng");
//                }else{
//                    view.sendRequestWhileRunningFailure("Xin thử lại sau ít phút");
//                }
//            }
//
//        });
//
//    }
}
