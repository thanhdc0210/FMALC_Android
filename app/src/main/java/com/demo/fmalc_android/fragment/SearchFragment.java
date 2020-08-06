package com.demo.fmalc_android.fragment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.fmalc_android.R;
import com.demo.fmalc_android.adapter.ScheduleViewCardAdapter;
import com.demo.fmalc_android.contract.SearchingContract;
import com.demo.fmalc_android.entity.GlobalVariable;
import com.demo.fmalc_android.entity.Schedule;
import com.demo.fmalc_android.enumType.SearchTypeForDriverEnum;
import com.demo.fmalc_android.presenter.SchedulePresenter;
import com.demo.fmalc_android.presenter.SearchingPresenter;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements SearchingContract.View {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private MaterialSearchView searchView;
    private ChipGroup chipGroup;
    private Chip consignmentId, licensePlates, consignmentOwner;
    private SearchTypeForDriverEnum searchType;
    private RecyclerView consignmentRecyclerView;
    private SearchingPresenter searchingPresenter;
    private ScheduleViewCardAdapter scheduleViewCardAdapter;
    private TextView txtEmpty;
    private GlobalVariable globalVariable;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        globalVariable = (GlobalVariable) getActivity().getApplicationContext();
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Tìm kiếm lịch trình");
        consignmentRecyclerView = view.findViewById(R.id.rvConsignment);
        txtEmpty = view.findViewById(R.id.txtEmptyView);
        searchingPresenter = new SearchingPresenter();
        searchingPresenter.setView(this);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        consignmentId = view.findViewById(R.id.consignmentId);
        licensePlates = view.findViewById(R.id.licensePlates);
        consignmentOwner = view.findViewById(R.id.consignmentOwner);
        chipGroup = view.findViewById(R.id.chipGroup);
        searchType = SearchTypeForDriverEnum.CONSIGNMENT_ID;
        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
//                Chip chip = chipGroup.findViewById(checkedId);
                if (checkedId == consignmentId.getId()) {
                    searchType = SearchTypeForDriverEnum.CONSIGNMENT_ID;
                } else if (checkedId == licensePlates.getId()) {
                    searchType = SearchTypeForDriverEnum.LICENSE_PLATE;
                } else if (checkedId == consignmentOwner.getId()) {
                    searchType = SearchTypeForDriverEnum.OWNER_NAME;
                } else {
                    searchType = SearchTypeForDriverEnum.CONSIGNMENT_ID;
                }
            }
        });
        searchView = view.findViewById(R.id.search);
        searchView.closeSearch();
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty()){
                searchingPresenter.searchConsignment(searchType, query, globalVariable.getToken());
                } else {
                    return false;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.search);
        searchView.setMenuItem(item);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void searchConsignmentSuccessful(List<Schedule> scheduleList) {
        if (scheduleList == null) {
            txtEmpty.setVisibility(View.VISIBLE);
            consignmentRecyclerView.setVisibility(View.GONE);
        } else {
            txtEmpty.setVisibility(View.GONE);
            consignmentRecyclerView.setVisibility(View.VISIBLE);
            scheduleViewCardAdapter = new ScheduleViewCardAdapter(scheduleList, getActivity());
            consignmentRecyclerView.setAdapter(scheduleViewCardAdapter);
            consignmentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }

    @Override
    public void searchConsignmentForFailure(String message) {
        txtEmpty.setVisibility(View.VISIBLE);
        consignmentRecyclerView.setVisibility(View.GONE);
    }
}