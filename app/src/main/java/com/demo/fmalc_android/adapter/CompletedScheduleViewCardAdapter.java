package com.demo.fmalc_android.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.fmalc_android.R;
import com.demo.fmalc_android.activity.ConsignmentDetailActivity;
import com.demo.fmalc_android.entity.Schedule;
import com.demo.fmalc_android.entity.Place;

import java.text.SimpleDateFormat;
import java.util.List;

public class CompletedScheduleViewCardAdapter extends  RecyclerView.Adapter<CompletedScheduleViewCardAdapter.ViewHolder>{
    private List<Schedule> scheduleList;
    private Context context;

    public CompletedScheduleViewCardAdapter(List<Schedule> scheduleList, Context context) {
        this.scheduleList = scheduleList;
        this.context = context;
    }

    @NonNull
    @Override
    public CompletedScheduleViewCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.view_card, parent, false);
        return new CompletedScheduleViewCardAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompletedScheduleViewCardAdapter.ViewHolder holder, int position) {
        Schedule schedule = scheduleList.get(position);
        holder.txtId.setText(schedule.getScheduleId().toString());
        holder.txtCompanyName.setText(schedule.getOwnerName());
        //Giờ làm
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        holder.txtStartTime.setText(format.format(schedule.getPlaces().get(0).getPlannedTime()));

        Place finishPlace = schedule.getPlaces().get(schedule.getPlaces().size()-1);
        holder.txtFinishTime.setText(format.format(finishPlace.getPlannedTime()));
        holder.txtReceivedPlace.setText(finishPlace.getName());
        holder.txtDeliveryPlace.setText(schedule.getPlaces().get(0).getName());
        holder.txtWeight.setText(schedule.getWeight().toString() + " kg ");
        holder.txtVehicleInfo.setText(schedule.getLicensePlates()+" | "+ schedule.getDriverName());
        holder.txtTimeCountDown.setText("Hoàn thành lúc: " + format.format(finishPlace.getActualTime()));

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ConsignmentDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("consignment_id", schedule.getScheduleId());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
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
