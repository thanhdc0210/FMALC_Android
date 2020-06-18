package com.demo.fmalc_android.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.fmalc_android.R;

import butterknife.BindView;

public class ConsignmentDetailActivity extends AppCompatActivity {
@BindView(R.id.txtTitleConsignmentNo)
TextView consignmentNo;
@BindView(R.id.txtTitleVehicleNo)
        TextView vehicleNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consignment_detail);
        Bundle bundle = getIntent().getExtras();

//Extract the data…
        String venName = bundle.getString("giangg");
        Toast.makeText(this, venName, Toast.LENGTH_SHORT).show();
//Create the text view

//        consignmentNo.setText(intent.getStringExtra("message"));
//        vehicleNo.setText(intent.getStringExtra("message"));
    }
}