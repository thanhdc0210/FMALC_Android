package com.demo.fmalc_android.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.Typeface;
import android.os.Bundle;

import com.demo.fmalc_android.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.roboto);
        setContentView(R.layout.activity_driver_home);

    }
}