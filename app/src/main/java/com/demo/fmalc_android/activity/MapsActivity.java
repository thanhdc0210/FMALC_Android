package com.demo.fmalc_android.activity;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.demo.fmalc_android.contract.MapContract;
import com.demo.fmalc_android.directionhelpers.FetchURL;
import com.demo.fmalc_android.directionhelpers.TaskLoadedCallback;
import com.demo.fmalc_android.entity.DetailedSchedule;
import com.demo.fmalc_android.entity.GlobalVariable;
import com.demo.fmalc_android.entity.Place;
import com.demo.fmalc_android.presenter.MapPresenter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
 import com.demo.fmalc_android.R;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, MapContract.view , TaskLoadedCallback {
    private int id =0;
    private MapPresenter presenter;
    private GoogleMap mMap;
    private List<String> mUrls = new ArrayList<>();
    private List<Place> places = new ArrayList<>();
    private GlobalVariable globalVariable;

    
//    private GoogleMap googleMap;
private Polyline currentPolyline;
    private MarkerOptions place1, place2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        globalVariable = (GlobalVariable) getApplicationContext();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        Intent intent =getIntent();
//        int id = 0;
        id = intent.getIntExtra("schedule_id",0);
        init();
        presenter.getSchedule(id);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
//        System.out.println(Pla);

        mMap =  googleMap;
//        mMap = new GoogleMap();
        if(places.size()>0){
            for(int i=0; i< places.size(); i++){
                LatLng sydney = new LatLng(places.get(i).getLatitude(),places.get(i).getLongitude());
                Marker marker =  mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));

            }
        }

        // Add a marker in Sydney and move the camera
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(places.get(0).getLatitude(),places.get(0).getLongitude())));
    }

    private void  init(){
        presenter = new MapPresenter();
        presenter.setView(this);
    }
    @Override
    public void getScheduleSuccess(DetailedSchedule detailedSchedule) {
        this.places = detailedSchedule.getPlaces();
//        mMap =  googleMap;
        Collections.sort(places, new Comparator<Place>() {
            @Override
            public int compare(Place o1, Place o2) {
                return o1.getPlannedTime().compareTo(o2.getPlannedTime());
            }
        });
        if(places.size()>0){
//            PolylineOptions rectOptions = new PolylineOptions();
            for(int i=0; i< places.size()-1; i++){

                place1 = new MarkerOptions().position(new LatLng(places.get(i).getLatitude(),places.get(i).getLongitude())).title((i+1)+"."+places.get(i).getAddress());
//                rectOptions.add(sydney);
                place2 = new MarkerOptions().position(new LatLng(places.get(i+1).getLatitude(),places.get(i+1).getLongitude())).title((i+2)+"."+places.get(i+1).getAddress());
                mMap.addMarker(place1);
                mMap.addMarker(place2);
                mUrls.add(getUrl(place1.getPosition(), place2.getPosition(), "driving"));

            }

            if(mUrls.size()>0){
                for (int i = 0; i < mUrls.size(); i++) {
                    String url = mUrls.get(i);
                     new FetchURL(MapsActivity.this,"directions").execute(url, "driving");
                    // Start downloading json data from Google Directions API
//                    downloadTask.execute(url);
                }
            }
//            Polyline polyline = mMap.addPolyline(rectOptions);
            mMap.moveCamera(CameraUpdateFactory.zoomTo(12));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(places.get(0).getLatitude(),places.get(0).getLongitude())));
        }
//        Log.d("mylog", "Added Markers");

//        place1 = new MarkerOptions().position(new LatLng(10.3392645, 107.0923223)).title("Location 1");
//        place2 = new MarkerOptions().position(new LatLng(20.91005119999999, 107.1839024)).title("Location 2");
//        mMap.addMarker(place1);
//        mMap.addMarker(place2);
//        new FetchURL(MapsActivity.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");
    }
    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String key = "AIzaSyBMUeWW7cGPbl14igFrHElHdc27gNJE-n4";
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + key;
//        String url = "https://maps.googleapis.com/maps/api/distancematrix/" + output + "?" + parameters + "&key=" + key;
//        System.out.println(url);
        return url;
    }

    @Override
    public void getScheduleFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

    @Override
    public void onDistanceDone(Integer... meter) {

    }
}