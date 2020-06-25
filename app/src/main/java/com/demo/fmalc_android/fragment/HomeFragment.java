package com.demo.fmalc_android.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.fmalc_android.R;
import com.demo.fmalc_android.activity.DriverHomeActivity;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private PrepareFragment prepareFragment ;
    private WorkingFragment workingFragment;
    private CompleteFragment completeFragment;

    private boolean isVisible;
    private boolean isStarted;


    @BindView(R.id.txtConsignmentId)
    TextView codeConsignment;
    @BindView(R.id.txtVehicleInfo)
    TextView vehicleInfor;
    @BindView(R.id.card_view_item)
    LinearLayout cardView;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


//    @Override
//    protected void onResume() {
//        super.onResume();
//        HomeFragment fgm= new HomeFragment();
//        getSupportFragmentManager().beginTransaction().replace(R.id.container, fgm).detach(fgm).attach(fgm).commit();
//    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        prepareFragment = new PrepareFragment();
//        getFragmentManager().beginTransaction().detach(prepareFragment).attach(prepareFragment).commit();
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        System.out.println("RELOADDDED");
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        viewPager =(ViewPager) view.findViewById(R.id.view_pager);
        tabLayout = view.findViewById(R.id.tab_layout);

        prepareFragment =  new PrepareFragment();
        workingFragment = new WorkingFragment();
        completeFragment = new CompleteFragment();

        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new  ViewPagerAdapter(getFragmentManager());
        viewPagerAdapter.addFragment(prepareFragment, "Chuẩn bị");
        viewPagerAdapter.addFragment(workingFragment, "Đang làm");
        viewPagerAdapter.addFragment(completeFragment, "Hoàn thành");
        viewPager.setAdapter(viewPagerAdapter);



        BadgeDrawable badgeDrawable = tabLayout.getTabAt(0).getOrCreateBadge();
        badgeDrawable.setVisible(true);
        //set tổng số cho tab đó
        badgeDrawable.setNumber(12);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        isStarted = true;
        if (isVisible)
            getFragmentManager().beginTransaction().detach(prepareFragment).attach(prepareFragment).commit();
    }

    @Override
    public void onStop() {
        super.onStop();
        isStarted = false;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        if (isVisible && isStarted)
            getFragmentManager().beginTransaction().detach(prepareFragment).attach(prepareFragment).commit();
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
