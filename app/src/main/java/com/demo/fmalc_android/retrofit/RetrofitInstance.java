package com.demo.fmalc_android.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    private static Retrofit retrofit;
    private static Gson gson;

    private static final String BASE_URL = "http://192.168.0.101:8080/fmalc/";

    public static synchronized Retrofit getInstance() {

//        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
//                .callTimeout(2, TimeUnit.MINUTES)
//                .connectTimeout(20, TimeUnit.SECONDS)
//                .readTimeout(30, TimeUnit.SECONDS)
//                .writeTimeout(30, TimeUnit.SECONDS);

        HttpLoggingInterceptor loggingInterceptor =  new HttpLoggingInterceptor();

        // set your desired log level
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

        // add logging as last interceptor
//        clientBuilder.addInterceptor(loggingInterceptor);

        if (retrofit == null) {
            if (gson == null) {
                gson = new GsonBuilder().setLenient().create();
            }

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(new OkHttpClient.Builder().build())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

        }

        return retrofit;
    }
}
