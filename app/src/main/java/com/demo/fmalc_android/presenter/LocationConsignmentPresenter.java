package com.demo.fmalc_android.presenter;

import com.demo.fmalc_android.contract.LocationConsignmentContract;
import com.demo.fmalc_android.entity.Location;
import com.demo.fmalc_android.entity.VehicleDetail;
import com.demo.fmalc_android.retrofit.NetworkingUtils;
import com.demo.fmalc_android.service.LocationConsignmentService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationConsignmentPresenter implements LocationConsignmentContract.Presenter {

    private LocationConsignmentContract.View view;

    public void setView(LocationConsignmentContract.View view) {
        this.view = view;
    }

    private LocationConsignmentService service = NetworkingUtils.getLocationConsignmentService();

//    @Override
//    public void trackingLocation(Location location) {
//
//        Call<ResponseBody> call = service.trackingLocation(location);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if(response.isSuccessful()){
//                    if(response.code() == 200){
//
//                    }
//                }else{
//                    view.trackingLocationSFailed("Không thể lấy vị trí. Vui lòng kiểm tra lại");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                view.trackingLocationSFailed("Có lỗi xảy ra trong khi lấy vị trí");
//            }
//        });
//
//    }
//
//    @Override
//    public void getVehicleDetailByLicense(String license) {
//        Call<VehicleDetail> call = service.getDetailVehicleByLicense(license);
//        call.enqueue(new Callback<VehicleDetail>() {
//            @Override
//            public void onResponse(Call<VehicleDetail> call, Response<VehicleDetail> response) {
//                if(response.isSuccessful()){
//                    if(response.code()==200){
//                        VehicleDetail vehicleDetail = response.body();
//                        view.getVehicleDetailByLicenseSuccess(vehicleDetail);
//                    }else{
//                        view.getVehicleDetailByLicenseFailed("Không tìm thấy xe. Xin thử lại");
//                    }
//                }else{
//                    view.getVehicleDetailByLicenseFailed("Không tìm thấy xe. Xin thử lại");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<VehicleDetail> call, Throwable t) {
//                view.getVehicleDetailByLicenseFailed("Không thể lấy được thông tin. Xin thử lại");
//            }
//        });
//
//
//    }
}
