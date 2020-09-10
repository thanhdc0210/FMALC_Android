package com.demo.fmalc_android.activity;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.fmalc_android.R;
import com.demo.fmalc_android.adapter.ScheduleTimeStepAdapter;
import com.demo.fmalc_android.contract.ConsignmentDetailContract;
import com.demo.fmalc_android.contract.DetailedScheduleContract;
import com.demo.fmalc_android.directionhelpers.DistanceParse;
import com.demo.fmalc_android.directionhelpers.FetchURL;
import com.demo.fmalc_android.directionhelpers.TaskLoadedCallback;
import com.demo.fmalc_android.entity.DetailedSchedule;
import com.demo.fmalc_android.entity.GlobalVariable;
import com.demo.fmalc_android.entity.ListStatusUpdate;
import com.demo.fmalc_android.entity.Maintenance;
import com.demo.fmalc_android.entity.Notification;
import com.demo.fmalc_android.entity.Place;
import com.demo.fmalc_android.entity.VehicleDetail;
import com.demo.fmalc_android.enumType.NotificationTypeEnum;
import com.demo.fmalc_android.enumType.ConsignmentStatusEnum;
import com.demo.fmalc_android.enumType.DriverStatusEnum;
import com.demo.fmalc_android.enumType.VehicleStatusEnum;
import com.demo.fmalc_android.presenter.ConsignmentDetailPresenter;
import com.demo.fmalc_android.presenter.DetailedSchedulePresenter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;

import static com.demo.fmalc_android.entity.GlobalVariable.latitudePark;
import static com.demo.fmalc_android.entity.GlobalVariable.longitudePark;


public class ConsignmentDetailActivity extends AppCompatActivity implements TaskLoadedCallback, DetailedScheduleContract.View, ConsignmentDetailContract.View {

    private ConsignmentDetailPresenter consignmentDetailPresenter;
    public static final int DEFAULT_UPDATE_INTERVAL = 60;
    public static final int FAST_UPDATE_INTERVAL = 60 * 2;
    private static final int PERMISSIONS_FINE_LOCATION = 99;
    private String locationTemp = "";
    private long startTie = 0;
    private static final int DEFAULT_SCHEDULE_ID = 0;
    private int interval = 1000 * 60 * 2;// 2 phut ;
    private VehicleDetail vehicleDetail;
    private Integer numberConsignment;
    private Handler handler = new Handler();
    private Runnable runnable;
    private int delay = 1000 * 20;
    private GlobalVariable globalVariable;
    //location request is config file for all settings related to FusedLocationProviderClient
    private LocationRequest locationRequest;
    private Integer meter;
    private LocationCallback locationCallBack;
    //google's api for location services. the majority of the app functions using this class
    private FusedLocationProviderClient fusedLocationProviderClient;
    private List<Place> placeList;
    private DetailedSchedulePresenter detailedSchedulePresenter;
    private DetailedSchedule detailedSchedule;
    private DetailedSchedule consignmentDetail;
    private int numOfConsignmentInDay = -1;
    ScheduleTimeStepAdapter scheduleTimeStepAdapter;
    //    ScheduleTimeStepAdapter consignmentTimeStepAdapter;
    @BindView(R.id.txtTitleConsignmentNo)
    TextView txtTitleConsignmentNo;
    @BindView(R.id.time_step_item)
    LinearLayout consignmentDetailLayout;
    @BindView(R.id.txtLicensePlates)
    TextView txtLicensePlates;

    private static final String DISTANCE_MATRIX = "distancematrix";
    private static final String MODE_DRIVING = "driving";

    private Button btnLocationConsignment;
    private Button btnTracking;
    private ImageButton btnNote;
    private RecyclerView consignmentDetailRecycleView;
    private String license; // bien so xe
    private int consignment_id = 0; // id consignment
    private int schedule_id = 0;
    private List<Integer> placeId = new ArrayList<>();

//    public void getPlaceList(List<Place> placeList) {
//        this.placeList = placeList;
//    }


    public void getDetail(DetailedSchedule detailedSchedule) {
        this.detailedSchedule = detailedSchedule;
    }

    private EditText editText;
    private SweetAlertDialog sweetAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consignment_detail);
        Bundle bundle = getIntent().getExtras();
        consignment_id = bundle.getInt("consignment_id");
        schedule_id = bundle.getInt("schedule_id");
        consignmentDetailRecycleView = findViewById(R.id.rvTimeStep);
        txtLicensePlates = findViewById(R.id.txtLicensePlates);
        txtTitleConsignmentNo = findViewById(R.id.txtTitleConsignmentNo);
        btnNote = (ImageButton) findViewById(R.id.btnNote);
        btnTracking = findViewById(R.id.btnUpdateStatus);
        init();
        globalVariable = (GlobalVariable) getApplicationContext();

        btnLocationConsignment = findViewById(R.id.btnLocationConsignment);
        locationRequest = new LocationRequest();

        editText = new EditText(ConsignmentDetailActivity.this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setTextColor(Color.BLACK);
        editText.setPadding(20, 0, 20, 0);
        sweetAlertDialog = new SweetAlertDialog(ConsignmentDetailActivity.this, SweetAlertDialog.NORMAL_TYPE);
        sweetAlertDialog.setTitleText("Custom view");
        sweetAlertDialog.setConfirmText("Ok");
        sweetAlertDialog.setContentView(editText);

        locationRequest.setInterval(1000 * DEFAULT_UPDATE_INTERVAL);
        // how often does the location check occur when set to the most frequent update
        locationRequest.setFastestInterval(1000 * FAST_UPDATE_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationCallBack = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                //save the location
                startLocationUpdates();
            }
        };
