package com.demo.fmalc_android.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.fmalc_android.R;
import com.demo.fmalc_android.adapter.FuelTypeAdapter;
import com.demo.fmalc_android.contract.FuelTypeContract;
import com.demo.fmalc_android.entity.FuelType;
import com.demo.fmalc_android.presenter.FuelTypePresenter;

import java.util.ArrayList;
import java.util.List;

public class FillingFuelActivity extends AppCompatActivity implements FuelTypeContract.View {

    private List<FuelType> fuelTypeList = new ArrayList<>();
    private FuelTypePresenter fuelTypePresenter;
    private FuelTypeAdapter fuelTypeAdapter;
    private RecyclerView fuelTypeRecyclerView;

    private EditText edtCurrentKm;
    private EditText edtVolume;
    private TextView txtTotalPrice;
    private TextView txtCurrentLicensePlate;
    private Button btnSaveFillingFuel;

    private Double totalPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filling_fuel);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Thông tin đổ xăng dầu");
        init();
        fuelTypeRecyclerView = findViewById(R.id.recyclerViewFuel);
        fuelTypePresenter.getListFuelTypes();
        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        edtCurrentKm = findViewById(R.id.edtCurrentKm);
        edtVolume = findViewById(R.id.edtVolume);
        edtVolume.setText("0");
        btnSaveFillingFuel = findViewById(R.id.btnSaveFillingFuel);
        btnSaveFillingFuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FillingFuelActivity.this, "Lưu thành công", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });

    }

    private void init() {
        fuelTypePresenter = new FuelTypePresenter();
        fuelTypePresenter.setView(this);
    }

    @Override
    public void getListFuelTypeSuccess(List<FuelType> fuelTypeList) {
        fuelTypeAdapter = new FuelTypeAdapter(fuelTypeList, getApplicationContext());
        fuelTypeRecyclerView.setAdapter(fuelTypeAdapter);
        fuelTypeRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        edtVolume.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() ==0) {
                    edtVolume.setError("Không được để trống");
                } else {
                    edtVolume.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                int id = fuelTypeAdapter.getId();
                if (id != -1) {
                    FuelType fuelTypeCurrent = fuelTypeList.stream().filter(e -> e.getId().equals(id)).findAny().orElse(null);
                    if (fuelTypeCurrent != null) {
                        if(!s.toString().isEmpty()) {
                            Double volume = Double.valueOf(s.toString());
                            totalPrice = volume * fuelTypeCurrent.getPrice();
                            txtTotalPrice.setText(totalPrice + " VNĐ");
                        } else {
                            txtTotalPrice.setText(0 + " VNĐ");
                        }
                    }
                }
            }
        });
    }


    @Override
    public void getListFuelTypeFailure(String message) {
        Toast.makeText(this.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

}