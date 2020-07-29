package com.demo.fmalc_android.presenter;

import com.demo.fmalc_android.contract.DriverContract;
import com.demo.fmalc_android.contract.MaintenanceContract;
import com.demo.fmalc_android.entity.DriverInformation;
import com.demo.fmalc_android.entity.MaintainResponse;
import com.demo.fmalc_android.retrofit.NetworkingUtils;
import com.demo.fmalc_android.service.DriverService;
import com.demo.fmalc_android.service.MaintenanceService;

import java.net.URISyntaxException;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MaintenancePresenter implements MaintenanceContract.Presenter {

    MaintenanceContract.View view;

    public void setView(MaintenanceContract.View view) {
        this.view = view;
    }

    MaintenanceService maintenanceService = NetworkingUtils.getMaintenanceService();


    @Override
    public void getMaintenanceList(Integer driverId) {
        Call<List<MaintainResponse>> call = maintenanceService.getMaintenanceList(driverId);
        call.enqueue(new Callback<List<MaintainResponse>>() {
            @Override
            public void onResponse(Call<List<MaintainResponse>> call, Response<List<MaintainResponse>> response) {
                if (!response.isSuccessful()) {
                    view.getMaintenanceListFailure("Không thể tải thông tin");

                } else {
                    if (response.code() == 200 || response.code() == 204) {
                        List<MaintainResponse> maintainResponses = response.body();
                        view.getMaintenanceListSuccessful(maintainResponses);
                    }

                }
            }

            @Override
            public void onFailure(Call<List<MaintainResponse>> call, Throwable t) {
                view.getMaintenanceListFailure("Không thể tải thông tin " + t.getMessage());
            }
        });
    }

    @Override
    public void updateMaintenance(Integer driverId, Integer kmOld, MultipartBody.Part file) throws URISyntaxException {
        Call<ResponseBody> call = maintenanceService.updateMaintenance(driverId, kmOld, file);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    view.updateMaintenanceFailure("Không thể cập nhật");

                } else {
                    if (response.code() == 200 ) {
                        view.updateMaintenanceSuccessful(true);
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                view.updateMaintenanceFailure("Không thể cập nhật");
            }
        });
    }
}
