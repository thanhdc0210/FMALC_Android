package com.demo.fmalc_android.fragment;

import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.demo.fmalc_android.R;
import com.demo.fmalc_android.adapter.ScheduleViewCardAdapter;
import com.demo.fmalc_android.contract.ScheduleContract;
import com.demo.fmalc_android.entity.Schedule;
import com.demo.fmalc_android.entity.GlobalVariable;
import com.demo.fmalc_android.paging.ILoadMore;
import com.demo.fmalc_android.presenter.SchedulePresenter;

import java.util.ArrayList;
import java.util.List;

public class PrepareFragment extends Fragment implements ScheduleContract.View {


    RecyclerView consignmentRecyclerView;
    ScheduleViewCardAdapter scheduleViewCardAdapter;
    LinearLayout consignmentRecyclerViewLayout;
    private SchedulePresenter schedulePresenter;
    private GlobalVariable globalVariable;
    List<Schedule> scheduleList = new ArrayList<>();
    List<Schedule> showData = new ArrayList<>();
    private boolean isLoading = false;
    int i = 0, nextLimit = 0;
    private SwipeRefreshLayout swipeRefreshLayout;


    public PrepareFragment() {
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
        status.add(0);
        globalVariable = (GlobalVariable) getActivity().getApplicationContext();
        schedulePresenter.findByConsignmentStatusAndUsername(status, globalVariable.getUsername());

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshList();
            }
        });
        return view;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
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

        getConsignmentList(scheduleList);
        populateData();
        scheduleViewCardAdapter = new ScheduleViewCardAdapter(showData, getActivity());

        consignmentRecyclerView.setAdapter(scheduleViewCardAdapter);
        consignmentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (showData.size() > 1) {
            initScrollListener();
        }
    }

    @Override
    public void findByConsignmentStatusAndUsernameForFailure(String message) {
        Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void populateData() {
        i = 0;
        if (scheduleList.size() < 5){
            showData = scheduleList;
        }else{
            while (i < 5){
                showData.add(scheduleList.get(i));
                i++;
            }
        }
    }

    private void initScrollListener() {
        consignmentRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == showData.size() - 1) {
                        //bottom of list!
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });


    }

    private void loadMore() {
        showData.add(null);
        consignmentRecyclerView.post(new Runnable() {
            public void run() {
                scheduleViewCardAdapter.notifyItemInserted(showData.size()-1);
            }
        });
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showData.remove(showData.size() - 1);
                int scrollPosition = showData.size();
                scheduleViewCardAdapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition+1;
                if (currentSize < scheduleList.size() - 5){
                    nextLimit = currentSize + 5;
                }else{
                    nextLimit = scheduleList.size();
                }

                while (currentSize - 1 < nextLimit) {
                    showData.add(scheduleList.get(currentSize-1));
                    currentSize++;
                }

//                scheduleViewCardAdapter.notifyDataSetChanged();
                consignmentRecyclerView.post(new Runnable() {
                    public void run() {
                        scheduleViewCardAdapter.notifyDataSetChanged();
                    }
                });
                isLoading = false;
            }
        }, 2000);


    }



    private void refreshList(){
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                System.out.println("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
                List<Integer> status = new ArrayList<>();
                status.add(0);
                schedulePresenter.findByConsignmentStatusAndUsername(status, globalVariable.getUsername());
                System.out.println("CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC");
//                consignmentRecyclerView.setAdapter(new Re );
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);

    }
}