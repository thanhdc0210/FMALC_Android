package com.demo.fmalc_android.presenter;

import android.app.Activity;

import com.demo.fmalc_android.contract.ConsignmentDetailContract;
//import com.demo.fmalc_android.entity.ConsignmentDetail;
import com.demo.fmalc_android.entity.DetailedSchedule;
import com.demo.fmalc_android.entity.Location;
import com.demo.fmalc_android.entity.Maintenance;
import com.demo.fmalc_android.entity.Notification;
import com.demo.fmalc_android.entity.Place;
import com.demo.fmalc_android.entity.VehicleDetail;
import com.demo.fmalc_android.retrofit.NetworkingUtils;
//import com.demo.fmalc_android.service.ConsignmentService;
import com.demo.fmalc_android.service.LocationConsignmentService;
import com.demo.fmalc_android.service.MaintenanceService;
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

    MaintenanceService maintenanceService = NetworkingUtils.getMaintenanceService();

    public void findByConsignmentId(Integer scheduleId) {
        Call<DetailedSchedule> call = consignmentService.findScheduleById(scheduleId);
       call.enqueue(new Callback<DetailedSchedule>() {
           @Override
           public void onResponse(Call<DetailedSchedule> call, Response<DetailedSchedule> response) {
               if (response.code() == 204){
                   view.findByConsignmentIdFailure("Bạn không đủ quyền để xem thông tin chi tiết");
               }else if (response.code() == 200){
                   view.findByConsignmentIdSuccess(response.body());
               }else{
                   view.findByConsignmentIdFailure("Có lỗi xảy ra trong quá trình lấy dữ liệu");
               }
           }

           @Override
           public void onFailure(Call<DetailedSchedule> call, Throwable t) {
               if (t.getMessage().contains("timed out")){
                   view.findByConsignmentIdFailure("Vui lòng kiểm tra lại kết nối mạng");
               }else if (t.getMessage().contains("Unable to resolve host")) {
                   view.findByConsignmentIdFailure("Mất kết nối mạng");
               }else{
                   view.findByConsignmentIdFailure("Xin thử lại sau ít phút");
               }
           }
       });
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


    @Override
    public void updateActualTime(Integer placeId,Integer idSchedule) {

        Call<Place> call = service.updateActualTime(placeId,idSchedule );
        call.enqueue(new Callback<Place>() {
            @Override
            public void onResponse(Call<Place> call, Response<Place> response) {
                if(response.isSuccessful()){
                    view.updateActualTimeSuccess(response.body());
                }else{
                    view.updateActualTimeFailed("");
                }
            }

            @Override
            public void onFailure(Call<Place> call, Throwable t) {
                view.updateActualTimeFailed("Có lỗi xảy ra!");
            }
        });
    }

    @Override
    public void updatePlannedTime(Integer id, Integer km) {
        Call<Maintenance> call = maintenanceService.updatePlannedTime(id,km);
        call.enqueue(new Callback<Maintenance>() {
            @Override
            public void onResponse(Call<Maintenance> call, Response<Maintenance> response) {
                if(response.isSuccessful()){
                    view.updatePlannedTimeSuccess(response.body());
                }else{
                    view.updatePlannedTimeFailed("");
                }
            }

            @Override
            public void onFailure(Call<Maintenance> call, Throwable t) {
                view.updatePlannedTimeFailed("");
            }
        });
    }

    @Override
    public void stopTracking(Integer id) {
        Call<String> call = service.stopTracking(id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    view.stopTrackingSuccess("Thành công");
                }else{
                    view.updateActualTimeFailed("Có lỗi xảy ra");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                view.updateActualTimeFailed("Có lỗi xảy ra");
            }
        });
    }

    @Override
    public void notifyForManager(Notification notification) {
        Call<ResponseBody> call = service.notifyForManager(notification);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    view.notifyForManagerSuccess("Lich bảo trì xe đã được gửi");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                view.notifyForManagerFailed("Có lỗi xảy ra!");
            }
        });
    }

    @Override
    public void getFirstConsignment(int idDriver) {
        Call<Integer> call =consignmentService.getFirstConsignment(idDriver);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()){
                    view.getFirstConsignmentSuccess(response.body());
                }else{
                    view.getFirstConsignmentFailed("");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                view.getFirstConsignmentFailed("Có lỗi xảy ra!");
            }
        });
    }
}
