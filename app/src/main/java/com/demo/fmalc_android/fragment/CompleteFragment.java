package com.demo.fmalc_android.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.demo.fmalc_android.R;
import com.demo.fmalc_android.adapter.CompletedScheduleViewCardAdapter;
import com.demo.fmalc_android.contract.ScheduleContract;
import com.demo.fmalc_android.entity.Schedule;
import com.demo.fmalc_android.entity.GlobalVariable;
import com.demo.fmalc_android.presenter.SchedulePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompleteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompleteFragment extends Fragment implements ScheduleContract.View {

    RecyclerView consignmentRecyclerView;
    LinearLayout consignmentRecyclerViewLayout;
    CompletedScheduleViewCardAdapter completedScheduleViewCardAdapter;
    private SchedulePresenter schedulePresenter;
    private GlobalVariable globalVariable;
    List<Schedule> scheduleList;

    public CompleteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_prepare, container, false);

        init();

        // Adapter init and setup

        consignmentRecyclerViewLayout = view.findViewById(R.id.card_view_item);
        consignmentRecyclerView = (RecyclerView)  view.findViewById(R.id.rvConsignment);
        List<Integer> status = new ArrayList<>();
        status.add(3);
        status.add(5);
        globalVariable = (GlobalVariable) getActivity().getApplicationContext();
        schedulePresenter.findByConsignmentStatusAndUsername(status, "driver2");
        return view;

    }

    private void init(){
        schedulePresenter = new SchedulePresenter();
        schedulePresenter.setView(this);
    }

    private void getConsignmentList(List<Schedule> scheduleList){
        this.scheduleList = scheduleList;
    }

    @Override
    public void findByConsignmentStatusAndUsernameForSuccess(List<Schedule> scheduleList) {

        completedScheduleViewCardAdapter = new CompletedScheduleViewCardAdapter(scheduleList, getActivity());

        consignmentRecyclerView.setAdapter(completedScheduleViewCardAdapter);
        consignmentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getConsignmentList(scheduleList);
    }

    @Override
    public void findByConsignmentStatusAndUsernameForFailure(String message) {
        Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
    }

}