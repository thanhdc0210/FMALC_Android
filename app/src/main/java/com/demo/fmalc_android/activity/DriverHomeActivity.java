package com.demo.fmalc_android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.fmalc_android.R;
import com.demo.fmalc_android.fragment.AccountFragment;
import com.demo.fmalc_android.fragment.HomeFragment;
import com.demo.fmalc_android.fragment.InspectionFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import butterknife.BindView;


public class DriverHomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{



    @BindView(R.id.txtConsignmentId)
    TextView codeConsignment;
    @BindView(R.id.txtVehicleInfo)
    TextView vehicleInfor;
    @BindView(R.id.card_view_item)
    LinearLayout cardView;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home);
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        //default là navigation Home, có sửa lại để test fragment khác
        bottomNavigationView.setSelectedItemId(R.id.navigation_inspection);

        // Set up firebase cloud messaging
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        System.out.println("tokennnnnnnnnnnnnnnnnnnnnnnnnn " + token);
                    }
                });
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Intent intent;
        switch (item.getItemId()){
            case R.id.navigation_home:
                item.setChecked(true);
                HomeFragment fgm= new HomeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fgm).detach(fgm).attach(fgm).commit();
                break;
            case R.id.navigation_inspection:
                item.setChecked(true);
                getSupportFragmentManager().beginTransaction().replace(R.id.container,new InspectionFragment()).commit();
                break;
            case  R.id.navigation_noti:
                item.setChecked(true);
                getSupportFragmentManager().beginTransaction().replace(R.id.container,new InspectionFragment()).commit();
                break;
            case R.id.navigation_search:
                item.setChecked(true);
                break;
            case R.id.navigation_account:
                item.setChecked(true);
                getSupportFragmentManager().beginTransaction().replace(R.id.container,new AccountFragment()).commit();
                break;
            default:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
        }

        return false;
    }



}