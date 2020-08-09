package com.demo.fmalc_android.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.fmalc_android.R;
import com.demo.fmalc_android.entity.FuelRequest;
import com.demo.fmalc_android.entity.FuelType;

import java.util.ArrayList;
import java.util.List;

public class FuelTypeAdapter extends RecyclerView.Adapter<FuelTypeAdapter.ViewHolder> {

    private List<FuelType> fuelTypeList = new ArrayList<>();
    private Context context;
    private int lastSelectedPosition = -1;
    public Integer id = -1;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private FuelType fuelType = new FuelType();

    public FuelType getFuelType() {
        return fuelType;
    }

    public FuelTypeAdapter(List<FuelType> fuelTypeList, Context context) {
        this.fuelTypeList = fuelTypeList;
        this.context = context;
    }

    @NonNull
    @Override
    public FuelTypeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.fuel_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FuelTypeAdapter.ViewHolder holder, int position) {
        boolean mCheck = false;
        FuelType fuelTypeChecked = fuelTypeList.get(position);

        holder.txtFuelType.setText(fuelTypeChecked.getType());
        holder.txtPrice.setText(fuelTypeChecked.getPrice() + "");
        holder.rdbtnFuel.setChecked(position == lastSelectedPosition);
        holder.rdbtnFuel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setId(fuelTypeChecked.getId());
                    fuelType.setId(fuelTypeChecked.getId());
                    fuelType.setPrice(fuelTypeChecked.getPrice());
//                    Toast.makeText(context, fuelTypeChecked.getPrice()+" id: " + fuelTypeChecked.getId(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return fuelTypeList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public TextView txtFuelType;
        public TextView txtPrice;
        public RadioButton rdbtnFuel;
        public LinearLayout linearLayoutFuelType;

        ViewHolder(View itemView) {
            super(itemView);
            txtFuelType = itemView.findViewById(R.id.txtFuelType);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            rdbtnFuel = itemView.findViewById(R.id.rdbtnFuel);
            linearLayoutFuelType = itemView.findViewById(R.id.linearLayoutFuelType);
            rdbtnFuel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int copyLastCheck = lastSelectedPosition;
                    lastSelectedPosition = getAdapterPosition();
                    notifyItemChanged(copyLastCheck);
                    notifyItemChanged(lastSelectedPosition);

                }
            });
        }


    }
}

