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

//    private static final String BASE_URL = "http://10.1.16.57:8080/fmalc/";
//    private static final String BASE_URL = "http://fmalc-env.eba-epptzq6v.ap-southeast-1.elasticbeanstalk.com/";
    private static final String BASE_URL = "http://api.fmalc.online/";


    public static synchronized Retrofit getInstance() {

//        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
//                .callTimeout(2, TimeUnit.MINUTES)..
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
