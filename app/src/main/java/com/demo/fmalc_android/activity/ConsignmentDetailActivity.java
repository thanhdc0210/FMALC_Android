package com.demo.fmalc_android.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.fmalc_android.R;
import com.demo.fmalc_android.adapter.ConsignmentTimeStepAdapter;
import com.demo.fmalc_android.contract.ConsignmentDetailContract;
import com.demo.fmalc_android.entity.ConsignmentDetail;
import com.demo.fmalc_android.entity.Place;
import com.demo.fmalc_android.presenter.ConsignmentDetailPresenter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

import butterknife.BindView;


public class ConsignmentDetailActivity extends AppCompatActivity implements ConsignmentDetailContract.View , View.OnClickListener {

    private ConsignmentDetailPresenter consignmentDetailPresenter;
    private ConsignmentDetail consignmentDetail;
    ConsignmentTimeStepAdapter consignmentTimeStepAdapter;
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

    public void getDetail(ConsignmentDetail consignmentDetail) {
        this.consignmentDetail = consignmentDetail;
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
        consignmentDetailPresenter.findByConsignmentId(id);
        btnLocationConsignment = findViewById(R.id.btnLocationConsignment);
        btnLocationConsignment.setOnClickListener(this);
//        Toast.makeText(this, consignmentDetail.getConsignmentId()+consignmentDetail.getLicensePlates(), Toast.LENGTH_SHORT).show();



    }

    private void init() {
        consignmentDetailPresenter = new ConsignmentDetailPresenter();
        consignmentDetailPresenter.setView(this);

    }


    @Override
    public void findByConsignmentIdSuccess(ConsignmentDetail consignmentDetail) {
//        txtTitleConsignmentNo.setText(consignmentDetail.getConsignmentId()+"");
//        txtLicensePlates.setText(consignmentDetail.getLicensePlates());
        consignmentTimeStepAdapter = new ConsignmentTimeStepAdapter(consignmentDetail.getPlaces(), this);
        consignmentDetailRecycleView.setAdapter(consignmentTimeStepAdapter);
        consignmentDetailRecycleView.setLayoutManager(new LinearLayoutManager(this));
        txtTitleConsignmentNo.setText("CHI TIẾT DỊCH VỤ " + consignmentDetail.getConsignmentId());
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
    public void findByConsignmentIdFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        System.out.println(consignmentDetail.getLicensePlates()+"PPPPPPPP");
        Intent intent = new Intent(getBaseContext(), LocationConsignmentActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtra("CONSIGNMENT_ID", consignmentDetail.getConsignmentId()+"");
        intent.putExtra("LICENSE_PLATES", consignmentDetail.getLicensePlates());
        intent.putExtra("CONSIGNMENT_STATUS", consignmentDetail.getStatus());
        startActivity(intent);
    }
}