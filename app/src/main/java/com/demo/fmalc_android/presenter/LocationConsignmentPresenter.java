package com.demo.fmalc_android.presenter;

import com.demo.fmalc_android.contract.LocationConsignmentContract;
import com.demo.fmalc_android.entity.Location;
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
}
