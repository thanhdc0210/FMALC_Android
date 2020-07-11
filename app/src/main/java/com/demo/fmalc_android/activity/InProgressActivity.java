package com.demo.fmalc_android.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.demo.fmalc_android.R;

public class InProgressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Báo cáo khi đang chạy");
        setContentView(R.layout.activity_in_progress);
    }
}