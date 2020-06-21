package com.demo.fmalc_android.presenter;

import com.demo.fmalc_android.contract.ConsignmentContract;
import com.demo.fmalc_android.entity.Consignment;
import com.demo.fmalc_android.retrofit.NetworkingUtils;
import com.demo.fmalc_android.service.ConsignmentService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConsignmentPresenter implements ConsignmentContract.Presenter {

    ConsignmentContract.View view;

    public void setView(ConsignmentContract.View view) {
        this.view = view;
    }

    ConsignmentService consignmentService = NetworkingUtils.getConsignmentService();

    @Override
    public void findByConsignmentStatusAndUsername(List<Integer> status, String username) {
        Call<List<Consignment>> call = consignmentService.findByConsignmentStatusAndUsernameForDriver(status, username);
        call.enqueue(new Callback<List<Consignment>>() {
            @Override
            public void onResponse(Call<List<Consignment>> call, Response<List<Consignment>> response) {
                if(!response.isSuccessful()){
                    view.findByConsignmentStatusAndUsernameForFailure("Không thể lấy danh sách");
                }else {
                    List<Consignment> consignmentList = response.body();
                    view.findByConsignmentStatusAndUsernameForSuccess(consignmentList);
                }
            }

            @Override
            public void onFailure(Call<List<Consignment>> call, Throwable t) {
                view.findByConsignmentStatusAndUsernameForFailure("Có lỗi xảy ra trong quá trình lấy danh sách");
            }
        });
    }
}
