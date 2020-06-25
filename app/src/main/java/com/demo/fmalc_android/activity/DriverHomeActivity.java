package com.demo.fmalc_android.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.fmalc_android.R;
import com.demo.fmalc_android.fragment.CompleteFragment;
import com.demo.fmalc_android.fragment.HomeFragment;
import com.demo.fmalc_android.fragment.InspectionFragment;
import com.demo.fmalc_android.fragment.PrepareFragment;
import com.demo.fmalc_android.fragment.WorkingFragment;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class DriverHomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;



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
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);

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
                break;
            default:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
        }

        return false;
    }




    public class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmentTitle = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitle.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }
    }

    @OnClick(R.id.card_view_item)
    public void onClickViewDetail(View view) {
//        Toast.makeText(DriverHomeActivity.this, "clmmmm", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(DriverHomeActivity.this, ConsignmentDetailActivity.class);


        TextView user = (TextView) view.findViewById(R.id.txtConsignmentId);

//        user.setText("sadsahd");
//        Toast.makeText(DriverHomeActivity.this, user.getText().toString(), Toast.LENGTH_SHORT).show();
        intent.putExtra("message", user.getText().toString());
        //Create the bundle
        Bundle bundle = new Bundle();
//Add your data from getFactualResults method to bundle
        bundle.putString("giangg", user.getText().toString());
//Add the bundle to the intent
        intent.putExtras(bundle);

        startActivity(intent);
//        new DriverHomeActivity().getDataFromPrepareFragment();
    }



    public void getDataFromPrepareFragment(){
        Intent intent = getIntent();
        String message = intent.getStringExtra("message");

        Intent intent2 = new Intent(DriverHomeActivity.this, ConsignmentDetailActivity.class);
        intent.putExtra("message", message);

    }
}