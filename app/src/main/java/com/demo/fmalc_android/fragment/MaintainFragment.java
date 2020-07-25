package com.demo.fmalc_android.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.fmalc_android.R;
import com.demo.fmalc_android.adapter.IssueAdapter;
import com.demo.fmalc_android.adapter.MaintainAdapter;
import com.demo.fmalc_android.contract.MaintenanceContract;
import com.demo.fmalc_android.entity.MaintainResponse;
import com.demo.fmalc_android.presenter.MaintenancePresenter;
import com.demo.fmalc_android.presenter.ReportIssueResponsePresenter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MaintainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MaintainFragment extends Fragment implements MaintenanceContract.View {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private LinearLayout layoutRecycleView;
    private TextView txtEmptyMaintain;
    private MaintenancePresenter maintenancePresenter;
    private MaintainAdapter maintainAdapter;

    public MaintainFragment() {
        // Required empty public constructor
    }




        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MaintainFragment.
         */
    // TODO: Rename and change types and number of parameters
    public static MaintainFragment newInstance(String param1, String param2) {
        MaintainFragment fragment = new MaintainFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_maintain, container, false);
        layoutRecycleView = view.findViewById(R.id.linearLayoutIssueItem);
        recyclerView = (RecyclerView)  view.findViewById(R.id.recycleViewMaintain);
        txtEmptyMaintain = view.findViewById(R.id.txtEmptyMaintain);
        init();
        //TODO sửa lại truyền id driver
        maintenancePresenter.getMaintenanceList(1);

        return view;
    }

    private void init() {
        maintenancePresenter = new MaintenancePresenter();
        maintenancePresenter.setView(this);
    }

    @Override
    public void getMaintenanceListSuccessful(List<MaintainResponse> maintainResponseList) {
        if (maintainResponseList == null) {
            txtEmptyMaintain.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            maintainAdapter = new MaintainAdapter(maintainResponseList, getActivity());
            recyclerView.setAdapter(maintainAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            txtEmptyMaintain.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void getMaintenanceListFailure(String message) {
        Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateMaintenanceSuccessful(boolean isSuccessful) {

    }

    @Override
    public void updateMaintenanceFailure(String message) {

    }
}