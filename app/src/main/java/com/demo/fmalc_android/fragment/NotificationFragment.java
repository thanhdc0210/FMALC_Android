package com.demo.fmalc_android.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.demo.fmalc_android.R;
import com.demo.fmalc_android.adapter.CompletedScheduleViewCardAdapter;
import com.demo.fmalc_android.adapter.NotificationViewCardAdapter;
import com.demo.fmalc_android.contract.NotificationMobileContract;
import com.demo.fmalc_android.entity.GlobalVariable;
import com.demo.fmalc_android.entity.NotificationMobileResponse;
import com.demo.fmalc_android.presenter.NotificationMobilePresenter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends Fragment implements NotificationMobileContract.View {

    RecyclerView notificationRecyclerView;
    NotificationViewCardAdapter notificationViewCardAdapter;
    LinearLayout notificationLinearLayout;
    private GlobalVariable globalVariable;
    List<NotificationMobileResponse> notificationMobileResponses = new ArrayList<>();
    List<NotificationMobileResponse> showData = new ArrayList<>();
    private boolean isLoading = false;
    int i = 0, nextLimit = 0;
    NotificationMobilePresenter notificationMobilePresenter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        notificationLinearLayout = view.findViewById(R.id.card_view_item_notification);
        notificationRecyclerView = view.findViewById(R.id.rvNotification);
        globalVariable = (GlobalVariable) getActivity().getApplicationContext();

        init();

        notificationMobilePresenter.findNotificationByUsername(globalVariable.getUsername(), globalVariable.getToken());

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayoutNotification);
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

    private void getNotificationMobileResponseList(List<NotificationMobileResponse> notificationMobileResponses){
        this.notificationMobileResponses = notificationMobileResponses;
        this.notificationMobileResponses.sort(Comparator.comparing(a-> a.getTime(),Comparator.nullsLast(Comparator.naturalOrder())));
        Collections.reverse(notificationMobileResponses);
    }

    void init(){
        notificationMobilePresenter = new NotificationMobilePresenter();
        notificationMobilePresenter.setView(this);
    };

    @Override
    public void findNotificationByUsernameSuccess(List<NotificationMobileResponse> notificationMobileResponses) {
        if ((notificationMobileResponses.size()>0)) {
            getNotificationMobileResponseList(notificationMobileResponses);
            populateData();

            notificationViewCardAdapter = new NotificationViewCardAdapter(showData, getActivity(), globalVariable.getToken(), globalVariable.getId());
            notificationRecyclerView.setAdapter(notificationViewCardAdapter);
            notificationRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            if (showData.size() > 9) {
                initScrollListener();
            }
        }
    }

    @Override
    public void findNotificationByUsernameFailure(String message) {
        Toast.makeText(this.getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateStatusSuccess() {}

    public void takeDayOffSuccess(boolean status) {
    }

    @Override
    public void updateStatusFailure(String message) {}

    public void takeDayOffFailure(String message) {
    }

    private void populateData() {
        i = 0;
        if (notificationMobileResponses.size() < 10){
            showData = notificationMobileResponses;
        }else{
            while (i < 10){
                showData.add(notificationMobileResponses.get(i));
                i++;
            }
        }
    }

    private void initScrollListener() {
        notificationRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        notificationRecyclerView.post(new Runnable() {
            public void run() {
                notificationViewCardAdapter.notifyItemInserted(showData.size()-1);
            }
        });
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showData.remove(showData.size() - 1);
                int scrollPosition = showData.size();
                notificationViewCardAdapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition+1;
                if (currentSize < notificationMobileResponses.size() - 4){
                    nextLimit = currentSize + 4;
                }else{
                    nextLimit = notificationMobileResponses.size();
                }

                while (currentSize - 1 < nextLimit) {
                    showData.add(notificationMobileResponses.get(currentSize-1));
                    currentSize++;
                }

//                scheduleViewCardAdapter.notifyDataSetChanged();
                notificationRecyclerView.post(new Runnable() {
                    public void run() {
                        notificationViewCardAdapter.notifyDataSetChanged();
                    }
                });
                isLoading = false;
            }
        }, 800);


    }



    private void refreshList(){
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                notificationMobileResponses.clear();
                showData.clear();
                List<NotificationMobileResponse> notificationMobileResponses = new ArrayList<>();
                List<NotificationMobileResponse> showData = new ArrayList<>();
                notificationMobilePresenter.findNotificationByUsername(globalVariable.getUsername(), globalVariable.getToken());
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 800);
    }
}