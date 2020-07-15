package com.demo.fmalc_android.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.fmalc_android.R;
import com.demo.fmalc_android.adapter.InspectionAdapter;
import com.demo.fmalc_android.contract.ReportIssueContract;
import com.demo.fmalc_android.contract.VehicleAfterDeliveryContract;
import com.demo.fmalc_android.contract.VehicleContract;
import com.demo.fmalc_android.entity.GlobalVariable;
import com.demo.fmalc_android.entity.Inspection;
import com.demo.fmalc_android.entity.ReportIssueRequest;
import com.demo.fmalc_android.entity.VehicleInspection;
import com.demo.fmalc_android.presenter.ReportIssuePresenter;
import com.demo.fmalc_android.presenter.VehicleAfterDeliveryPresenter;
import com.demo.fmalc_android.presenter.VehicleInspectionPresenter;

import java.util.ArrayList;
import java.util.List;


public class PreparingActivity extends AppCompatActivity implements VehicleContract.View, ReportIssueContract.View, VehicleAfterDeliveryContract.View {

    private VehicleInspection vehicleInspection;
    private VehicleInspectionPresenter vehicleInspectionPresenter;
    private ReportIssuePresenter reportIssuePresenter;
    private VehicleAfterDeliveryPresenter vehicleAfterDeliveryPresenter;
    private  List<String> spinnerArray = new ArrayList<>();
    private InspectionAdapter inspectionAdapter;
    private List<Inspection> inspectionList;
    private Button btnSubmit;
    private EditText edtNoteIssue;
    Menu menu;
    private GlobalVariable globalVariable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preparing);
        Bundle bundle = getIntent().getExtras();
        String vehicleStatus = bundle.getString("VEHICLE_STATUS");

        Toast.makeText(this, vehicleStatus, Toast.LENGTH_SHORT).show();
        init();
        List<Integer> status = new ArrayList<>();
        globalVariable = (GlobalVariable) getApplicationContext();
        if(vehicleStatus.contains("0")){
            status.add(Integer.parseInt(vehicleStatus));
            status.add(1);
            setTitle("Báo cáo trước khi chạy");
            vehicleInspectionPresenter.getListLicensePlate(status, globalVariable.getUsername());
        }else{
            setTitle("Báo cáo sau khi chạy");
            status.add(Integer.parseInt(vehicleStatus));
            vehicleAfterDeliveryPresenter.getListLicensePlateAndInspectionAfterDelivery(status, globalVariable.getUsername());
        }

    }

    private void setUpRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerViewInspection);

//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        inspectionAdapter = new InspectionAdapter(inspectionList,this);
        recyclerView.setAdapter(inspectionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    private void init(){
        vehicleInspectionPresenter = new VehicleInspectionPresenter();
        vehicleInspectionPresenter.setView(this);

        reportIssuePresenter = new ReportIssuePresenter();
        reportIssuePresenter.setView(this);

        vehicleAfterDeliveryPresenter = new VehicleAfterDeliveryPresenter();
        vehicleAfterDeliveryPresenter.setView(this);
    }
    private void getVehicleInspection(VehicleInspection vehicleInspection){
        this.vehicleInspection = vehicleInspection;
    }
    @Override
    public void getListLicensePlateAndInspectionSuccess(VehicleInspection vehicleInspection) {

        //Đổ data cho spinner
       spinnerArray.add("Chọn một xe");
       vehicleInspection.getVehicleLicensePlates().forEach(e-> spinnerArray.add(e));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = (Spinner) findViewById(R.id.spinnerLicensePlates);
        spinner.setAdapter(adapter);

        // Đổ data cho inspection list recycle view
        inspectionList = vehicleInspection.getInspections();
        setUpRecyclerView();

        //Submit data
        inspectionAdapter.getListIssue();
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                globalVariable = (GlobalVariable) getApplicationContext();
                // Report Issue Before Delivery
                String vehicleLicensePlates = spinner.getSelectedItem().toString();
                if (vehicleLicensePlates.contains("một xe")){
                    Toast.makeText(getApplicationContext(),"Vui lòng chọn xe để báo cáo",Toast.LENGTH_SHORT);
                }else {
                    ReportIssueRequest reportIssueRequest = new ReportIssueRequest();
                    reportIssueRequest.setReportIssueContentRequests(inspectionAdapter.getListIssue());
                    reportIssueRequest.setUsername(globalVariable.getUsername());
                    reportIssueRequest.setVehicleLicensePlates(spinner.getSelectedItem().toString());
                    reportIssueRequest.setType(0);

                    reportIssuePresenter.createReportIssueBeforeDelivery(reportIssueRequest);
                }
            }
        });
    }

    @Override
    public void getListLicensePlateAndInspectionFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void createReportIssueBeforeDeliveryForSuccess(ReportIssueRequest reportIssueRequest) {
        Toast.makeText(this, "Báo cáo thành công", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void createReportIssueBeforeDeliveryForFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getListLicensePlateAndInspectionAfterDeliverySuccess(VehicleInspection vehicleInspection) {
        //Đổ data cho spinner
        spinnerArray.add("Chọn một xe");
        vehicleInspection.getVehicleLicensePlates().forEach(e-> spinnerArray.add(e));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = (Spinner) findViewById(R.id.spinnerLicensePlates);
        spinner.setAdapter(adapter);

        // Đổ data cho inspection list recycle view
        inspectionList = vehicleInspection.getInspections();
        setUpRecyclerView();

        //Submit data


        btnSubmit = findViewById(R.id.btnSubmit);
        inspectionAdapter.getListIssue();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalVariable = (GlobalVariable) getApplicationContext();

                // Report Issue Before Delivery
                String vehicleLicensePlates = spinner.getSelectedItem().toString();
                if (vehicleLicensePlates.contains("một xe")){
                    Toast.makeText(getApplicationContext(),"Vui lòng chọn xe để báo cáo",Toast.LENGTH_SHORT);
                }else {
                    ReportIssueRequest reportIssueRequest = new ReportIssueRequest();
                    reportIssueRequest.setReportIssueContentRequests(inspectionAdapter.getListIssue());
                    reportIssueRequest.setUsername(globalVariable.getUsername());
                    reportIssueRequest.setVehicleLicensePlates(spinner.getSelectedItem().toString());
                    reportIssueRequest.setType(1);

                    reportIssuePresenter.createReportIssueBeforeDelivery(reportIssueRequest);
                }
            }
        });
    }

    @Override
    public void getListLicensePlateAndInspectionAfterDeliveryFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    // tắt bàn phím
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}