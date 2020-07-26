package com.demo.fmalc_android.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.demo.fmalc_android.R;
import com.demo.fmalc_android.contract.LocationConsignmentContract;
import com.demo.fmalc_android.entity.Notification;
import com.demo.fmalc_android.entity.VehicleDetail;
import com.demo.fmalc_android.presenter.LocationConsignmentPresenter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;

public class LocationConsignmentActivity extends AppCompatActivity {



    private VehicleDetail vehicleDetail;
    private LocationConsignmentPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_consignment);
        String consignmentId= getIntent().getStringExtra("CONSIGNMENT_ID");
        String licensePlates = getIntent().getStringExtra("LICENSE_PLATES");
        String status = (getIntent().getStringExtra("CONSIGNMENT_STATUS"));

        //map


        //
//        init();

//        presenter.getVehicleDetailByLicense(licensePlates);


    }

//    private  void init(){
//        presenter = new LocationConsignmentPresenter();
//        presenter.setView(this);
//    }















    }