//        detailedSchedulePresenter.findScheduleByConsignment_IdAndDriver_Id(id, globalVariable.getId());
        globalVariable.setIdScheduleNow(schedule_id);
        consignmentDetailPresenter.findByConsignmentId(schedule_id);

        btnLocationConsignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("schedule_id", schedule_id);
                startActivity(intent);
            }
        });

        btnTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.WAITING.getValue()).equals(consignmentDetail.getStatus())) {
//                    System.out.println(consignmentDetail.getStatus());
                    ListStatusUpdate listStatusUpdate = new ListStatusUpdate();
                    listStatusUpdate.setVehicle_status(VehicleStatusEnum.RUNNING.getValue());
                    listStatusUpdate.setConsignment_status(ConsignmentStatusEnum.OBTAINING.getValue());
                    listStatusUpdate.setDriver_status(DriverStatusEnum.RUNNING.getValue());
                    globalVariable.setIdSchedule(consignmentDetail.getScheduleId());
                    List<Place> places = new ArrayList<>();
                    globalVariable.setPlaces(places);
                    globalVariable.setConsignmentDetail(consignmentDetail);
                    detailedSchedulePresenter.updateConsDriVeh(listStatusUpdate, consignmentDetail.getScheduleId());
                    consignmentDetailPresenter.stopTracking(vehicleDetail.getId());
                    updateGPS();
                    tracking();
                }
            }
        });
        consignmentDetailPresenter.getFirstConsignment(globalVariable.getId());
    }//end oncreate

    private void init() {
        detailedSchedulePresenter = new DetailedSchedulePresenter();
        detailedSchedulePresenter.setView(this);
        consignmentDetailPresenter = new ConsignmentDetailPresenter();
        consignmentDetailPresenter.setView(this);
    }

    private void tracking() {
        startLocationUpdates();
    }

    private int tmp = 0;

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_FINE_LOCATION
            );
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallBack, null);
        if (tmp == 0) {
            tmp = 1;
            handler.postDelayed(runnable = new Runnable() {
                public void run() {
                    updateGPS();
                    tmp = 0;
                }
            }, delay);
        }
    }

    private void updateGPS() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(ConsignmentDetailActivity.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    System.out.println("Sao khong chay vạy");
                    try {
                        duration(location);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        detailLocation(location);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
            }
        }
    }



    @Override
    public void findByConsignmentIdSuccess(DetailedSchedule consignmentDetail) {
//        consignmentDetail.get
//        globalVariable.get
        this.consignmentDetail = consignmentDetail;
        scheduleTimeStepAdapter = new ScheduleTimeStepAdapter(consignmentDetail.getPlaces(), this);
        if (consignmentDetail.getScheduleId()!= null && (consignmentDetail.getScheduleId() == globalVariable.getIdScheduleNow())) {
            if (consignmentDetail.getStatus().equals(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.OBTAINING.getValue()))||
                    consignmentDetail.getStatus().equals(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.DELIVERING.getValue()))
            ) {


                if (globalVariable.getIdSchedule() > 0) {
                    globalVariable.setConsignmentDetail(consignmentDetail);
                } else {
                    globalVariable.setIdSchedule(consignmentDetail.getScheduleId());
                    globalVariable.setConsignmentDetail(consignmentDetail);
                    List<Place> places = new ArrayList<>();
                    globalVariable.setPlaces(places);
                    for (int i = 0; i < consignmentDetail.getPlaces().size(); i++) {
                        if (consignmentDetail.getPlaces().get(i).getActualTime() != null) {
                            globalVariable.getPlaces().add(consignmentDetail.getPlaces().get(i));
                        }
                    }

                }
                if (globalVariable.getPlaces().size() == consignmentDetail.getPlaces().size() && consignmentDetail.getStatus()!= ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.COMPLETED.getValue())) {
                    btnTracking.setText(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.COMPLETED.getValue()));
                    btnTracking.setClickable(true);
                    btnTracking.setBackgroundColor(Color.rgb(91, 202, 96));
                    btnTracking.setTextColor(Color.WHITE);
                    btnTracking.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            btnTracking.setText(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.COMPLETED.getValue()));
                            final String[] content = new String[1];
                            editText = new EditText(ConsignmentDetailActivity.this);
                            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                            editText.setTextColor(Color.BLACK);
                            editText.setPadding(20, 0, 20, 0);
                            sweetAlertDialog = new SweetAlertDialog(ConsignmentDetailActivity.this, SweetAlertDialog.NORMAL_TYPE);
                            sweetAlertDialog.setTitleText("Số km hiện tại");
                            sweetAlertDialog.setConfirmText("Hoàn thành");
                            sweetAlertDialog.setCustomView(editText);
                            sweetAlertDialog.show();
                            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    content[0] = editText.getText().toString();
                                    int km = Integer.parseInt(content[0]);
                                    if(km>0){
                                        ListStatusUpdate listStatusUpdate = new ListStatusUpdate();
                                        listStatusUpdate.setVehicle_status(VehicleStatusEnum.AVAILABLE.getValue());
                                        listStatusUpdate.setConsignment_status(ConsignmentStatusEnum.COMPLETED.getValue());
                                        listStatusUpdate.setDriver_status(DriverStatusEnum.AVAILABLE.getValue());
                                        detailedSchedulePresenter.updateConsDriVeh(listStatusUpdate, globalVariable.getConsignmentDetail().getScheduleId());
                                        consignmentDetailPresenter.updatePlannedTime(vehicleDetail.getId(), km);
                                        consignmentDetailPresenter.findByConsignmentId(globalVariable.getIdSchedule());
                                        btnTracking.setText(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.COMPLETED.getValue()));
                                        btnTracking.setClickable(false);
                                        btnTracking.setBackgroundColor(Color.GRAY);
                                        btnTracking.setTextColor(Color.WHITE);
                                        sweetAlertDialog.hide();
                                        Toast.makeText(ConsignmentDetailActivity.this, "Bạn đã hoàn thành lô hàng", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(ConsignmentDetailActivity.this, "Vui lòng nhập số km lớn hơn 0", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });


                        }

