package com.demo.fmalc_android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.fmalc_android.R;
import com.demo.fmalc_android.adapter.ScheduleTimeStepAdapter;
import com.demo.fmalc_android.contract.DetailedScheduleContract;
import com.demo.fmalc_android.entity.DetailedSchedule;
import com.demo.fmalc_android.entity.Place;
import com.demo.fmalc_android.presenter.DetailedSchedulePresenter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

import butterknife.BindView;


public class ConsignmentDetailActivity extends AppCompatActivity implements DetailedScheduleContract.View , View.OnClickListener {

    private DetailedSchedulePresenter detailedSchedulePresenter;
    private DetailedSchedule detailedSchedule;
    ScheduleTimeStepAdapter scheduleTimeStepAdapter;
    List<Place> placeList;

    @BindView(R.id.txtTitleConsignmentNo)
    TextView txtTitleConsignmentNo;
    @BindView(R.id.time_step_item)
    LinearLayout consignmentDetailLayout;
    @BindView(R.id.txtLicensePlates)
    TextView txtLicensePlates;

    Button btnLocationConsignment;

    ImageButton btnNote;

    RecyclerView consignmentDetailRecycleView;


    public void getPlaceList(List<Place> placeList) {
        this.placeList = placeList;
    }

    public void getDetail(DetailedSchedule detailedSchedule) {
        this.detailedSchedule = detailedSchedule;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consignment_detail);
        Bundle bundle = getIntent().getExtras();
//Extract the data…
        int id = bundle.getInt("consignment_id");

        consignmentDetailRecycleView = findViewById(R.id.rvTimeStep);
        txtLicensePlates = findViewById(R.id.txtLicensePlates);
        txtTitleConsignmentNo = findViewById(R.id.txtTitleConsignmentNo);
        btnNote = (ImageButton) findViewById(R.id.btnNote);
        init();
        detailedSchedulePresenter.findByScheduleId(id);
        btnLocationConsignment = findViewById(R.id.btnLocationConsignment);
        btnLocationConsignment.setOnClickListener(this);
//        Toast.makeText(this, consignmentDetail.getConsignmentId()+consignmentDetail.getLicensePlates(), Toast.LENGTH_SHORT).show();



    }

    private void init() {
        detailedSchedulePresenter = new DetailedSchedulePresenter();
        detailedSchedulePresenter.setView(this);

    }


    @Override
    public void findByScheduleIdSuccess(DetailedSchedule consignmentDetail) {
//        txtTitleConsignmentNo.setText(consignmentDetail.getConsignmentId()+"");
//        txtLicensePlates.setText(consignmentDetail.getLicensePlates());
        scheduleTimeStepAdapter = new ScheduleTimeStepAdapter(consignmentDetail.getPlaces(), this);
        consignmentDetailRecycleView.setAdapter(scheduleTimeStepAdapter);
        consignmentDetailRecycleView.setLayoutManager(new LinearLayoutManager(this));
        txtTitleConsignmentNo.setText("CHI TIẾT DỊCH VỤ " + consignmentDetail.getScheduleId());
        txtLicensePlates.setText(consignmentDetail.getLicensePlates());

        btnNote.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

               new  MaterialAlertDialogBuilder(ConsignmentDetailActivity.this, R.style.AlertDialog)
                       .setIcon(getDrawable(R.drawable.ic_chat_24px))
                       .setTitle("Ghi chú")
                       .setMessage(consignmentDetail.getOwnerNote())
                       .setPositiveButton("OK",null)
                       .show();

            }

        });
        getDetail(consignmentDetail);

    }


    @Override
    public void findByScheduleIdFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
//        System.out.println(detailedSchedule.getLicensePlates()+"PPPPPPPP");
        Intent intent = new Intent(getBaseContext(), LocationConsignmentActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtra("CONSIGNMENT_ID", detailedSchedule.getScheduleId()+"");
        intent.putExtra("LICENSE_PLATES", detailedSchedule.getLicensePlates());
        intent.putExtra("CONSIGNMENT_STATUS", detailedSchedule.getStatus());
        startActivity(intent);
    }
}