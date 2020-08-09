package com.demo.fmalc_android.activity;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
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
import androidx.core.content.ContextCompat;
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
import com.demo.fmalc_android.entity.GlobalPlace;
import com.demo.fmalc_android.entity.GlobalVariable;
import com.demo.fmalc_android.entity.ListStatusUpdate;
import com.demo.fmalc_android.entity.Notification;
import com.demo.fmalc_android.entity.Place;
import com.demo.fmalc_android.entity.VehicleDetail;
import com.demo.fmalc_android.enum1.NotificationTypeEnum;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;


public class ConsignmentDetailActivity extends AppCompatActivity implements TaskLoadedCallback, DetailedScheduleContract.View, ConsignmentDetailContract.View {

    private ConsignmentDetailPresenter consignmentDetailPresenter;
    private DetailedSchedule consignmentDetail;
    public static final int DEFAULT_UPDATE_INTERVAL = 60;
    public static final int FAST_UPDATE_INTERVAL = 60 * 2;
    private static final int PERMISSIONS_FINE_LOCATION = 99;
    private String locationTemp = "";
    private long startTie = 0;
    private int interval = 1000 * 60 * 2;// 2 phut ;
    private VehicleDetail vehicleDetail;
    private Integer numberConsignment;
    private Handler handler = new Handler();
    private Runnable runnable;
    private int delay = 1000 * 60;
    private GlobalVariable globalVariable;
    //location request is config file for all settings related to FusedLocationProviderClient
    private LocationRequest locationRequest;
    private Integer meter;
    private LocationCallback locationCallBack;
    //google's api for location services. the majority of the app functions using this class
    private FusedLocationProviderClient fusedLocationProviderClient;
    private ScheduleTimeStepAdapter consignmentTimeStepAdapter;
    private List<Place> placeList;
    private DetailedSchedulePresenter detailedSchedulePresenter;
    private DetailedSchedule detailedSchedule;
    ScheduleTimeStepAdapter scheduleTimeStepAdapter;
    @BindView(R.id.txtTitleConsignmentNo)
    TextView txtTitleConsignmentNo;
    @BindView(R.id.time_step_item)
    LinearLayout consignmentDetailLayout;
    @BindView(R.id.txtLicensePlates)
    TextView txtLicensePlates;

    private Button btnLocationConsignment;
    private Button btnTracking;
    private ImageButton btnNote;
    private RecyclerView consignmentDetailRecycleView;
    private String license; // bien so xe
    private int id = 0; // id consignment
    private List<Integer> placeId = new ArrayList<>();

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
        id = bundle.getInt("consignment_id");
        consignmentDetailRecycleView = findViewById(R.id.rvTimeStep);
        txtLicensePlates = findViewById(R.id.txtLicensePlates);
        txtTitleConsignmentNo = findViewById(R.id.txtTitleConsignmentNo);
        btnNote = (ImageButton) findViewById(R.id.btnNote);
        btnTracking = findViewById(R.id.btnUpdateStatus);
        init();
        globalVariable = (GlobalVariable) getApplicationContext();

        btnLocationConsignment = findViewById(R.id.btnLocationConsignment);
        locationRequest = new LocationRequest();

        // how often does the default location check occur ?

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

        detailedSchedulePresenter.findByScheduleId(id);

        btnLocationConsignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("SCHEDULE_ID", id);
                startActivity(intent);
            }
        });

        btnTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.WAITING.getValue()).equals(consignmentDetail.getStatus())) {
                    System.out.println(consignmentDetail.getStatus());
                    ListStatusUpdate listStatusUpdate = new ListStatusUpdate();
                    listStatusUpdate.setVehicle_status(VehicleStatusEnum.RUNNING.getValue());
                    listStatusUpdate.setConsignment_status(ConsignmentStatusEnum.OBTAINING.getValue());
                    listStatusUpdate.setDriver_status(DriverStatusEnum.RUNNING.getValue());
                    globalVariable.setIdSchedule(consignmentDetail.getScheduleId());
                    List<Place> places = new ArrayList<>();
                    globalVariable.setPlaceList(places);
                    detailedSchedulePresenter.updateConsDriVeh(listStatusUpdate, consignmentDetail.getScheduleId());
                    consignmentDetailPresenter.stopTracking(vehicleDetail.getId());
                    updateGPS();
                    tracking();
                }
            }
        });

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
        consignmentTimeStepAdapter = new ScheduleTimeStepAdapter(consignmentDetail.getPlaces(), this);
        consignmentDetailRecycleView.setAdapter(consignmentTimeStepAdapter);
    }

    @Override
    public void findByConsignmentIdFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void findByScheduleIdSuccess(DetailedSchedule consignmentDetail) {
        scheduleTimeStepAdapter = new ScheduleTimeStepAdapter(consignmentDetail.getPlaces(), this);
        consignmentDetailRecycleView.setAdapter(scheduleTimeStepAdapter);
        detailedSchedulePresenter.numOfConsignment(globalVariable.getId());
        consignmentDetailRecycleView.setLayoutManager(new LinearLayoutManager(this));
        txtTitleConsignmentNo.setText("CHI TIẾT DỊCH VỤ " + consignmentDetail.getScheduleId());
        txtLicensePlates.setText(consignmentDetail.getLicensePlates());
        consignmentDetailPresenter.getVehicleDetailByLicense(consignmentDetail.getLicensePlates());
        if (ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.WAITING.getValue()).equals(consignmentDetail.getStatus())) {

        } else {
            btnTracking.setText(consignmentDetail.getStatus());
            btnTracking.setClickable(false);
            btnTracking.setBackgroundColor(Color.GRAY);
            btnTracking.setTextColor(Color.WHITE);
        }

        btnNote.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(ConsignmentDetailActivity.this, R.style.AlertDialog)
                        .setIcon(getDrawable(R.drawable.ic_chat_24px))
                        .setTitle("Ghi chú")
                        .setMessage(consignmentDetail.getOwnerNote())
                        .setPositiveButton("OK", null)
                        .show();

            }
        });
        this.consignmentDetail = consignmentDetail;
        if (consignmentDetail.getStatus().equals(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.DELIVERING.getValue()))
                || consignmentDetail.getStatus().equals(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.OBTAINING.getValue()))) {
            System.out.println("SAO K CHAY");

            if (globalVariable.getIdSchedule() < 1 || globalVariable.getIdSchedule() != consignmentDetail.getScheduleId()) {
                globalVariable.setIdSchedule(consignmentDetail.getScheduleId());
                List<Place> places = new ArrayList<>();
                globalVariable.setPlaceList(places);
            }
            updateGPS();
            tracking();

        } else if (consignmentDetail.getStatus().equals(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.COMPLETED.getValue()))) {

        }
    }


    @Override
    public void findByScheduleIdFailure(String message) {
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
        Toast.makeText(this, "Bạn sắp đến " + place.getAddress(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateActualTimeFailed(String message) {

    }

    @Override
    public void updatePlannedTimeSuccess(ResponseBody responseBody) {

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


    private void checkingPosition() {

    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origins=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destinations=" + dest.latitude + "," + dest.longitude;
        // Mode
        String key = "AIzaSyBMUeWW7cGPbl14igFrHElHdc27gNJE-n4";
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
//        Geocoder geocoder = new Geocoder(ConsignmentDetailActivity.this);
//        List<Address> address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        List<Place> placesDeli = new ArrayList<>();
        for (int i = 0; i < consignmentDetail.getPlaces().size(); i++) {
            if (consignmentDetail.getPlaces().get(i).getType() == 1) {
                placesDeli.add(consignmentDetail.getPlaces().get(i));
            }
        }
        List<Place> places = consignmentDetail.getPlaces();
        if (places.size() > 0 && globalVariable.getPlaceList() != null) {
            Collections.sort(places, new Comparator<Place>() {
                @Override
                public int compare(Place o1, Place o2) {
                    return o1.getPlannedTime().compareTo(o2.getPlannedTime());
                }
            });
            int i = globalVariable.getPlaceList().size();
            if (i < places.size()) {
                place = places.get(i);
            } else {
            }
            if (place.getType() != null) {
                LatLng origin = new LatLng(location.getLatitude(), location.getLongitude());
                LatLng destination = new LatLng(place.getLatitude(), place.getLongitude());
                String s = new FetchURL(ConsignmentDetailActivity.this, "distancematrix").execute(getUrl(origin, destination, "driving"), "driving").get();
                int me = new DistanceParse(ConsignmentDetailActivity.this, "driving").execute(s).get();
                System.out.println("VALUEEEE" + me);
                if (place.getType() == 1) {
                    if (i == 0 && me <= 500 && !globalVariable.getPlaceList().contains(places.get(i))) {
                        if (i < places.size() - 1 && places.get(i + 1).getType() == 0) {
                            meter = -1;
                            btnTracking.setText(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.OBTAINING.getValue()));
                            btnTracking.setClickable(false);
                            btnTracking.setBackgroundColor(Color.GRAY);
                            btnTracking.setTextColor(Color.WHITE);
                            detailLocation(location);
                            globalVariable.getPlaceList().add(places.get(i));
                        }
                    } else if (me > 0 && me <= 500 && !globalVariable.getPlaceList().contains(places.get(i))) {
                        if (i < places.size() - 1 && places.get(i + 1).getType() == 0) {
                            handler.postDelayed(runnable = new Runnable() {
                                public void run() {
                                    btnTracking.setText(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.OBTAINING.getValue()));
                                    btnTracking.setClickable(false);
                                    btnTracking.setBackgroundColor(Color.GRAY);
                                    btnTracking.setTextColor(Color.WHITE);
//                                    detailLocation(location);
                                    globalVariable.getPlaceList().add(places.get(i));
                                    meter = -1;
                                    if (!globalVariable.getPlaceList().contains(places.get(i)) || places.get(i).getActualTime() == null) {
                                        globalVariable.getPlaceList().add(places.get(places.size() - 1));
                                        ListStatusUpdate listStatusUpdate = new ListStatusUpdate();
                                        listStatusUpdate.setVehicle_status(VehicleStatusEnum.RUNNING.getValue());
                                        listStatusUpdate.setConsignment_status(ConsignmentStatusEnum.OBTAINING.getValue());
                                        listStatusUpdate.setDriver_status(DriverStatusEnum.RUNNING.getValue());
                                        detailedSchedulePresenter.updateConsDriVeh(listStatusUpdate, consignmentDetail.getScheduleId());
                                        consignmentDetailPresenter.updateActualTime(places.get(i).getId());
                                    }
                                }
                            }, 3000);


                        } else if (me > 0 && me <= 500 && i == places.size() - 1 && (!globalVariable.getPlaceList().contains(places.get(i)) || places.get(i).getActualTime() != null)) {
                            handler.postDelayed(runnable = new Runnable() {
                                public void run() {
                                    meter = -1;
                                    btnTracking.setText(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.COMPLETED.getValue()));
                                    btnTracking.setClickable(true);
                                    btnTracking.setBackgroundColor(Color.rgb(91, 202, 96));
                                    btnTracking.setTextColor(Color.WHITE);
                                    btnTracking.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            if (!globalVariable.getPlaceList().contains(places.get(i)) || places.get(i).getActualTime() == null) {
                                                btnTracking.setText(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.COMPLETED.getValue()));
                                                btnTracking.setClickable(false);
                                                btnTracking.setBackgroundColor(Color.GRAY);
                                                btnTracking.setTextColor(Color.WHITE);
                                                globalVariable.getPlaceList().add(places.get(places.size() - 1));
                                                ListStatusUpdate listStatusUpdate = new ListStatusUpdate();
                                                listStatusUpdate.setVehicle_status(VehicleStatusEnum.AVAILABLE.getValue());
                                                listStatusUpdate.setConsignment_status(ConsignmentStatusEnum.COMPLETED.getValue());
                                                listStatusUpdate.setDriver_status(DriverStatusEnum.AVAILABLE.getValue());
                                                detailedSchedulePresenter.updateConsDriVeh(listStatusUpdate, consignmentDetail.getScheduleId());
                                                consignmentDetailPresenter.updateActualTime(places.get(i).getId());
                                                consignmentDetailPresenter.updatePlannedTime(vehicleDetail.getId(), vehicleDetail.getKilometerRunning() + 100);

                                            }

                                        }
                                    });
                                }
                            }, 3000);
                        }
                    }
                } else if (0 < me && me <= 500 && place.getType() == 0) {
                    if (i == 0 && !globalVariable.getPlaceList().contains(places.get(0))) {
                        if (places.size() - 1 > i && places.get(1).getType() == 1) {
                            handler.postDelayed(runnable = new Runnable() {
                                public void run() {
                                    handler.postDelayed(runnable, delay);
                                    btnTracking.setText(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.DELIVERING.getValue()));
                                    btnTracking.setClickable(false);
                                    btnTracking.setBackgroundColor(Color.GRAY);
                                    btnTracking.setTextColor(Color.WHITE);
                                    if (!globalVariable.getPlaceList().contains(places.get(i)) || places.get(i).getActualTime() == null) {
                                        globalVariable.getPlaceList().add(places.get(i));
                                        ListStatusUpdate listStatusUpdate = new ListStatusUpdate();
                                        listStatusUpdate.setVehicle_status(VehicleStatusEnum.RUNNING.getValue());
                                        listStatusUpdate.setConsignment_status(ConsignmentStatusEnum.DELIVERING.getValue());
                                        listStatusUpdate.setDriver_status(DriverStatusEnum.RUNNING.getValue());
                                        detailedSchedulePresenter.updateConsDriVeh(listStatusUpdate, consignmentDetail.getScheduleId());
                                        consignmentDetailPresenter.updateActualTime(places.get(i).getId());
                                    }


                                }
                            }, 3000);

                        }
                    } else if (globalVariable.getPlaceList().size() - 1 == i - 1 && !globalVariable.getPlaceList().contains(places.get(i))) {
                        if (places.size() - 1 > i && places.get(i + 1).getType() == 1) {
                            handler.postDelayed(runnable = new Runnable() {
                                public void run() {
                                    handler.postDelayed(runnable, delay);
                                    btnTracking.setText(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.DELIVERING.getValue()));
                                    btnTracking.setClickable(false);
                                    btnTracking.setBackgroundColor(Color.GRAY);
                                    btnTracking.setTextColor(Color.WHITE);
                                    if (!globalVariable.getPlaceList().contains(places.get(i)) || places.get(i).getActualTime() == null) {
                                        globalVariable.getPlaceList().add(places.get(i));
                                        ListStatusUpdate listStatusUpdate = new ListStatusUpdate();
                                        listStatusUpdate.setVehicle_status(VehicleStatusEnum.RUNNING.getValue());
                                        listStatusUpdate.setConsignment_status(ConsignmentStatusEnum.DELIVERING.getValue());
                                        listStatusUpdate.setDriver_status(DriverStatusEnum.RUNNING.getValue());
                                        detailedSchedulePresenter.updateConsDriVeh(listStatusUpdate, consignmentDetail.getScheduleId());
                                        consignmentDetailPresenter.updateActualTime(places.get(i).getId());
                                        meter = -1;
                                    }
                                }
                            }, 3000);
                        }
                    }
                }

            }
        }
    }

    private final Handler handler1 = new Handler();
    private Runnable runnable1;

    private Location detailLocation(final Location location) throws IOException {
        System.out.println("Cũng vô luôn");
        if(consignmentDetail.getStatus() == ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.COMPLETED.getValue())){
            LatLng destination = new LatLng(R.string.latitudePark,R.string.longitudePark);
            LatLng origin = new LatLng(location.getLatitude(), location.getLongitude());

            int me = 0;
            try {
                String s = new FetchURL(ConsignmentDetailActivity.this, "distancematrix").execute(getUrl(origin, destination, "driving"), "driving").get();
                me = new DistanceParse(ConsignmentDetailActivity.this, "driving").execute(s).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(me <=100){
                consignmentDetailPresenter.stopTracking(vehicleDetail.getId());
            }
        }else{
            if (consignmentDetail.getScheduleId() == globalVariable.getIdSchedule()) {
                Geocoder geocoder = new Geocoder(ConsignmentDetailActivity.this);
                List<Address> address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                String temp = address.get(0).getAddressLine(0);
                com.demo.fmalc_android.entity.Location latLng = new com.demo.fmalc_android.entity.Location();

                if (!locationTemp.equals(temp)) {

                    locationTemp = temp;
                    latLng.setAddress(temp);
                    latLng.setLatitude(location.getLatitude());
                    latLng.setLongitude(location.getLongitude());
                    latLng.setTime("2020-6-10 11:00:00");
                    latLng.setSchedule(id); //id consignment
                    latLng.setAddress(locationTemp);
                    System.out.println("Vooooooooooooooooooooooooooo " + new Date());
                    consignmentDetailPresenter.trackingLocation(latLng);
                    startTie = System.currentTimeMillis();
                } else {
                    if (((System.currentTimeMillis() - startTie) / (interval)) == 1) {
                        Notification notification = new Notification();
                        notification.setVehicle_id(vehicleDetail.getId());
                        notification.setDriver_id(globalVariable.getId());
                        notification.setStatus(true);
                        notification.setType(NotificationTypeEnum.LONG_IDLE_TIMES.getValue());
                        notification.setContent("Xe biển số :" + vehicleDetail.getLicensePlates() + " dừng trong lúc giao hàng quá lâu ở: " + locationTemp);
                        consignmentDetailPresenter.sendNotification(notification);
                        System.out.println("đứng 1 chỗ quá lâu");
                        startTie = System.currentTimeMillis();
                        handler.postDelayed(runnable, delay);
                    }

                }
            }
        }

        return location;
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
            if (consignmentDetail.getStatus() == ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.WAITING.getValue())) {
                btnTracking.setText("Có đơn hàng chưa hoàn thành");
                btnTracking.setClickable(false);
                btnTracking.setBackgroundColor(Color.GRAY);
                btnTracking.setTextColor(Color.WHITE);
            } else if (consignmentDetail.getStatus() == ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.COMPLETED.getValue())) {
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
    public void onTaskDone(Object... values) {
    }

    @Override
    public void onDistanceDone(Integer... meters) {
        this.meter = meters[0];

    }
}