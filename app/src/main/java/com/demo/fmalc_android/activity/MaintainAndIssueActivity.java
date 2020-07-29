package com.demo.fmalc_android.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.demo.fmalc_android.R;
import com.demo.fmalc_android.fragment.HomeFragment;
import com.demo.fmalc_android.fragment.IssueFragment;
import com.demo.fmalc_android.fragment.MaintainFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


public class MaintainAndIssueActivity extends AppCompatActivity {

    private MaintainFragment maintainFragment;
    private IssueFragment issueFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Báo cáo và Sự cố");
//        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        setContentView(R.layout.activity_report_and_issue);
        ViewPager viewPager = findViewById(R.id.view_pager_maintain_issue);
        TabLayout tabs = findViewById(R.id.tab_layout_maintain_issue);

        tabs.setTabTextColors(ContextCompat.getColor(getApplicationContext(), R.color.colorNormal),
                ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        maintainFragment = new MaintainFragment();
        issueFragment = new IssueFragment();

        tabs.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(issueFragment, "Sự cố");
        viewPagerAdapter.addFragment(maintainFragment, "Bảo trì");
        viewPager.setAdapter(viewPagerAdapter);




    }
    private class ViewPagerAdapter extends FragmentPagerAdapter {

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
}