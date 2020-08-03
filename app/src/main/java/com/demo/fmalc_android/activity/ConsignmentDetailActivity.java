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
import java.util.List;

import butterknife.BindView;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;


public class ConsignmentDetailActivity extends AppCompatActivity implements TaskLoadedCallback, DetailedScheduleContract.View, ConsignmentDetailContract.View {

    private ConsignmentDetailPresenter consignmentDetailPresenter;
    private DetailedSchedule consignmentDetail;

    public static final int DEFAULT_UPDATE_INTERVAL = 10;
    public static final int FAST_UPDATE_INTERVAL = 10;
    private static final int PERMISSIONS_FINE_LOCATION = 1;
    private String locationTemp = "";
    private long startTie = 0;
    private int interval = 1000 * 60 * 1;// 1 phut * 15;
    //    private GlobalPlace globalPlace;
    private VehicleDetail vehicleDetail;
    private Integer numberConsignment;
    private Handler handler = new Handler();
    private Runnable runnable;
    private int delay = 10000;
    private GlobalVariable globalVariable;
    //location request is config file for all settings related to FusedLocationProviderClient
    private LocationRequest locationRequest;
    private Integer meter;
    private LocationCallback locationCallBack;
    //google's api for location services. the majority of the app functions using this class
    private FusedLocationProviderClient fusedLocationProviderClient;


    private ScheduleTimeStepAdapter consignmentTimeStepAdapter;
    private List<Place> placeList;


//    public class ConsignmentDetailActivity extends AppCompatActivity implements DetailedScheduleContract.View, View.OnClickListener {

    private DetailedSchedulePresenter detailedSchedulePresenter;
    private DetailedSchedule detailedSchedule;
    ScheduleTimeStepAdapter scheduleTimeStepAdapter;
//        List<Place> placeList;


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
//        statusCheck();
        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("consignment_id");
        consignmentDetailRecycleView = findViewById(R.id.rvTimeStep);
        txtLicensePlates = findViewById(R.id.txtLicensePlates);
        txtTitleConsignmentNo = findViewById(R.id.txtTitleConsignmentNo);
        btnNote = (ImageButton) findViewById(R.id.btnNote);
        btnTracking = findViewById(R.id.btnUpdateStatus);
        meter = -1;
        init();
//        globalPlace = (GlobalPlace) getApplicationContext();
        globalVariable = (GlobalVariable) getApplicationContext();

        detailedSchedulePresenter.findByScheduleId(id);

        btnLocationConsignment = findViewById(R.id.btnLocationConsignment);
//        btnLocationConsignment.setClickable(false);


        // map

        locationRequest = new LocationRequest();

        // how often does the default location check occur ?

        locationRequest.setInterval(1000 * DEFAULT_UPDATE_INTERVAL);
        // how often does the location check occur when set to the most frequent update
        locationRequest.setFastestInterval(1000 * FAST_UPDATE_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


//        updateGPS();
        locationCallBack = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                //save the location
                startLocationUpdates();
//                updateGPS();
            }
        };


        btnLocationConsignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("SCHEDULE_ID", id);
                startActivity(intent);
            }
        });
//
        btnTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    if (numberConsignment == 0) {
                if (ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.WAITING.getValue()).equals(consignmentDetail.getStatus())) {
                    System.out.println(consignmentDetail.getStatus());
                    ListStatusUpdate listStatusUpdate = new ListStatusUpdate();
                    listStatusUpdate.setVehicle_status(VehicleStatusEnum.RUNNING.getValue());
                    listStatusUpdate.setConsignment_status(ConsignmentStatusEnum.OBTAINING.getValue());
                    listStatusUpdate.setDriver_status(DriverStatusEnum.RUNNING.getValue());
                    detailedSchedulePresenter.updateConsDriVeh(listStatusUpdate, consignmentDetail.getScheduleId());
                    globalVariable.setIdSchedule(consignmentDetail.getScheduleId());
                    List<Place> places = new ArrayList<>();
                    globalVariable.setPlaceList(places);
                    globalVariable.setIdSchedule(consignmentDetail.getScheduleId());
                    updateGPS();
                    tracking();
                } else {
//                    System.out.println(consignmentDetail.getStatus());

                }
            }


