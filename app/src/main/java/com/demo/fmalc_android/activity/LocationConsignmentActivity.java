package com.demo.fmalc_android.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.demo.fmalc_android.R;

public class LocationConsignmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_consignment);
        String consignmentId= getIntent().getStringExtra("CONSIGNMENT_ID");
        String licensePlates = getIntent().getStringExtra("LICENSE_PLATES");
        String status = (getIntent().getStringExtra("CONSIGNMENT_STATUS"));

    }
}