//                        }
                    });
                }
                this.consignmentDetail = consignmentDetail;
                scheduleTimeStepAdapter = new ScheduleTimeStepAdapter(consignmentDetail.getPlaces(), this);
                consignmentDetailRecycleView.setAdapter(scheduleTimeStepAdapter);
//                detailedSchedulePresenter.numOfConsignment(globalVariable.getId());
                consignmentDetailRecycleView.setLayoutManager(new LinearLayoutManager(this));
                txtTitleConsignmentNo.setText("CHI TIẾT DỊCH VỤ " + consignmentDetail.getConsignmentId());
                txtLicensePlates.setText(consignmentDetail.getLicensePlates());
                consignmentDetailPresenter.getVehicleDetailByLicense(consignmentDetail.getLicensePlates());
                if (ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.WAITING.getValue()).equals(consignmentDetail.getStatus())) {

                } else if ((ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.OBTAINING.getValue()).equals(consignmentDetail.getStatus())
                        ||ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.DELIVERING.getValue()).equals(consignmentDetail.getStatus())) && globalVariable.getIdSchedule()>0 && consignmentDetail.getScheduleId() == globalVariable.getIdSchedule()
                ) {
                    if(globalVariable.getPlaces() != null && consignmentDetail.getPlaces().size() == globalVariable.getPlaces().size() ){

                    }else{
                        btnTracking.setText(consignmentDetail.getStatus());
                        btnTracking.setClickable(false);
                        btnTracking.setBackgroundColor(Color.GRAY);
                        btnTracking.setTextColor(Color.WHITE);
                    }

                }
                btnNote.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        new SweetAlertDialog(ConsignmentDetailActivity.this, SweetAlertDialog.NORMAL_TYPE)
                                .setTitleText("Ghi chú")
                                .setContentText(consignmentDetail.getOwnerNote())
                                .show();
