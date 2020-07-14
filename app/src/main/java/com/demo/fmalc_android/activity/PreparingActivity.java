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
import com.demo.fmalc_android.contract.VehicleContract;
import com.demo.fmalc_android.entity.GlobalVariable;
import com.demo.fmalc_android.entity.Inspection;
import com.demo.fmalc_android.entity.ReportIssueContentRequest;
import com.demo.fmalc_android.entity.ReportIssueRequest;
import com.demo.fmalc_android.entity.VehicleInspection;
import com.demo.fmalc_android.presenter.ReportIssuePresenter;
import com.demo.fmalc_android.presenter.VehicleInspectionPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class PreparingActivity extends AppCompatActivity implements VehicleContract.View, ReportIssueContract.View {

    private VehicleInspection vehicleInspection;
    private VehicleInspectionPresenter vehicleInspectionPresenter;
    private ReportIssuePresenter reportIssuePresenter;
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
        setTitle("Báo cáo trước khi chạy");
        setContentView(R.layout.activity_preparing);
        Bundle bundle = getIntent().getExtras();
        String vehicleStatus = bundle.getString("VEHICLE_STATUS");

        Toast.makeText(this, vehicleStatus, Toast.LENGTH_SHORT).show();
        init();
        List<Integer> status = new ArrayList<>();
        status.add(Integer.parseInt(vehicleStatus));
        status.add(1);
        globalVariable = (GlobalVariable) getApplicationContext();
        vehicleInspectionPresenter.getListLicensePlate(status, globalVariable.getUsername());
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


        btnSubmit = findViewById(R.id.btnSubmit);
        inspectionAdapter.getListIssue();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                globalVariable = (GlobalVariable) getApplicationContext();
                // Report Issue Before Delivery
                ReportIssueRequest reportIssueRequest = new ReportIssueRequest();
                reportIssueRequest.setReportIssueContentRequests(inspectionAdapter.getListIssue());
                reportIssueRequest.setUsername(globalVariable.getUsername());
                reportIssueRequest.setVehicleLicensePlates(spinner.getSelectedItem().toString());
                reportIssueRequest.setType(0);

                for(Map.Entry<Integer, ReportIssueContentRequest> entry : inspectionAdapter.getListIssue().entrySet()){
                    System.out.println("Inspection Id: " + entry.getValue().getInspectionId()
                    + " Content: " + entry.getValue().getContent() + "/n");
                }

                System.out.println("Vehicle License Plates: " + spinner.getSelectedItem().toString());
            }
        });


    }

    @Override
    public void getListLicensePlateAndInspectionFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void createReportIssueForSuccess(ReportIssueRequest reportIssueRequest) {

    }

    @Override
    public void createReportIssueForFailure(String message) {

    }


    // tắt bàn phím
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}