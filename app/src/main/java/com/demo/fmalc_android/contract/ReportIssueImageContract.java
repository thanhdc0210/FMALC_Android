package com.demo.fmalc_android.contract;

import android.content.Context;
import android.net.Uri;

import java.net.URISyntaxException;

import okhttp3.MultipartBody;

public interface ReportIssueImageContract {
    interface View{
        void getLinkImageAfterUploadS3(String url);
        void getLinkImageAfterUploadS3Failure(String message);
    }

    interface Presenter{
        void getLinkImage(MultipartBody.Part file, String auth) throws URISyntaxException;
    }
}
