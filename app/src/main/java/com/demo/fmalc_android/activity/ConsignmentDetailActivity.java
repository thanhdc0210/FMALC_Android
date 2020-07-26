package com.demo.fmalc_android.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.fmalc_android.R;
import com.demo.fmalc_android.adapter.ConsignmentTimeStepAdapter;
import com.demo.fmalc_android.contract.ConsignmentDetailContract;
import com.demo.fmalc_android.contract.LocationConsignmentContract;
import com.demo.fmalc_android.entity.ConsignmentDetail;
import com.demo.fmalc_android.entity.Notification;
import com.demo.fmalc_android.entity.Place;
import com.demo.fmalc_android.entity.VehicleDetail;
import com.demo.fmalc_android.presenter.ConsignmentDetailPresenter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;


public class ConsignmentDetailActivity extends AppCompatActivity implements ConsignmentDetailContract.View, View.OnClickListener {

    private ConsignmentDetailPresenter consignmentDetailPresenter;
    private ConsignmentDetail consignmentDetail;

    public static final int DEFAULT_UPDATE_INTERVAL = 10;
    public static final int FAST_UPDATE_INTERVAL = 10;
    private static final int PERMISSIONS_FINE_LOCATION = 99;
    private String locationTemp = "";
    private long startTie = 0;
    private int interval = 1000 * 60 * 1;// 1 phut * 15;

    private VehicleDetail vehicleDetail;

    private Handler handler = new Handler();
    private Runnable runnable;
    private int delay = 10000;

    //location request is config file for all settings related to FusedLocationProviderClient
    private LocationRequest locationRequest;

    private LocationCallback locationCallBack;
    //google's api for location services. the majority of the app functions using this class
    private FusedLocationProviderClient fusedLocationProviderClient;


    private ConsignmentTimeStepAdapter consignmentTimeStepAdapter;
    private List<Place> placeList;

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
    private int id; // id consignment

    public void getPlaceList(List<Place> placeList) {
        this.placeList = placeList;
    }

//    public void getDetail(ConsignmentDetail consignmentDetail) {
//        this.consignmentDetail = consignmentDetail;
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consignment_detail);
        Bundle bundle = getIntent().getExtras();
//Extract the data…
        id = bundle.getInt("consignment_id");
        license = bundle.getString("LICENSE");
        consignmentDetailRecycleView = findViewById(R.id.rvTimeStep);
        txtLicensePlates = findViewById(R.id.txtLicensePlates);
        txtTitleConsignmentNo = findViewById(R.id.txtTitleConsignmentNo);
        btnNote = (ImageButton) findViewById(R.id.btnNote);
        btnTracking = findViewById(R.id.btnUpdateStatus);

        init();
        consignmentDetailPresenter.findByConsignmentId(id);

        consignmentDetailPresenter.getVehicleDetailByLicense(license);

        btnLocationConsignment = findViewById(R.id.btnLocationConsignment);
        btnLocationConsignment.setClickable(false);


        // map

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
                try {
                    detailLocation(locationResult.getLastLocation());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        if (consignmentDetail != null && (consignmentDetail.getStatus().equals("Đang lấy hàng") || consignmentDetail.getStatus().equals("Đang_giao_hàng"))) {
            startLocationUpdates();
        }

        btnTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("CLiCK");
                btnTracking.setText("Đang lấy hàng");
                btnTracking.setClickable(false);
                btnTracking.setBackgroundColor(Color.GRAY);
                btnTracking.setTextColor(Color.WHITE);
                startLocationUpdates();
            }
        }); //end set onclick
        updateGPS();


    }//end oncreate

    private void init() {
        consignmentDetailPresenter = new ConsignmentDetailPresenter();
        consignmentDetailPresenter.setView(this);

    }

    private void tracking() {
        handler.postDelayed(runnable = new Runnable() {
            public void run() {

                handler.postDelayed(runnable, delay);
                startLocationUpdates();
            }
        }, delay);
    }

    private void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
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
                    try {
                        detailLocation(location);
                    } catch (IOException e) {
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
    public void findByConsignmentIdSuccess(ConsignmentDetail consignmentDetail) {
        consignmentTimeStepAdapter = new ConsignmentTimeStepAdapter(consignmentDetail.getPlaces(), this);
        consignmentDetailRecycleView.setAdapter(consignmentTimeStepAdapter);
        consignmentDetailRecycleView.setLayoutManager(new LinearLayoutManager(this));
        txtTitleConsignmentNo.setText("CHI TIẾT DỊCH VỤ " + consignmentDetail.getConsignmentId());
        txtLicensePlates.setText(consignmentDetail.getLicensePlates());

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
        vehicleDetail = responseBody;
        getVehicleDetail(responseBody);
    }

    private VehicleDetail getVehicleDetail(VehicleDetail vehicleDetails) {
        vehicleDetail = vehicleDetails;
        return vehicleDetail;
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
    public void onClick(View v) {
        Intent intent = new Intent(getBaseContext(), LocationConsignmentActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtra("CONSIGNMENT_ID", consignmentDetail.getConsignmentId() + "");
        intent.putExtra("LICENSE_PLATES", consignmentDetail.getLicensePlates());
        intent.putExtra("CONSIGNMENT_STATUS", consignmentDetail.getStatus());
        startActivity(intent);
    }


    private void detailLocation(final Location location) throws IOException {


        Geocoder geocoder = new Geocoder(ConsignmentDetailActivity.this);


        List<Address> address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        String temp = address.get(0).getAddressLine(0);

        if (!locationTemp.equals(temp)) {
            System.out.println(locationTemp + " --- " + temp);
            locationTemp = temp;
            com.demo.fmalc_android.entity.Location latLng = new com.demo.fmalc_android.entity.Location();
            latLng.setLatitude(location.getLatitude());
            latLng.setLongitude(location.getLongitude());
            latLng.setTime("2020-6-10 11:00:00");
            latLng.setConsignment(id); //id consignment
            latLng.setAddress(locationTemp);
            System.out.println(latLng.getLatitude() + "  -  " + latLng.getLongitude() + " - " + latLng.getConsignment() + " - " + latLng.getTime());
            consignmentDetailPresenter.trackingLocation(latLng);
            startTie = System.currentTimeMillis();
        } else {
            if (((System.currentTimeMillis() - startTie) / (interval)) == 1) {
                System.out.println(locationTemp + " --- " + temp);
                Notification notification = new Notification();
                notification.setVehicle_id(vehicleDetail.getId());
                notification.setDriver_id(1);
                notification.setStatus(true);
                notification.setContent("Xe biển số :" + vehicleDetail.getLicensePlates() + " dừng trong lúc giao hàng quá lâu ở: " + locationTemp);
                notification.setTime("2020-6-10 11:00:00");
                consignmentDetailPresenter.sendNotification(notification);
                System.out.println("đứng 1 chỗ quá lâu");
                startTie = System.currentTimeMillis();
            }

        }
    }

}