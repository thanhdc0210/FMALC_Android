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
    public void getMaintenanceList(Integer driverId, String auth) {
        Call<List<MaintainResponse>> call = maintenanceService.getMaintenanceList(driverId, auth);
        call.enqueue(new Callback<List<MaintainResponse>>() {
            @Override
            public void onResponse(Call<List<MaintainResponse>> call, Response<List<MaintainResponse>> response) {
                if (response.code() == 204){
                    view.getMaintenanceListFailure("Dữ liệu bạn yêu cầu hiện không có");
                }else if (response.code() == 200) {
                    view.getMaintenanceListSuccessful(response.body());
                } else {
                    view.getMaintenanceListFailure("Có lỗi xảy ra trong quá trình lấy dữ liệu ");
                }
            }

            @Override
            public void onFailure(Call<List<MaintainResponse>> call, Throwable t) {
                if (t.getMessage().contains("timed out")){
                    view.getMaintenanceListFailure("Vui lòng kiểm tra lại kết nối mạng");
                }else if (t.getMessage().contains("Unable to resolve host")) {
                    view.getMaintenanceListFailure("Mất kết nối mạng");
                }else{
                    view.getMaintenanceListFailure("Xin thử lại sau ít phút");
                }
            }
        });
    }

    @Override
    public void updateMaintenance(Integer driverId, Integer kmOld, MultipartBody.Part file, String auth) throws URISyntaxException {
        Call<ResponseBody> call = maintenanceService.updateMaintenance(driverId, kmOld, file, auth);
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
