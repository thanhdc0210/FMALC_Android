package com.demo.fmalc_android.presenter;

import com.demo.fmalc_android.contract.ConsignmentDetailContract;
import com.demo.fmalc_android.entity.ConsignmentDetail;
import com.demo.fmalc_android.retrofit.NetworkingUtils;
import com.demo.fmalc_android.service.ConsignmentService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConsignmentDetailPresenter implements ConsignmentDetailContract.Presenter {

    ConsignmentDetailContract.View view;

    public void setView(ConsignmentDetailContract.View view) {
        this.view = view;
    }

    ConsignmentService consignmentService = NetworkingUtils.getConsignmentService();


    public void findByConsignmentId(Integer id) {
        Call<ConsignmentDetail> call = consignmentService.findByConsignmentId(id);
        call.enqueue(new Callback<ConsignmentDetail>() {
            @Override
            public void onResponse(Call<ConsignmentDetail> call, Response<ConsignmentDetail> response) {
                if (!response.isSuccessful()) {
                    view.findByConsignmentIdFailure("Không tìm thấy lô hàng");
                } else {
                    ConsignmentDetail consignmentDetail = response.body();
                    view.findByConsignmentIdSuccess(consignmentDetail);
                }
            }

            @Override
            public void onFailure(Call<ConsignmentDetail> call, Throwable t) {
                view.findByConsignmentIdFailure("Có lỗi xảy ra trong quá trình lấy thông tin");

            }
        });
    }
}