//                        new MaterialAlertDialogBuilder(ConsignmentDetailActivity.this, R.style.AlertDialog)
//                                .setIcon(getDrawable(R.drawable.ic_chat_24px))
//                                .setTitle("Ghi chú")
//                                .setMessage(consignmentDetail.getOwnerNote())
//                                .setPositiveButton("OK", null)
//                                .show();

                    }
                });

                if (consignmentDetail.getStatus().equals(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.DELIVERING.getValue()))
                        || consignmentDetail.getStatus().equals(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.OBTAINING.getValue()))) {
                    System.out.println("SAO K CHAY");

                    if (globalVariable.getIdSchedule() < 1 || globalVariable.getIdSchedule() != consignmentDetail.getScheduleId()) {
                        globalVariable.setIdSchedule(consignmentDetail.getScheduleId());
                        List<Place> places = new ArrayList<>();
                        globalVariable.setPlaceList(places);
                        for (int i = 0; i < consignmentDetail.getPlaces().size(); i++) {
                            if (consignmentDetail.getPlaces().get(i).getActualTime() != null) {
                                globalVariable.getPlaces().add(consignmentDetail.getPlaces().get(i));
                            }
                        }
                    }
                    updateGPS();
                    tracking();
                } else if (consignmentDetail.getStatus().equals(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.COMPLETED.getValue()))) {
                    btnTracking.setText(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.COMPLETED.getValue()));
                    btnTracking.setClickable(false);
                    btnTracking.setBackgroundColor(Color.GRAY);
                    btnTracking.setTextColor(Color.WHITE);
                }
            }else {

                detailedSchedulePresenter.getScheduleRunningForDriver(globalVariable.getId());
                scheduleTimeStepAdapter = new ScheduleTimeStepAdapter(consignmentDetail.getPlaces(), this);
                consignmentDetailRecycleView.setAdapter(scheduleTimeStepAdapter);
                detailedSchedulePresenter.numOfConsignment(globalVariable.getId());
                consignmentDetailRecycleView.setLayoutManager(new LinearLayoutManager(this));
                txtTitleConsignmentNo.setText("CHI TIẾT DỊCH VỤ " + consignmentDetail.getConsignmentId());
                txtLicensePlates.setText(consignmentDetail.getLicensePlates());
                consignmentDetailPresenter.getVehicleDetailByLicense(consignmentDetail.getLicensePlates());
                if (ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.WAITING.getValue()).equals(consignmentDetail.getStatus())) {

                } else {
                   if( globalVariable.getPlaces() != null && consignmentDetail.getPlaces().size() == globalVariable.getPlaces().size()){

                   }else{
                       btnTracking.setText(consignmentDetail.getStatus());
                       btnTracking.setClickable(false);
                       btnTracking.setBackgroundColor(Color.GRAY);
                       btnTracking.setTextColor(Color.WHITE);
                   }
                }
                btnNote.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        new SweetAlertDialog(ConsignmentDetailActivity.this, SweetAlertDialog.NORMAL_TYPE)
                                .setTitleText("Ghi chú")
                                .setContentText(consignmentDetail.getOwnerNote())
                                .show();

                    }
                });

                if (consignmentDetail.getStatus().equals(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.DELIVERING.getValue()))
                        || consignmentDetail.getStatus().equals(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.OBTAINING.getValue()))) {

                    if (globalVariable.getIdSchedule() < 1 || globalVariable.getIdSchedule() != consignmentDetail.getScheduleId()) {
                        globalVariable.setIdSchedule(consignmentDetail.getScheduleId());
                        List<Place> places = new ArrayList<>();
                        globalVariable.setPlaceList(places);
                        for (int i = 0; i < consignmentDetail.getPlaces().size(); i++) {
                            if (consignmentDetail.getPlaces().get(i).getActualTime() != null) {
                                globalVariable.getPlaces().add(consignmentDetail.getPlaces().get(i));
                            }
                        }
                        if ( globalVariable.getPlaces() != null && globalVariable.getPlaces().size() == consignmentDetail.getPlaces().size()) {

                        }
                    }
                    updateGPS();
                    tracking();
                } else if (consignmentDetail.getStatus().equals(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.COMPLETED.getValue()))) {
                    btnTracking.setText(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.COMPLETED.getValue()));
                    btnTracking.setClickable(false);
                    btnTracking.setBackgroundColor(Color.GRAY);
                    btnTracking.setTextColor(Color.WHITE);
                    updateGPS();
                    tracking();
                }
            }

        } else if (  globalVariable.getIdSchedule() > 0) {
            globalVariable.setConsignmentDetail(consignmentDetail);

            consignmentDetailPresenter.getVehicleDetailByLicense(consignmentDetail.getLicensePlates());
            if (ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.WAITING.getValue()).equals(consignmentDetail.getStatus())) {

            } else {
               if(globalVariable.getPlaces() != null && globalVariable.getPlaces().size() == consignmentDetail.getPlaces().size()){
//                 if()
                    if(!consignmentDetail.getStatus().equals(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.COMPLETED.getValue()))&& consignmentDetail.getScheduleId()==globalVariable.getIdSchedule()){
                        btnTracking.setText(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.COMPLETED.getValue()));
                        btnTracking.setClickable(true);
                        btnTracking.setBackgroundColor(Color.GREEN);
                        btnTracking.setTextColor(Color.WHITE);
                    }
                }else{
                    btnTracking.setText(consignmentDetail.getStatus());
                    btnTracking.setClickable(false);
                    btnTracking.setBackgroundColor(Color.GRAY);
                    btnTracking.setTextColor(Color.WHITE);
                }
            }

            btnNote.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    new SweetAlertDialog(ConsignmentDetailActivity.this, SweetAlertDialog.NORMAL_TYPE)
                            .setTitleText("Ghi chú")
                            .setContentText(consignmentDetail.getOwnerNote())
                            .show();

                }
            });


            if (globalVariable.getConsignmentDetail().getStatus().equals(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.DELIVERING.getValue()))
                    || globalVariable.getConsignmentDetail().getStatus().equals(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.OBTAINING.getValue()))) {
                System.out.println("SAO K CHAY");
                updateGPS();
                tracking();

            } else if (consignmentDetail.getStatus().equals(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.COMPLETED.getValue()))) {
                btnTracking.setText(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.COMPLETED.getValue()));
                btnTracking.setClickable(false);
                btnTracking.setBackgroundColor(Color.GRAY);
                btnTracking.setTextColor(Color.WHITE);
                updateGPS();
                tracking();
            }
        } else if (globalVariable.getIdSchedule() <= 0 && !consignmentDetail.getStatus().equals(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.OBTAINING.getValue()))
                && !consignmentDetail.getStatus().equals(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.DELIVERING.getValue()))) {

            detailedSchedulePresenter.getScheduleRunningForDriver(globalVariable.getId());
        }
    }

    @Override
    public void findByConsignmentIdFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void trackingLocationSuccess(ResponseBody responseBody) {

    }

    @Override
    public void trackingLocationSFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getVehicleDetailByLicenseSuccess(VehicleDetail responseBody) {
//        vehicleDetail = new VehicleDetail();
        this.vehicleDetail = responseBody;
//            getVehicleDetail(responseBody);
    }

    @Override
    public void getVehicleDetailByLicenseFailed(String responseBody) {
        Toast.makeText(this, responseBody, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sendNotificationSuccess(Notification notification) {
//        Toast.makeText(this, "Thông báo thành công", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sendNotificationFailed(String notification) {

    }

    @Override
    public void updateActualTimeSuccess(Place place) {
        if(consignmentDetail.getStatus()!=ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.COMPLETED.getValue())){

        }else{
            Toast.makeText(this, "Bạn sắp đến " + place.getAddress(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void updateActualTimeFailed(String message) {

    }

    @Override
    public void updatePlannedTimeSuccess(Maintenance responseBody) {
        if (responseBody.getActualMaintainDate() != null) {
            Notification notification = new Notification();
            notification.setDriver_id(responseBody.getDriver());
            notification.setVehicle_id(responseBody.getVehicle());
            notification.setStatus(false);
            notification.setContent(responseBody.getMaintenanceType());
            notification.setType(NotificationTypeEnum.MAINTAIN_SCHEDULE.getValue());
            consignmentDetailPresenter.notifyForManager(notification);
        }
    }

    @Override
    public void updatePlannedTimeFailed(String responseBody) {

    }

    @Override
    public void stopTrackingSuccess(String string) {

    }

    @Override
    public void stopTrackingFailed(String message) {

    }

    @Override
    public void notifyForManagerSuccess(String success) {

    }

    @Override
    public void notifyForManagerFailed(String message) {

    }

    @Override
    public void getFirstConsignmentSuccess(int idConsignment) {
        if(idConsignment != consignment_id){
            btnTracking.setClickable(false);
            btnTracking.setBackgroundColor(Color.GRAY);
            btnTracking.setTextColor(Color.WHITE);
        }
    }

    @Override
    public void getFirstConsignmentFailed(String message) {
        Toast.makeText(ConsignmentDetailActivity.this, "cccccccccccccccccccc", Toast.LENGTH_SHORT).show();
    }


    private void checkingPosition() {

    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origins=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destinations=" + dest.latitude + "," + dest.longitude;
        // Mode
        String key = "AIzaSyBMUeWW7cGPbl14igFrHElHdc27gNJE-n4";
//        String key="AIzaSyDcw-KnCAPzni3QYvgk3RDoTZL65xh8a4o";
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/distancematrix/" + output + "?" + parameters + "&key=" + key;
//        System.out.println(url);
        return url;
    }

    private void duration(Location location) throws IOException, ExecutionException, InterruptedException {
        System.out.println("Cũng vô");
        Place place = new Place();
        if (globalVariable.getIdSchedule() <= DEFAULT_SCHEDULE_ID && globalVariable.getConsignmentDetail() == null) {

        } else if (globalVariable.getPlaces().size() < globalVariable.getConsignmentDetail().getPlaces().size()) {
            List<Place> placesDeli = new ArrayList<>();
            for (int i = 0; i < globalVariable.getConsignmentDetail().getPlaces().size(); i++) {
                if (globalVariable.getConsignmentDetail().getPlaces().get(i).getType() == 1) {
                    placesDeli.add(globalVariable.getConsignmentDetail().getPlaces().get(i));
                }
            }
//            globalVariable.getConsignmentDetail().getPlaces().sort(Comparator.comparing(Place::getPlannedTime));
            List<Place> places = globalVariable.getConsignmentDetail().getPlaces();
//            places.sort(Comparator.comparing(Place::getPlannedTime).reversed());
            if (places.size() > 0 && globalVariable.getPlaces() != null) {
//                places.sort(Comparator.comparing(Place::getPlannedTime));

                int i = globalVariable.getPlaces().size();
                if (i < places.size()) {
                    place = places.get(i);
                } else {
                }
                if (place.getType() != null) {
//                     = -1;
                    int me =-1;
                    try{
                        LatLng origin = new LatLng(location.getLatitude(), location.getLongitude());
                        LatLng destination = new LatLng(place.getLatitude(), place.getLongitude());
                        String s = new FetchURL(ConsignmentDetailActivity.this, DISTANCE_MATRIX).execute(getUrl(origin, destination, MODE_DRIVING), MODE_DRIVING).get();
                        if(s!=null){
                            me = new DistanceParse(ConsignmentDetailActivity.this, MODE_DRIVING).execute(s).get();
                        }
                    }catch (Exception e){
                            me=-1;
                    }

                    System.out.println("VALUEEEE" + me);
                    if (place.getType() == 1) {
                        if (place.getActualTime() != null) {
                            globalVariable.getPlaces().add(place);
                        }
                        System.out.println("TYPEEEEEEEEEEE" + place.getType());
                        if (i == 0 && me <= 500 && !globalVariable.getPlaces().contains(places.get(i))) {
                            System.out.println("???????????????/" + place.getType());
                        } else if (me >= 0 && me <= 500 && !globalVariable.getPlaces().contains(places.get(i))) {
                            if (i < places.size() - 1 && places.get(i + 1).getType() == 0) {
                                if (globalVariable.getIdScheduleNow() == globalVariable.getConsignmentDetail().getScheduleId()) {
                                    System.out.println(places.get(0).getAddress() + "------------");
                                    btnTracking.setText(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.OBTAINING.getValue()));
                                    btnTracking.setClickable(false);
                                    btnTracking.setBackgroundColor(Color.GRAY);
                                    btnTracking.setTextColor(Color.WHITE);
                                    globalVariable.getPlaces().add(places.get(i));
                                }


                                if (!globalVariable.getPlaces().contains(places.get(i)) && places.get(i).getActualTime() == null) {
                                    globalVariable.getPlaces().add(places.get(places.size() - 1));
                                    ListStatusUpdate listStatusUpdate = new ListStatusUpdate();
                                    listStatusUpdate.setVehicle_status(VehicleStatusEnum.RUNNING.getValue());
                                    listStatusUpdate.setConsignment_status(ConsignmentStatusEnum.OBTAINING.getValue());
                                    listStatusUpdate.setDriver_status(DriverStatusEnum.RUNNING.getValue());
                                    detailedSchedulePresenter.updateConsDriVeh(listStatusUpdate, consignmentDetail.getScheduleId());
                                    consignmentDetailPresenter.updateActualTime(places.get(i).getId(), globalVariable.getIdSchedule());
                                    consignmentDetailPresenter.findByConsignmentId(globalVariable.getIdSchedule());
                                }


                            } else if (me >= 0 && me <= 500 && i == places.size() - 1 && (!globalVariable.getPlaces().contains(places.get(i)) || places.get(i).getActualTime() != null)) {
                                System.out.println(places.get(0).getAddress() + "------------");
                                consignmentDetailPresenter.updateActualTime(places.get(i).getId(), globalVariable.getIdSchedule());
                                if (globalVariable.getIdScheduleNow() == globalVariable.getConsignmentDetail().getScheduleId()) {
                                    btnTracking.setText(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.COMPLETED.getValue()));
                                    btnTracking.setClickable(true);
                                    btnTracking.setBackgroundColor(Color.rgb(91, 202, 96));
                                    btnTracking.setTextColor(Color.WHITE);
                                    btnTracking.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            btnTracking.setText(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.COMPLETED.getValue()));
                                            final String[] content = new String[1];
                                            editText = new EditText(ConsignmentDetailActivity.this);
                                            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                                            editText.setTextColor(Color.BLACK);
                                            editText.setPadding(20, 0, 20, 0);
                                            sweetAlertDialog = new SweetAlertDialog(ConsignmentDetailActivity.this, SweetAlertDialog.NORMAL_TYPE);
                                            sweetAlertDialog.setTitleText("Số km hiện tại");
                                            sweetAlertDialog.setConfirmText("Hoàn thành");
                                            sweetAlertDialog.setCustomView(editText);
                                            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {

                                                    content[0] = editText.getText().toString();
                                                    int km = Integer.parseInt(content[0]);
                                                    if(km>0){
                                                        ListStatusUpdate listStatusUpdate = new ListStatusUpdate();
                                                        listStatusUpdate.setVehicle_status(VehicleStatusEnum.AVAILABLE.getValue());
                                                        listStatusUpdate.setConsignment_status(ConsignmentStatusEnum.COMPLETED.getValue());
                                                        listStatusUpdate.setDriver_status(DriverStatusEnum.AVAILABLE.getValue());
                                                        detailedSchedulePresenter.updateConsDriVeh(listStatusUpdate, globalVariable.getConsignmentDetail().getScheduleId());
                                                        consignmentDetailPresenter.updatePlannedTime(vehicleDetail.getId(), km);
                                                        consignmentDetailPresenter.findByConsignmentId(globalVariable.getIdSchedule());
                                                        btnTracking.setText(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.COMPLETED.getValue()));
                                                        btnTracking.setClickable(false);
                                                        btnTracking.setBackgroundColor(Color.GRAY);
                                                        btnTracking.setTextColor(Color.WHITE);
                                                        sweetAlertDialog.hide();
                                                        Toast.makeText(ConsignmentDetailActivity.this, "Bạn đã hoàn thành lô hàng", Toast.LENGTH_SHORT).show();
                                                    }else{
                                                        Toast.makeText(ConsignmentDetailActivity.this, "Vui lòng nhập số km lớn hơn 0", Toast.LENGTH_SHORT).show();
                                                    }

                                                }
                                            });


                                            sweetAlertDialog.show();
                                        }

                                    });
                                }

                            }
                        }
                    } else if (0 <= me && me <= 500 && place.getType() == 0) {
                        System.out.println(places.get(0).getAddress() + "------------");
                        if (place.getActualTime() != null) {
                            globalVariable.getPlaces().add(place);
                        }
                        if ( !globalVariable.getPlaces().contains(places.get(0))) {
                            if (places.size() - 1 > i && places.get(1).getType() == 1) {
                                System.out.println(places.get(0).getAddress() + "------------");

                                if (globalVariable.getIdScheduleNow() == globalVariable.getConsignmentDetail().getScheduleId()) {
                                    btnTracking.setText(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.DELIVERING.getValue()));
                                    btnTracking.setClickable(false);
                                    btnTracking.setBackgroundColor(Color.GRAY);
                                    btnTracking.setTextColor(Color.WHITE);
                                }
                                if (!globalVariable.getPlaces().contains(places.get(i)) || places.get(i).getActualTime() == null) {
                                    globalVariable.getPlaces().add(places.get(i));
                                    ListStatusUpdate listStatusUpdate = new ListStatusUpdate();
                                    listStatusUpdate.setVehicle_status(VehicleStatusEnum.RUNNING.getValue());
                                    listStatusUpdate.setConsignment_status(ConsignmentStatusEnum.DELIVERING.getValue());
                                    listStatusUpdate.setDriver_status(DriverStatusEnum.RUNNING.getValue());
                                    detailedSchedulePresenter.updateConsDriVeh(listStatusUpdate, consignmentDetail.getScheduleId());
                                    consignmentDetailPresenter.updateActualTime(places.get(i).getId(), globalVariable.getIdSchedule());
                                    consignmentDetailPresenter.findByConsignmentId(globalVariable.getIdSchedule());
                                }
                            }
                        }
                    }

                }

            }
        }

    }


    private int DISTANCE_PARKING = 0;
    private String pattern = "yyyy-MM-dd";
    private long timeToBackParking = 0;

    @SneakyThrows
    private void detailLocation(final Location location) throws IOException {

//        detailedSchedulePresenter.findByScheduleId(globalVariable.getIdSchedule());

        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        if (globalVariable.getIdSchedule() > 0) {

            Geocoder geocoder = new Geocoder(ConsignmentDetailActivity.this);
            List<Address> address = new ArrayList<>();
            String temp = "";
            try {
                address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (address.size() > 0) {
                    temp = address.get(0).getAddressLine(0);
                } else {
                    temp = "";
                    locationTemp = "";
                }
            } catch (Exception e) {
                temp = "";
                locationTemp = "";
            }

            if (globalVariable.getConsignmentDetail().getStatus() == ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.COMPLETED.getValue())
                    || globalVariable.getPlaces().size() == globalVariable.getConsignmentDetail().getPlaces().size()
            ) {
                System.out.println("Cũng vô luôn 1 ");
                detailedSchedulePresenter.checkConsignmentInDay(globalVariable.getId());
                System.out.println("Cũng vô luôn 2 ");
                if (numOfConsignmentInDay == 0) {
                    System.out.println("Heen");
                    int me = -1;
                    try{
                        LatLng destination = new LatLng(latitudePark, longitudePark);
                        LatLng origin = new LatLng(location.getLatitude(), location.getLongitude());
                        String s = new FetchURL(ConsignmentDetailActivity.this, DISTANCE_MATRIX).execute(getUrl(origin, destination, MODE_DRIVING), MODE_DRIVING).get();
                        if(s!=null){
                            me = new DistanceParse(ConsignmentDetailActivity.this, MODE_DRIVING).execute(s).get();
                        }
                    }catch (Exception e){

                    }



                    if (me>0 && me <= 100) {
                        consignmentDetailPresenter.stopTracking(vehicleDetail.getId());
                        globalVariable.setIdSchedule(DEFAULT_SCHEDULE_ID);
                    } else if (me>0 && DISTANCE_PARKING == 0) {
                        DISTANCE_PARKING = me;
                        timeToBackParking = System.currentTimeMillis();
                    } else if (me>0 && DISTANCE_PARKING > me) {
                        DISTANCE_PARKING = me;
                        timeToBackParking = System.currentTimeMillis();
                    } else if ( me>0 &&  DISTANCE_PARKING <= me) {
                        if (((System.currentTimeMillis() - timeToBackParking) / (interval)) >= 1) {
                            Notification notification = new Notification();
                            notification.setVehicle_id(vehicleDetail.getId());
                            notification.setDriver_id(globalVariable.getId());
                            notification.setStatus(true);
                            notification.setType(NotificationTypeEnum.ODD_HOURS_ALERTS.getValue());
                            notification.setContent("Xe biển số :" + vehicleDetail.getLicensePlates() + " " + NotificationTypeEnum.getValueEnumToShow(NotificationTypeEnum.ODD_HOURS_ALERTS.getValue()) + " tại: " + locationTemp);
                            consignmentDetailPresenter.sendNotification(notification);
                            System.out.println("Chạy ngoài giờ làm việc");
                            timeToBackParking = System.currentTimeMillis();
                        }
                    }

                }
            }


            com.demo.fmalc_android.entity.Location latLng = new com.demo.fmalc_android.entity.Location();
            if (!locationTemp.equals(temp)) {
                locationTemp = temp;
                latLng.setAddress(temp);
                latLng.setLatitude(location.getLatitude());
                latLng.setLongitude(location.getLongitude());
                latLng.setTime(sdf.format(new Date()));
                latLng.setSchedule(globalVariable.getIdSchedule()); //id consignment
                latLng.setAddress(locationTemp);
                System.out.println("Vooooooooooooooooooooooooooo " + locationTemp);
                consignmentDetailPresenter.trackingLocation(latLng);
                startTie = System.currentTimeMillis();
            } else {
                if (((System.currentTimeMillis() - startTie) / (interval)) >= 1) {
                    Notification notification = new Notification();
                    notification.setVehicle_id(vehicleDetail.getId());
                    notification.setDriver_id(globalVariable.getId());
                    notification.setStatus(true);
                    notification.setType(NotificationTypeEnum.LONG_IDLE_TIMES.getValue());
                    notification.setContent("Xe biển số :" + vehicleDetail.getLicensePlates() + " " + NotificationTypeEnum.getValueEnumToShow(NotificationTypeEnum.LONG_IDLE_TIMES.getValue()) + " tại: " + locationTemp);
                    consignmentDetailPresenter.sendNotification(notification);
                    System.out.println(notification.getContent());
                    startTie = System.currentTimeMillis();
//                    handler.postDelayed(runnable, delay);
                }

            }
        }
    }


    @Override
    public void updateConsDriVehSuccess(ListStatusUpdate statusUpdate) {
        if (statusUpdate.getConsignment_status() == ConsignmentStatusEnum.COMPLETED.getValue()) {
            btnTracking.setText(ConsignmentStatusEnum.getValueEnumToShow(statusUpdate.getConsignment_status()));
        } else if (statusUpdate.getConsignment_status() == ConsignmentStatusEnum.DELIVERING.getValue()) {
            btnTracking.setText(ConsignmentStatusEnum.getValueEnumToShow(statusUpdate.getConsignment_status()));
        } else if (statusUpdate.getConsignment_status() == ConsignmentStatusEnum.OBTAINING.getValue()) {
            btnTracking.setText(ConsignmentStatusEnum.getValueEnumToShow(statusUpdate.getConsignment_status()));
        }
        btnTracking.setClickable(false);
        btnTracking.setBackgroundColor(Color.GRAY);
        btnTracking.setTextColor(Color.WHITE);

    }

    @Override
    public void updateConsDriVehFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void numOfConsignmentSuccess(Integer statusUpdate) {
        this.numberConsignment = statusUpdate;
        if (statusUpdate > 0) {
            if (consignmentDetail.getStatus().equals(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.WAITING.getValue()))) {
                btnTracking.setText("Có đơn hàng");
                btnTracking.setClickable(false);
                btnTracking.setBackgroundColor(Color.GRAY);
                btnTracking.setTextColor(Color.WHITE);
            } else if (consignmentDetail.getStatus().equals(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.COMPLETED.getValue())) ) {
                btnTracking.setText(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.COMPLETED.getValue()));
                btnTracking.setClickable(false);
                btnTracking.setBackgroundColor(Color.GRAY);
                btnTracking.setTextColor(Color.WHITE);
            }
        }
    }

    @Override
    public void numOfConsignmentFailed(String message) {

    }


    @Override
    public void checkConsignmentInDaySuccess(Integer numberConsignment) {
        this.numOfConsignmentInDay = numberConsignment;
    }

    @Override
    public void checkConsignmentInDayFailed(String message) {

    }

    @Override
    public void getScheduleRunningForDriverSuccess(DetailedSchedule detailedSchedule) {
        if (detailedSchedule != null) {
            if (detailedSchedule != null && detailedSchedule.getScheduleId() == globalVariable.getIdScheduleNow()) {
                globalVariable.setConsignmentDetail(detailedSchedule);
                consignmentDetailPresenter.getVehicleDetailByLicense(detailedSchedule.getLicensePlates());
                if (ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.WAITING.getValue()).equals(detailedSchedule.getStatus())) {

                } else {
                    btnTracking.setText(detailedSchedule.getStatus());
                    btnTracking.setClickable(false);
                    btnTracking.setBackgroundColor(Color.GRAY);
                    btnTracking.setTextColor(Color.WHITE);
                }

                globalVariable.setIdSchedule(detailedSchedule.getScheduleId());
                globalVariable.setConsignmentDetail(detailedSchedule);
                List<Place> places = new ArrayList<>();
                globalVariable.setPlaces(places);
                for (int i = 0; i < detailedSchedule.getPlaces().size(); i++) {
                    if (detailedSchedule.getPlaces().get(i).getActualTime() != null) {
                        globalVariable.getPlaces().add(detailedSchedule.getPlaces().get(i));
                    }
                }
                if (globalVariable.getPlaces().size() == detailedSchedule.getPlaces().size()) {
                    btnTracking.setText(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.COMPLETED.getValue()));
                    if (detailedSchedule.getStatus().equals(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.COMPLETED.getValue()))) {
                        btnTracking.setClickable(false);
                    } else {
                        btnTracking.setClickable(true);
                    }

                    btnTracking.setBackgroundColor(Color.rgb(91, 202, 96));
                    btnTracking.setTextColor(Color.WHITE);
                    btnTracking.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            btnTracking.setText(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.COMPLETED.getValue()));
                            final String[] content = new String[1];
                            editText = new EditText(ConsignmentDetailActivity.this);
                            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                            editText.setTextColor(Color.BLACK);
                            editText.setPadding(20, 0, 20, 0);
                            sweetAlertDialog = new SweetAlertDialog(ConsignmentDetailActivity.this, SweetAlertDialog.NORMAL_TYPE);
                            sweetAlertDialog.setTitleText("Số km hiện tại");
                            sweetAlertDialog.setConfirmText("Hoàn thành");
                            sweetAlertDialog.setCustomView(editText);
                            sweetAlertDialog.show();
                            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    content[0] = editText.getText().toString();
                                    int km = Integer.parseInt(content[0]);
                                    if(km>0){
                                        ListStatusUpdate listStatusUpdate = new ListStatusUpdate();
                                        listStatusUpdate.setVehicle_status(VehicleStatusEnum.AVAILABLE.getValue());
                                        listStatusUpdate.setConsignment_status(ConsignmentStatusEnum.COMPLETED.getValue());
                                        listStatusUpdate.setDriver_status(DriverStatusEnum.AVAILABLE.getValue());
                                        detailedSchedulePresenter.updateConsDriVeh(listStatusUpdate, globalVariable.getConsignmentDetail().getScheduleId());
                                        consignmentDetailPresenter.updatePlannedTime(vehicleDetail.getId(), km);
                                        consignmentDetailPresenter.findByConsignmentId(globalVariable.getIdSchedule());
                                        btnTracking.setText(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.COMPLETED.getValue()));
                                        btnTracking.setClickable(false);
                                        btnTracking.setBackgroundColor(Color.GRAY);
                                        btnTracking.setTextColor(Color.WHITE);
                                        sweetAlertDialog.hide();
                                        Toast.makeText(ConsignmentDetailActivity.this, "Bạn đã hoàn thành lô hàng", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(ConsignmentDetailActivity.this, "Vui lòng nhập số km lớn hơn 0", Toast.LENGTH_SHORT).show();
                                    }


                                }
                            });


                        }