//                }
        });

    }//end oncreate

    private void init() {
        detailedSchedulePresenter = new DetailedSchedulePresenter();
        detailedSchedulePresenter.setView(this);
        consignmentDetailPresenter = new ConsignmentDetailPresenter();
        consignmentDetailPresenter.setView(this);

    }

    private void tracking() {
        handler.postDelayed(runnable = new Runnable() {
            public void run() {

                handler.postDelayed(runnable, delay);
                startLocationUpdates();
            }
        }, 10000);
    }

    private void startLocationUpdates() {
//        System.out.println("vô nè");
//        updateGPS();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_FINE_LOCATION
            );
//               public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                                      int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallBack, null);
        updateGPS();

    }

    private void updateGPS() {
        //get permission from user to track GPS
        // get the current location from the fused client
        // update the UI - i.e. set all properties in their associate text view items.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(ConsignmentDetailActivity.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // we got permission. Put the values of location. XXX into the UI components.
//                    Bundle bundle = getIntent().getExtras();
//                    id = bundle.getInt("consignment_id");
//                    detailedSchedulePresenter.findByScheduleId(id);
                    try {
//                        if(id>0){


                        handler.postDelayed(runnable = new Runnable() {
                            public void run() {

                                handler.postDelayed(runnable, delay);
                                try {
                                    duration(location);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, 10000);
//                            startLocationUpdates();
//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            //permission not granted yet.

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
//        txtTitleConsignmentNo.setText(consignmentDetail.getConsignmentId()+"");
//        txtLicensePlates.setText(consignmentDetail.getLicensePlates());
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
        if ((consignmentDetail.getStatus().equals(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.DELIVERING.getValue()))
                || consignmentDetail.getStatus().equals(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.OBTAINING.getValue())))) {

            updateGPS();

            tracking();
        } else if (consignmentDetail.getStatus().equals(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.COMPLETED.getValue()))) {

        }
//        updateGPS();

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

//        private VehicleDetail getVehicleDetail (VehicleDetail vehicleDetails){
//            vehicleDetail = vehicleDetails;
//            return vehicleDetail;
//        }

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


    private void duration(Location location) throws IOException {
        Place place = new Place();
        List<Place> placesDeli = new ArrayList<>();
        for (int i = 0; i < consignmentDetail.getPlaces().size(); i++) {
            if (consignmentDetail.getPlaces().get(i).getType() == 1) {
                placesDeli.add(consignmentDetail.getPlaces().get(i));
            }
        }
        List<Place> places = consignmentDetail.getPlaces();
//            List<Place> places = detailedSchedule.getPlaces();
        if (places.size() > 0 && globalVariable.getPlaceList() != null) {
            Collections.sort(places, new Comparator<Place>() {
                @Override
                public int compare(Place o1, Place o2) {
                    return o1.getPlannedTime().compareTo(o2.getPlannedTime());
                }
            });
//           int i = 0;

            int i = globalVariable.getPlaceList().size();

            if (i < places.size()) {
                place = places.get(i);


            } else {
                System.out.println("NULLLLLLLLLLLLLLLLLLLL");
            }
            if (place.getType() != null && meter != null && 0 < meter) {
                LatLng origin = new LatLng(location.getLatitude(), location.getLongitude());
                LatLng destination = new LatLng(place.getLatitude(), place.getLongitude());
                meter =-1;
                new FetchURL(ConsignmentDetailActivity.this, "distancematrix").execute(getUrl(origin, destination, "driving"), "driving");
//                if (placeId == null && i == 0) {
                if (place.getType() == 1) {
                    System.out.println(places.size() + "TYPEEEEEEEEEEEE " + globalVariable.getPlaceList().size());
                    if (i == 0 && meter <= 500 && !globalVariable.getPlaceList().contains(places.get(i))) {
                        System.out.println(places.size() + "SIZEEEEEEEEEEEEEEE " + globalVariable.getPlaceList().size());
                        if (i < places.size() - 1 && places.get(i + 1).getType() == 0) {
                            meter = -1;
                            btnTracking.setText("MMMM");
                            btnTracking.setClickable(false);
                            btnTracking.setBackgroundColor(Color.GRAY);
                            btnTracking.setTextColor(Color.WHITE);
                            detailLocation(location);
                            globalVariable.getPlaceList().add(places.get(i));
                            meter = -1;
                        }
                    } else if (meter > 0 && meter <= 500 && !globalVariable.getPlaceList().contains(places.get(i))) {
                        if (i < places.size() - 1 && places.get(i + 1).getType() == 0) {

                            meter = -1;
                            handler.postDelayed(runnable = new Runnable() {

                                public void run() {

                                    btnTracking.setText("LLLLL");
                                    btnTracking.setClickable(false);
                                    btnTracking.setBackgroundColor(Color.GRAY);
                                    btnTracking.setTextColor(Color.WHITE);
//                                    detailLocation(location);
                                    globalVariable.getPlaceList().add(places.get(i));
                                    meter = -1;
                                    try {
                                        detailLocation(location);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    if (!globalVariable.getPlaceList().contains(places.get(i))) {
                                        globalVariable.getPlaceList().add(places.get(places.size() - 1));
                                        ListStatusUpdate listStatusUpdate = new ListStatusUpdate();
                                        listStatusUpdate.setVehicle_status(VehicleStatusEnum.RUNNING.getValue());
                                        listStatusUpdate.setConsignment_status(ConsignmentStatusEnum.OBTAINING.getValue());
                                        listStatusUpdate.setDriver_status(DriverStatusEnum.RUNNING.getValue());
                                        detailedSchedulePresenter.updateConsDriVeh(listStatusUpdate, consignmentDetail.getScheduleId());
                                        consignmentDetailPresenter.updateActualTime(places.get(i).getId());
                                    }

                                }
                            }, 5000);


                        } else if (meter > 0 && meter <= 500 && i == places.size() - 1 && !globalVariable.getPlaceList().contains(places.get(i))) {
                            handler.postDelayed(runnable = new Runnable() {
                                public void run() {
                                    meter = -1;
                                    btnTracking.setText(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.COMPLETED.getValue()));
                                    btnTracking.setClickable(true);
                                    btnTracking.setBackgroundColor(Color.rgb(91, 202, 96));
                                    btnTracking.setTextColor(Color.WHITE);
//                                    globalVariable.getPlaceList().add(places.get(i));
                                    System.out.println(places.size() + "SIZEEEEEEEEEEEEEEE " + globalVariable.getPlaceList().size());

                                    try {
                                        detailLocation(location);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    btnTracking.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            EditText edittext = new EditText(getApplicationContext());
                                            edittext.setInputType(InputType.TYPE_CLASS_NUMBER);
                                            edittext.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                                            MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(getApplicationContext(), R.style.AlertDialog)
                                                    .setTitle("Kết thúc lộ trình")
                                                    .setMessage("Nhập số km trước khi kết thúc")
                                                    .setView(edittext)
                                                    .setPositiveButton("Yes ", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            String youEditTextValue = edittext.getText().toString();
                                                            //OR
                                                        }
                                                    })
                                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int whichButton) {
                                                            // what ever you want to do with No option.
                                                        }
                                                    });


                                            if (!globalVariable.getPlaceList().contains(places.get(i))) {
                                                globalVariable.getPlaceList().add(places.get(places.size() - 1));
                                                ListStatusUpdate listStatusUpdate = new ListStatusUpdate();
                                                listStatusUpdate.setVehicle_status(VehicleStatusEnum.AVAILABLE.getValue());
                                                listStatusUpdate.setConsignment_status(ConsignmentStatusEnum.COMPLETED.getValue());
                                                listStatusUpdate.setDriver_status(DriverStatusEnum.AVAILABLE.getValue());
                                                detailedSchedulePresenter.updateConsDriVeh(listStatusUpdate, consignmentDetail.getScheduleId());
//                                                consignmentDetailPresenter.updateActualTime(places.get(i).getId());
                                            }

                                        }
                                    });
                                }
                            }, 5000);


                        }
                    }
                } else if (0 < meter && meter <= 500 && place.getType() == 0) {
                    System.out.println(places.size() + "ELSEEEEEEEEEEEEEEEEE " + globalVariable.getPlaceList().size());
                    if (i == 0 && !globalVariable.getPlaceList().contains(places.get(0))) {
                        System.out.println(places.size() + "LLLLLLLLLLLLLLLL " + globalVariable.getPlaceList().size());
                        if (places.size() - 1 > i && places.get(1).getType() == 1) {
                            System.out.println(places.size() + " MMMMMMMMMMMME " + globalVariable.getPlaceList().size());
                            meter = -1;
                            handler.postDelayed(runnable = new Runnable() {
                                public void run() {

                                    handler.postDelayed(runnable, delay);
                                    btnTracking.setText(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.DELIVERING.getValue()));
                                    btnTracking.setClickable(false);
                                    btnTracking.setBackgroundColor(Color.GRAY);
                                    btnTracking.setTextColor(Color.WHITE);

                                    try {
                                        detailLocation(location);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    if (!globalVariable.getPlaceList().contains(places.get(i))) {
                                        globalVariable.getPlaceList().add(places.get(i));
                                        ListStatusUpdate listStatusUpdate = new ListStatusUpdate();
                                        listStatusUpdate.setVehicle_status(VehicleStatusEnum.RUNNING.getValue());
                                        listStatusUpdate.setConsignment_status(ConsignmentStatusEnum.DELIVERING.getValue());
                                        listStatusUpdate.setDriver_status(DriverStatusEnum.RUNNING.getValue());
                                        detailedSchedulePresenter.updateConsDriVeh(listStatusUpdate, consignmentDetail.getScheduleId());
//                                        consignmentDetailPresenter.updateActualTime(places.get(i).getId());
                                    }


                                }
                            }, 5000);

                        }
                    } else if (globalVariable.getPlaceList().size() - 1 == i - 1 && !globalVariable.getPlaceList().contains(places.get(i))) {
                        if (places.size() - 1 > i && places.get(i + 1).getType() == 1) {
                            meter = -1;
                            handler.postDelayed(runnable = new Runnable() {
                                public void run() {

                                    handler.postDelayed(runnable, delay);
                                    btnTracking.setText(ConsignmentStatusEnum.getValueEnumToShow(ConsignmentStatusEnum.DELIVERING.getValue()));
                                    btnTracking.setClickable(false);
                                    btnTracking.setBackgroundColor(Color.GRAY);
                                    btnTracking.setTextColor(Color.WHITE);

                                    try {
                                        detailLocation(location);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    if (!globalVariable.getPlaceList().contains(places.get(i))) {
                                        globalVariable.getPlaceList().add(places.get(i));
                                        ListStatusUpdate listStatusUpdate = new ListStatusUpdate();
                                        listStatusUpdate.setVehicle_status(VehicleStatusEnum.RUNNING.getValue());
                                        listStatusUpdate.setConsignment_status(ConsignmentStatusEnum.DELIVERING.getValue());
                                        listStatusUpdate.setDriver_status(DriverStatusEnum.RUNNING.getValue());
                                        detailedSchedulePresenter.updateConsDriVeh(listStatusUpdate, consignmentDetail.getScheduleId());
//                                        consignmentDetailPresenter.updateActualTime(places.get(i).getId());
                                    }


                                }
                            }, 5000);
                        }
                    }
                }

            }
        }
    }

    private Location detailLocation(final Location location) throws IOException {


//            System.out.println( "AAAAAAA");
        if (consignmentDetail.getScheduleId() == globalVariable.getIdSchedule()) {
            Geocoder geocoder = new Geocoder(ConsignmentDetailActivity.this);

//            duration(location);

            List<Address> address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String temp = address.get(0).getAddressLine(0);
//            System.out.println("-------------" + id + "-----------------------");
            if (!locationTemp.equals(temp)) {
                System.out.println(locationTemp + " --- " + temp);
                locationTemp = temp;
                com.demo.fmalc_android.entity.Location latLng = new com.demo.fmalc_android.entity.Location();
                latLng.setLatitude(location.getLatitude());
                latLng.setLongitude(location.getLongitude());
                latLng.setTime("2020-6-10 11:00:00");
                latLng.setSchedule(id); //id consignment
                latLng.setAddress(locationTemp);
                System.out.println(latLng.getLatitude() + "  -  " + latLng.getLongitude() + " - " + latLng.getSchedule() + " - " + latLng.getTime());
                consignmentDetailPresenter.trackingLocation(latLng);
                startTie = System.currentTimeMillis();
            } else {
                if (((System.currentTimeMillis() - startTie) / (interval)) == 1) {
                    System.out.println(locationTemp + " --- " + temp);
                    Notification notification = new Notification();
                    notification.setVehicle_id(vehicleDetail.getId());


                    notification.setDriver_id(globalVariable.getId());
                    notification.setStatus(true);
                    notification.setType(NotificationTypeEnum.LONG_IDLE_TIMES.getValue());
                    notification.setContent("Xe biển số :" + vehicleDetail.getLicensePlates() + " dừng trong lúc giao hàng quá lâu ở: " + locationTemp);
//                    notification.setTime("2020-6-10 11:00:00");
                    consignmentDetailPresenter.sendNotification(notification);
                    System.out.println("đứng 1 chỗ quá lâu");
                    startTie = System.currentTimeMillis();
                }

            }
        }
        return location;
    }


    @Override
    public void updateConsDriVehSuccess(ListStatusUpdate statusUpdate) {
//        System.out.println("CLiCK");
        if (statusUpdate.getConsignment_status() == ConsignmentStatusEnum.COMPLETED.getValue()) {
            btnTracking.setText(ConsignmentStatusEnum.getValueEnumToShow(statusUpdate.getConsignment_status()));
//            btnTracking.setClickable(false);
//            btnTracking.setBackgroundColor(Color.GRAY);
//            btnTracking.setTextColor(Color.WHITE);
//            statusCheck();
        } else if (statusUpdate.getConsignment_status() == ConsignmentStatusEnum.DELIVERING.getValue()) {
            btnTracking.setText(ConsignmentStatusEnum.getValueEnumToShow(statusUpdate.getConsignment_status()));
//            btnTracking.setClickable(false);
//            btnTracking.setBackgroundColor(Color.GRAY);
//            btnTracking.setTextColor(Color.WHITE);
//            statusCheck();
        } else if (statusUpdate.getConsignment_status() == ConsignmentStatusEnum.OBTAINING.getValue()) {
            btnTracking.setText(ConsignmentStatusEnum.getValueEnumToShow(statusUpdate.getConsignment_status()));

        }

        btnTracking.setClickable(false);
        btnTracking.setBackgroundColor(Color.GRAY);
        btnTracking.setTextColor(Color.WHITE);

//        statusCheck();
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

//        Toast.makeText(globalVariable, "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDistanceDone(Integer... meters) {
        this.meter = -1;


        this.meter = meters[0];

        System.out.println("VALUEEEEEEEEEEEEEEEEEE::::::::::::::::" + meters[0]);
    }
}