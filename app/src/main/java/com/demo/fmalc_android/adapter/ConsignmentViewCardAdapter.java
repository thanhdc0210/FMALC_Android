package com.demo.fmalc_android.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.fmalc_android.activity.ConsignmentDetailActivity;
import com.demo.fmalc_android.R;
import com.demo.fmalc_android.activity.DriverHomeActivity;
import com.demo.fmalc_android.entity.Consignment;
import com.demo.fmalc_android.entity.Place;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;

public class ConsignmentViewCardAdapter extends  RecyclerView.Adapter<ConsignmentViewCardAdapter.ViewHolder> {
    private List<Consignment> consignmentList;
    private Context context;

    public ConsignmentViewCardAdapter(List<Consignment> consignmentList, Context context) {
        this.consignmentList = consignmentList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view =LayoutInflater.from(context).inflate(R.layout.view_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Consignment consignment = consignmentList.get(position);
        holder.txtId.setText(consignment.getConsignmentId().toString());
        holder.txtCompanyName.setText(consignment.getOwnerName());
        //Giờ làm
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        holder.txtStartTime.setText(format.format(consignment.getPlaces().get(0).getPlannedTime()));

        Place finishPlace = consignment.getPlaces().get(consignment.getPlaces().size()-1);
        holder.txtFinishTime.setText(finishPlace.getPlannedTime().toString());
        holder.txtReceivedPlace.setText(finishPlace.getName());
        holder.txtDeliveryPlace.setText(consignment.getPlaces().get(0).getName());
        holder.txtWeight.setText(consignment.getWeight().toString());
        holder.txtVehicleInfo.setText(consignment.getLicensePlates()+"|"+consignment.getDriverName());


        Long plannedTime = finishPlace.getPlannedTime().getTime();
        Long nowTime = new Date().getTime();
        long diff = plannedTime - nowTime;
        long remaining = diff / (60 * 60 * 1000) % 24;
//        Date now = new Date();
//        String remaining = DateUtils.formatElapsedTime (( now.getTime()-finishPlace.getPlannedTime().getTime() )/1000); // Remaining time to seconds
//        System.out.println(remaining);
//        int time  = (Integer.valueOf(remaining) % 3600) / 60;
        if(remaining <= 0){
            holder.txtTimeCountDown.setText("Trễ "+remaining+ "giờ");
        } else if (remaining >0){
            holder.txtTimeCountDown.setText("Còn "+remaining+ "giờ");
        }

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ConsignmentDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("consignment_id", consignment.getConsignmentId());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return consignmentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtId, txtCompanyName, txtStartTime, txtFinishTime, txtDeliveryPlace, txtReceivedPlace, txtTimeCountDown, txtWeight, txtVehicleInfo;
        public LinearLayout itemLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtId = (TextView) itemView.findViewById(R.id.txtConsignmentId);
            txtCompanyName = (TextView) itemView.findViewById(R.id.txtCompanyName);
            txtStartTime = (TextView) itemView.findViewById(R.id.txtStartTime);
            txtFinishTime = (TextView) itemView.findViewById(R.id.txtFinishTime);
            txtDeliveryPlace = (TextView) itemView.findViewById(R.id.txtDeliveryPlace);
            txtReceivedPlace = (TextView) itemView.findViewById(R.id.txtReceivedPlace);
            txtTimeCountDown = (TextView) itemView.findViewById(R.id.txtTimeCountDown);
            txtWeight = (TextView) itemView.findViewById(R.id.txtWeight);
            txtVehicleInfo = (TextView) itemView.findViewById(R.id.txtVehicleInfo);
            txtTimeCountDown = (TextView) itemView.findViewById(R.id.txtTimeCountDown);
            itemLayout = (LinearLayout) itemView.findViewById(R.id.card_view_item);
        }
    }
}
