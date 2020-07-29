package com.demo.fmalc_android.presenter;

import com.demo.fmalc_android.contract.ReportIssueImageContract;
import com.demo.fmalc_android.entity.ReportIssueRequest;
import com.demo.fmalc_android.retrofit.NetworkingUtils;
import com.demo.fmalc_android.service.ReportIssueService;

import java.io.IOException;
import java.net.URISyntaxException;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportIssueImagePresenter implements ReportIssueImageContract.Presenter {

    ReportIssueImageContract.View view;

    public void setView(ReportIssueImageContract.View view) {
        this.view = view;
    }

    ReportIssueService reportIssueService = NetworkingUtils.getReportIssueService();

    @Override
    public void getLinkImage(MultipartBody.Part file) throws URISyntaxException {

        Call<ResponseBody> call = reportIssueService.uploadImage(file );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    view.getLinkImageAfterUploadS3Failure("Không thể đăng tải hình ảnh");
                } else {
                    if (response.code() == 200) {
                        try {
                            view.getLinkImageAfterUploadS3(response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println("Error");
                        }
                    } else {
                        view.getLinkImageAfterUploadS3Failure("Không thể đăng tải hình ảnh");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                view.getLinkImageAfterUploadS3Failure("Có lỗi xảy ra trong quá trình đăng tải ảnh " + t.getMessage());
                System.out.println(t.getMessage());
            }
        });
    }
}
