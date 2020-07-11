package com.demo.fmalc_android.presenter;

import android.content.Context;
import android.net.Uri;
import android.os.FileUtils;
import android.util.Log;
import android.widget.Toast;

import com.demo.fmalc_android.contract.ReportIssueContract;
import com.demo.fmalc_android.contract.VehicleContract;
import com.demo.fmalc_android.entity.VehicleInspection;
import com.demo.fmalc_android.retrofit.NetworkingUtils;
import com.demo.fmalc_android.service.ReportIssueService;
import com.demo.fmalc_android.service.VehicleService;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

public class ReportIssuePresenter implements ReportIssueContract.Presenter {

    ReportIssueContract.View view;

    public void setView(ReportIssueContract.View view) {
        this.view = view;
    }

    ReportIssueService reportIssueService = NetworkingUtils.getReportIssueService();




    @Override
    public void getLinkImage(MultipartBody.Part file) throws URISyntaxException {


        Call<ResponseBody> call = reportIssueService.uploadImage(file );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(!response.isSuccessful()){
                    view.getLinkImageAfterUploadS3Failure("Không thể đăng tải hình ảnh");
                }else {
                    if(response.code() == 200) {
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
