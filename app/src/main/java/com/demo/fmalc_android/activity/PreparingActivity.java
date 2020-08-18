package com.demo.fmalc_android.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.demo.fmalc_android.entity.ReportIssueContentRequest;
import com.demo.fmalc_android.entity.ReportIssueRequest;
import com.demo.fmalc_android.entity.VehicleInspection;
import com.demo.fmalc_android.enumType.ConsignmentStatusEnum;
import com.demo.fmalc_android.fragment.InspectionFragment;
import com.demo.fmalc_android.presenter.ReportIssuePresenter;
import com.demo.fmalc_android.presenter.VehicleAfterDeliveryPresenter;
import com.demo.fmalc_android.presenter.VehiclePresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class PreparingActivity extends AppCompatActivity implements VehicleContract.View, ReportIssueContract.View, VehicleAfterDeliveryContract.View {

    private VehicleInspection vehicleInspection;
    private VehiclePresenter vehiclePresenter;
    private ReportIssuePresenter reportIssuePresenter;
    private VehicleAfterDeliveryPresenter vehicleAfterDeliveryPresenter;
    private  List<String> spinnerArray = new ArrayList<>();
    private InspectionAdapter inspectionAdapter;
    private List<Inspection> inspectionList;
    private Button btnSubmit;
    private EditText edtNoteIssue;
    private TextView txtCurrentLicensePlate,txtEmptyView;
    private LinearLayout inforLicensePlate;
    private RecyclerView recyclerView;
    Menu menu;
    private GlobalVariable globalVariable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preparing);
        Bundle bundle = getIntent().getExtras();
        int consignmentStatus = bundle.getInt("CONSIGNMENT_STATUS");
        txtEmptyView = findViewById(R.id.txtEmptyView);
        recyclerView = findViewById(R.id.recyclerViewInspection);
        txtCurrentLicensePlate = findViewById(R.id.txtCurrentLicensePlate);
        inforLicensePlate = findViewById(R.id.inforLicensePlate);
        btnSubmit = findViewById(R.id.btnSubmit);
        init();
        globalVariable = (GlobalVariable) getApplicationContext();
        if(consignmentStatus == ConsignmentStatusEnum.WAITING.getValue()){
            List<Integer> status = new ArrayList<>();
            status.add(ConsignmentStatusEnum.WAITING.getValue());
            setTitle("Báo cáo trước khi chạy");
            vehiclePresenter.findVehicleLicensePlatesAndInspectionForReportInspectionBeforeDelivery(status, globalVariable.getUsername(), globalVariable.getToken());
        }else{
            setTitle("Báo cáo sau khi chạy");
            List<Integer> status = new ArrayList<>();
            status.add(ConsignmentStatusEnum.COMPLETED.getValue());
            status.add(ConsignmentStatusEnum.MISSING_DOCUMENT.getValue());
            vehicleAfterDeliveryPresenter.getListLicensePlateAndInspectionAfterDelivery(status, globalVariable.getUsername(), globalVariable.getToken());
        }

    }

    private void setUpRecyclerView() {


//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        inspectionAdapter = new InspectionAdapter(inspectionList,this, globalVariable.getToken());
        recyclerView.setAdapter(inspectionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    private void init(){
        vehiclePresenter = new VehiclePresenter();
        vehiclePresenter.setView(this);

        reportIssuePresenter = new ReportIssuePresenter();
        reportIssuePresenter.setView(this);

        vehicleAfterDeliveryPresenter = new VehicleAfterDeliveryPresenter();
        vehicleAfterDeliveryPresenter.setView(this);
    }
    private void getVehicleInspection(VehicleInspection vehicleInspection){
        this.vehicleInspection = vehicleInspection;
    }
    @Override
    public void findVehicleLicensePlatesAndInspectionForReportInspectionBeforeDeliverySuccess(VehicleInspection vehicleInspection) {

        if (vehicleInspection.getVehicleLicensePlates().equals("")){
            inforLicensePlate.setVisibility(View.GONE);
            txtEmptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            btnSubmit.setVisibility(View.GONE);

        } else {
            //Gán biển số xe cho textview

            txtCurrentLicensePlate.setText(vehicleInspection.getVehicleLicensePlates());

            // Đổ data cho inspection list recycle view
            inspectionList = vehicleInspection.getInspections();
            setUpRecyclerView();

            //Submit data
            inspectionAdapter.getListIssue();
            btnSubmit = findViewById(R.id.btnSubmit);
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap<Integer, ReportIssueContentRequest> listResult = inspectionAdapter.getListIssue();
                    globalVariable = (GlobalVariable) getApplicationContext();
                    // Report Issue Before Delivery
                    if ( listResult.isEmpty() ) {
                        new SweetAlertDialog(PreparingActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Vui lòng chọn sự cố cần báo cáo")
                                .show();
                    } else {
                        ReportIssueRequest reportIssueRequest = new ReportIssueRequest();
                        for (Map.Entry<Integer, String> image : inspectionAdapter.getImageList().entrySet()) {
                            ReportIssueContentRequest temp = listResult.get(image.getKey());
                            temp.setImage(image.getValue());
                            listResult.replace(image.getKey(), temp);
                        }
                        reportIssueRequest.setReportIssueContentRequests(listResult);
                        reportIssueRequest.setUsername(globalVariable.getUsername());
                        reportIssueRequest.setVehicleLicensePlates(txtCurrentLicensePlate.getText().toString());
                        reportIssueRequest.setType(0);
                        reportIssuePresenter.createReportIssueForDelivery(reportIssueRequest, globalVariable.getToken());
                    }
                }
            });
        }


    }

    @Override
    public void findVehicleLicensePlatesAndInspectionForReportInspectionBeforeDeliveryFailure(String message) {
        Toast.makeText(this, "Hiện tại không có xe nào phù hợp để báo cáo", Toast.LENGTH_SHORT).show();
        onBackPressed();


    }

    @Override
    public void getVehicleRunningSuccess(int vehicleId) {

    }

    @Override
    public void getVehicleRunningFailure(String message) {

    }



    @Override
    public void createReportIssueForDeliveryForSuccess(ReportIssueRequest reportIssueRequest) {
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Báo cáo thành công")
                .setContentText("Báo cáo sự cố của bạn đã được ghi nhận")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        onBackPressed();
                    }
                })
                .show();

    }

    @Override
    public void createReportIssueForDeliveryForFailure(String message) {

    }

    @Override
    public void getListLicensePlateAndInspectionAfterDeliverySuccess(VehicleInspection vehicleInspection) {
        if (vehicleInspection.getVehicleLicensePlates().equals("")) {
            inforLicensePlate.setVisibility(View.GONE);
            txtEmptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            btnSubmit.setVisibility(View.GONE);
        } else {
            //Gán biển số xe cho textview
            txtCurrentLicensePlate = findViewById(R.id.txtCurrentLicensePlate);
            txtCurrentLicensePlate.setText(vehicleInspection.getVehicleLicensePlates());

            // Đổ data cho inspection list recycle view
            inspectionList = vehicleInspection.getInspections();
            setUpRecyclerView();

            //Submit data
            inspectionAdapter.getListIssue();

            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap<Integer, ReportIssueContentRequest> listResult = inspectionAdapter.getListIssue();
                    globalVariable = (GlobalVariable) getApplicationContext();
                    // Report Issue Before Delivery
                    String vehicleLicensePlates = txtCurrentLicensePlate.getText().toString().trim();
                    if (vehicleLicensePlates == "") {
                        Toast.makeText(getApplicationContext(), "Hôm nay bạn không có lịch chạy", Toast.LENGTH_SHORT).show();
                    } else {
                        ReportIssueRequest reportIssueRequest = new ReportIssueRequest();
                        for (Map.Entry<Integer, String> image : inspectionAdapter.getImageList().entrySet()) {
                            ReportIssueContentRequest temp = listResult.get(image.getKey());
                            temp.setImage(image.getValue());
                            listResult.replace(image.getKey(), temp);
                        }
                        reportIssueRequest.setReportIssueContentRequests(listResult);
                        reportIssueRequest.setUsername(globalVariable.getUsername());
                        reportIssueRequest.setVehicleLicensePlates(vehicleLicensePlates);
                        reportIssueRequest.setType(0);

                        reportIssuePresenter.createReportIssueForDelivery(reportIssueRequest, globalVariable.getToken());
                    }
                }
            });
        }
    }

    @Override
    public void getListLicensePlateAndInspectionAfterDeliveryFailure(String message) {
        Toast.makeText(this, "Hiện tại không có xe nào phù hợp để báo cáo", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }


    // tắt bàn phím
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

//    private void reloadActivityAfterCreateReportIssueSuccess(){
//        finish();
//        overridePendingTransition(0, 0);
//        startActivity(getIntent());
//        overridePendingTransition(0, 0);
//    }
}