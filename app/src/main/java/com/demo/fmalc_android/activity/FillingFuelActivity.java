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
import com.demo.fmalc_android.contract.FuelContract;
import com.demo.fmalc_android.contract.FuelTypeContract;
import com.demo.fmalc_android.entity.FuelRequest;
import com.demo.fmalc_android.entity.FuelType;
import com.demo.fmalc_android.entity.FuelTypeResponse;
import com.demo.fmalc_android.entity.GlobalVariable;
import com.demo.fmalc_android.presenter.FuelPresenter;
import com.demo.fmalc_android.presenter.FuelTypePresenter;

import java.util.ArrayList;
import java.util.List;

public class FillingFuelActivity extends AppCompatActivity implements FuelTypeContract.View, FuelContract.View {

    private List<FuelType> fuelTypeList = new ArrayList<>();
    private FuelTypePresenter fuelTypePresenter;
    private FuelPresenter fuelPresenter;
    private FuelTypeAdapter fuelTypeAdapter;
    private RecyclerView fuelTypeRecyclerView;

    private EditText edtCurrentKm;
    private EditText edtVolume;
    private TextView txtTotalPrice;
    private TextView txtCurrentLicensePlate;
    private Button btnSaveFillingFuel;

    private Double totalPrice;

    private GlobalVariable globalVariable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Thông tin đổ xăng dầu");
        setContentView(R.layout.activity_filling_fuel);
        init();
        fuelTypeRecyclerView = findViewById(R.id.recyclerViewFuel);
        globalVariable = (GlobalVariable) getApplicationContext();
        List<Integer> status = new ArrayList<>();
        status.add(1);
        status.add(2);
        fuelTypePresenter.getListFuelTypes(globalVariable.getUsername(), status);
        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        txtTotalPrice.setText("0 VNĐ");
        edtCurrentKm = findViewById(R.id.edtCurrentKm);
        edtVolume = findViewById(R.id.edtVolume);
        txtCurrentLicensePlate = findViewById(R.id.txtCurrentLicensePlate);
        btnSaveFillingFuel = findViewById(R.id.btnSaveFillingFuel);
    }

    private void init() {
        fuelTypePresenter = new FuelTypePresenter();
        fuelTypePresenter.setView(this);

        fuelPresenter = new FuelPresenter();
        fuelPresenter.setView(this);
    }

    @Override
    public void getListFuelTypeSuccess(FuelTypeResponse fuelTypeResponse) {
        List<FuelType> fuelTypeList = fuelTypeResponse.getFuelTypeList();
        fuelTypeAdapter = new FuelTypeAdapter(fuelTypeList, getApplicationContext());
        fuelTypeRecyclerView.setAdapter(fuelTypeAdapter);
        fuelTypeRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        txtCurrentLicensePlate.setText(fuelTypeResponse.getVehicleLicensePlate());

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

        btnSaveFillingFuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vehicleLicensePlate = txtCurrentLicensePlate.getText().toString();
                if (vehicleLicensePlate.equals("")){
                    Toast.makeText(FillingFuelActivity.this, "Hiện tại bạn không có lịch chạy", Toast.LENGTH_SHORT);
                }else{
                    String km = edtCurrentKm.getText().toString();
                    String vol = edtVolume.getText().toString();
                    if (km.equals("")){
                        Toast.makeText(FillingFuelActivity.this, "Bạn chưa nhập thông tin số km đổ nhiên liệu", Toast.LENGTH_SHORT);
                    }else if (vol.equals("")){
                        Toast.makeText(FillingFuelActivity.this, "Bạn chưa nhập thông tin số lít nhiên liệu", Toast.LENGTH_SHORT);
                    }else {
                        Integer kmOld = Integer.valueOf(km);
                        Double volume = Double.valueOf(vol);
                        FuelType fuelType = fuelTypeAdapter.getFuelType();

                        if (fuelType != null){
                            Integer fuelTypeId = fuelType.getId();
                            Double unitPriceAtFillingTime = fuelType.getPrice();
                            FuelRequest fuelRequest = new FuelRequest();
                            fuelRequest.setFuelTypeId(fuelTypeId);
                            fuelRequest.setKmOld(kmOld);
                            fuelRequest.setUnitPriceAtFillingTime(unitPriceAtFillingTime);
                            fuelRequest.setVolume(volume);
                            fuelRequest.setVehicleLicensePlates(vehicleLicensePlate);

                            fuelPresenter.saveFuelFilling(fuelRequest);
                        } else {
                            Toast.makeText(FillingFuelActivity.this.getApplicationContext(), "Bạn chưa chọn thông tin nhiên liệu", Toast.LENGTH_SHORT).show();
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

    @Override
    public void saveFuelFillingSuccess() {
        Toast.makeText(this.getApplicationContext(), "Lưu thành công ", Toast.LENGTH_LONG).show();
        onBackPressed();
    }

    @Override
    public void saveFuelFillingFailure(String message) {
        Toast.makeText(this.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}