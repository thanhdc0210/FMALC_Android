package com.demo.fmalc_android.presenter;

import com.demo.fmalc_android.contract.ConsignmentDetailContract;
//import com.demo.fmalc_android.entity.ConsignmentDetail;
import com.demo.fmalc_android.entity.DetailedSchedule;
import com.demo.fmalc_android.entity.Location;
import com.demo.fmalc_android.entity.Notification;
import com.demo.fmalc_android.entity.VehicleDetail;
import com.demo.fmalc_android.retrofit.NetworkingUtils;
//import com.demo.fmalc_android.service.ConsignmentService;
import com.demo.fmalc_android.service.LocationConsignmentService;
import com.demo.fmalc_android.service.ScheduleService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConsignmentDetailPresenter implements ConsignmentDetailContract.Presenter {

    ConsignmentDetailContract.View view;

    public void setView(ConsignmentDetailContract.View view) {
        this.view = view;
    }

    ScheduleService consignmentService = NetworkingUtils.getScheduleService();

    LocationConsignmentService service = NetworkingUtils.getLocationConsignmentService();


    public void findByConsignmentId(Integer id) {
        Call<DetailedSchedule> call = consignmentService.findByScheduleId(id);
       call.enqueue(new Callback<DetailedSchedule>() {
           @Override
           public void onResponse(Call<DetailedSchedule> call, Response<DetailedSchedule> response) {
               if (!response.isSuccessful()) {
                   view.findByConsignmentIdFailure("Không tìm thấy lô hàng");
               } else {
                   DetailedSchedule consignmentDetail = response.body();
                   view.findByConsignmentIdSuccess(consignmentDetail);
               }
           }

           @Override
           public void onFailure(Call<DetailedSchedule> call, Throwable t) {
               view.findByConsignmentIdFailure("Có lỗi xảy ra trong quá trình lấy thông tin");
           }
       });


//        call.enqueue(new Callback<DetailedSchedule>() {
//            @Override
//            public void onResponse(Call<DetailedSchedule> call, Response<DetailedSchedule> response) {
//                if (!response.isSuccessful()) {
//                    view.findByConsignmentIdFailure("Không tìm thấy lô hàng");
//                } else {
//                    DetailedSchedule consignmentDetail = response.body();
//                    view.findByConsignmentIdSuccess(consignmentDetail);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ConsignmentDetail> call, Throwable t) {
//                view.findByConsignmentIdFailure("Có lỗi xảy ra trong quá trình lấy thông tin");
//
//            }
//        });
    }

    @Override
    public void trackingLocation(Location location) {

        Call<ResponseBody> call = service.trackingLocation(location);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    if(response.code() == 200){

                    }
                }else{
                    view.trackingLocationSFailed("Không thể lấy vị trí. Vui lòng kiểm tra lại");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                view.trackingLocationSFailed("Có lỗi xảy ra trong khi lấy vị trí");
            }
        });

    }

    @Override
    public void getVehicleDetailByLicense(String license) {
        Call<VehicleDetail> call = service.getDetailVehicleByLicense(license);
        call.enqueue(new Callback<VehicleDetail>() {
            @Override
            public void onResponse(Call<VehicleDetail> call, Response<VehicleDetail> response) {
                if(response.isSuccessful()){
                    if(response.code()==200){
                        VehicleDetail vehicleDetail = response.body();
                        view.getVehicleDetailByLicenseSuccess(vehicleDetail);
                    }else{
                        view.getVehicleDetailByLicenseFailed("Không tìm thấy xe. Xin thử lại");
                    }
                }else{
                    view.getVehicleDetailByLicenseFailed("Không tìm thấy xeeeee. Xin thử lại" + response.code());
                }
            }

            @Override
            public void onFailure(Call<VehicleDetail> call, Throwable t) {
                view.getVehicleDetailByLicenseFailed("Không thể lấy được thông tin. Xin thử lại");
            }
        });


    }

    @Override
    public void sendNotification(Notification notification) {
        Call<Notification> call = service.notifyWorkingHours(notification);
        call.enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call<Notification> call, Response<Notification> response) {
                if(response.isSuccessful()){
                    if(response.code()==200){
                        Notification notification1 = response.body();
                        view.sendNotificationSuccess(notification1);
                    }else{
                        view.findByConsignmentIdFailure("Không thể thông báo. Vui lòng kiểm tra");
                    }
                }
            }

            @Override
            public void onFailure(Call<Notification> call, Throwable t) {
                view.findByConsignmentIdFailure("Không thể thông báo. Vui lòng kiểm tra");
            }
        });
    }
}
