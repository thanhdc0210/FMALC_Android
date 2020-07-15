package com.demo.fmalc_android.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.fmalc_android.R;
import com.demo.fmalc_android.adapter.IssueAdapter;
import com.demo.fmalc_android.contract.ReportIssueForUpdatingContract;
import com.demo.fmalc_android.contract.ReportIssueResponseContract;
import com.demo.fmalc_android.entity.GlobalVariable;
import com.demo.fmalc_android.entity.ReportIssueContentResponse;
import com.demo.fmalc_android.entity.ReportIssueInformationForUpdating;
import com.demo.fmalc_android.entity.ReportIssueRequest;
import com.demo.fmalc_android.entity.ReportIssueResponse;
import com.demo.fmalc_android.presenter.ReportIssueForUpdatingPresenter;
import com.demo.fmalc_android.presenter.ReportIssueResponsePresenter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IssueFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IssueFragment extends Fragment implements ReportIssueResponseContract.View, ReportIssueForUpdatingContract.View {

    RecyclerView issueInformationRecyclerView;
    LinearLayout issueInformationRecyclerViewLayout;
    IssueAdapter issueAdapter;
    private ReportIssueResponsePresenter reportIssueResponsePresenter;
    private ReportIssueForUpdatingPresenter reportIssueForUpdatingPresenter;
    private GlobalVariable globalVariable;
    ReportIssueResponse reportIssueResponse;
    List<ReportIssueContentResponse> reportIssueContentResponseList;
    TextView txtCurrentLicensePlate;
    Button btnUpdateReportIssue;

    public IssueFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_issue, container, false);

        init();

        txtCurrentLicensePlate = view.findViewById(R.id.txtCurrentLicensePlate);
        issueInformationRecyclerViewLayout = view.findViewById(R.id.linearLayoutIssueItem);
        issueInformationRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewIssue);
        globalVariable = (GlobalVariable) getActivity().getApplicationContext();
        btnUpdateReportIssue = view.findViewById(R.id.btnUpdateIssue);

        reportIssueResponsePresenter.getIssueInformationOfAVehicle(globalVariable.getUsername());

        btnUpdateReportIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Integer> listReportIssueId = issueAdapter.getListIssue();
                ReportIssueInformationForUpdating reportIssueInformationForUpdating = new ReportIssueInformationForUpdating();
                reportIssueInformationForUpdating.setUsername(globalVariable.getUsername());
                reportIssueInformationForUpdating.setReportIssueIdList(listReportIssueId);
                reportIssueForUpdatingPresenter.updateReportIssue(reportIssueInformationForUpdating);
            }
        });

        return view;
    }

    private void init(){
        reportIssueResponsePresenter = new ReportIssueResponsePresenter();
        reportIssueResponsePresenter.setView(this);

        reportIssueForUpdatingPresenter = new ReportIssueForUpdatingPresenter();
        reportIssueForUpdatingPresenter.setView(this);
    }

    public void getReportIssueContentResponseList(List<ReportIssueContentResponse> reportIssueContentResponseList) {
        this.reportIssueContentResponseList = reportIssueContentResponseList;
    }

    @Override
    public void getIssueInformationOfAVehicleSuccess(ReportIssueResponse reportIssueResponse) {
        txtCurrentLicensePlate.setText(reportIssueResponse.getVehicleLicensePlates());
        issueAdapter = new IssueAdapter(reportIssueResponse.getReportIssueContentResponses(), getActivity());
        issueInformationRecyclerView.setAdapter(issueAdapter);
        issueInformationRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getReportIssueContentResponseList(reportIssueResponse.getReportIssueContentResponses());
    }

    @Override
    public void getIssueInformationOfAVehicleFailure(String message) {
        Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateReportIssueForSuccess() {
        Toast.makeText(this.getContext(), "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
        reportIssueResponsePresenter.getIssueInformationOfAVehicle(globalVariable.getUsername());
    }

    @Override
    public void updateReportIssueForFailure(String message) {
        Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);

        // Refresh tab data:

        if (getFragmentManager() != null) {

            getFragmentManager()
                    .beginTransaction()
                    .detach(this)
                    .attach(this)
                    .commit();
        }
    }
}