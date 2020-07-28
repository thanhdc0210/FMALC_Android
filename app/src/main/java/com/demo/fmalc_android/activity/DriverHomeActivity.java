package com.demo.fmalc_android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.fmalc_android.R;
import com.demo.fmalc_android.fragment.AccountFragment;
import com.demo.fmalc_android.fragment.HomeFragment;
import com.demo.fmalc_android.fragment.InspectionFragment;
import com.demo.fmalc_android.fragment.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
                getSupportFragmentManager().beginTransaction().replace(R.id.container,new SearchFragment()).commit();
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