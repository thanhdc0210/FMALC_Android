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
import com.demo.fmalc_android.adapter.ConsignmentViewCardAdapter;
import com.demo.fmalc_android.contract.ConsignmentContract;
import com.demo.fmalc_android.entity.Consignment;
import com.demo.fmalc_android.entity.StatusRequest;
import com.demo.fmalc_android.presenter.ConsignmentPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PrepareFragment extends Fragment implements ConsignmentContract.View {


    RecyclerView consignmentRecyclerView;
    LinearLayout consignmentRecyclerViewLayout;
    ConsignmentViewCardAdapter consignmentViewCardAdapter;
    private ConsignmentPresenter consignmentPresenter;
    private String token;
    private StatusRequest statusRequest;
    List<Consignment> consignmentList;

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
        consignmentPresenter.findByConsignmentStatusAndUsernameForFleetManager(status, "manager1");
        return view;

    }

    private void init(){
        consignmentPresenter = new ConsignmentPresenter();
        consignmentPresenter.setView(this);
    }

    private void getConsignmentList(List<Consignment> consignmentList){
        this.consignmentList = consignmentList;
    }

    @Override
    public void findByConsignmentStatusAndUsernameForFleetManagerSuccess(List<Consignment> consignmentList) {

        consignmentViewCardAdapter = new ConsignmentViewCardAdapter(consignmentList, getActivity());

        consignmentRecyclerView.setAdapter(consignmentViewCardAdapter);
        consignmentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getConsignmentList(consignmentList);
    }

    @Override
    public void findByConsignmentStatusAndUsernameForFleetManagerFailure(String message) {
        Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
    }

}