package com.demo.fmalc_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        TextView t2 = findViewById(R.id.forgotPassLink);
//        t2.setMovementMethod(LinkMovementMethod.getInstance());
    }
}