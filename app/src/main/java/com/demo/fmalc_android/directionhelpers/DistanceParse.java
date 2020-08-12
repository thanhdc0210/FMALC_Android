package com.demo.fmalc_android.directionhelpers;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DistanceParse extends AsyncTask<String, Integer,Integer> {
    TaskLoadedCallback taskCallback;
    String directionMode = "driving";

    public DistanceParse(Context mContext, String directionMode) {
        this.taskCallback = (TaskLoadedCallback) mContext;
        this.directionMode = directionMode;
    }

    @Override
    protected Integer doInBackground(String... strings) {
        JSONObject jObject;
        Integer meter =-1;

        try {
            jObject = new JSONObject(strings[0]);
//            Log.d("mylog", jsonData[0].toString());
            DataParser parser = new DataParser();
//            Log.d("mylog", parser.toString());
            // Starts parsing data
            meter = parser.distance(jObject);
//            Log.d("mylog", "Executing routes");
//            Log.d("mylog", routes.toString());

        } catch (Exception e) {
//            Log.d("mylog", e.toString());
            e.printStackTrace();
        }
        return meter;
    }

    @Override
    protected void onPostExecute(Integer meters) {

            taskCallback.onDistanceDone(meters);

    }

}