//                        }
                    });
                }

                if (globalVariable.getIdSchedule() > 0) {
                    if (globalVariable.getConsignmentDetail().getStatus().equals(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.DELIVERING.getValue()))
                            || globalVariable.getConsignmentDetail().getStatus().equals(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.OBTAINING.getValue()))) {
                        System.out.println("SAO K CHAY");
                        updateGPS();
                        tracking();

                    } else if (detailedSchedule.getStatus().equals(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.COMPLETED.getValue()))) {
                        updateGPS();
                        tracking();
                    }
                }
            } else {
                globalVariable.setConsignmentDetail(detailedSchedule);
//            consignmentDetailPresenter.getVehicleDetailByLicense(detailedSchedule.getLicensePlates());
                globalVariable.setIdSchedule(detailedSchedule.getScheduleId());
                globalVariable.setConsignmentDetail(detailedSchedule);
                List<Place> places = new ArrayList<>();
                globalVariable.setPlaces(places);
                if (globalVariable.getIdSchedule() > 0) {
                    if (globalVariable.getConsignmentDetail().getStatus().equals(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.DELIVERING.getValue()))
                            || globalVariable.getConsignmentDetail().getStatus().equals(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.OBTAINING.getValue()))) {
//                    System.out.println("SAO K CHAY");
                        updateGPS();
                        tracking();

                    } else if (detailedSchedule.getStatus().equals(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.COMPLETED.getValue()))) {
                        updateGPS();
                        tracking();
                    }
                }
            }
        }
    }

    @Override
    public void getScheduleRunningForDriverFailed(String message) {

    }


    @Override
    public void onTaskDone(Object... values) {
    }

    @Override
    public void onDistanceDone(Integer... meters) {
        this.meter = meters[0];

    }
}