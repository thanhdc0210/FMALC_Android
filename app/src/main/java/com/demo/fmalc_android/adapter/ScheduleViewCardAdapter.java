package com.demo.fmalc_android.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.fmalc_android.activity.ConsignmentDetailActivity;
import com.demo.fmalc_android.R;
import com.demo.fmalc_android.entity.Schedule;
import com.demo.fmalc_android.entity.Place;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ScheduleViewCardAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Schedule> scheduleList;
    private Context context;
    private final int VIEW_TYPE_ITEM=0,VIEW_TYPE_LOADING=1;

    public ScheduleViewCardAdapter(List<Schedule> scheduleList, Context context) {
        this.scheduleList = scheduleList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_ITEM){
            View view = LayoutInflater.from(context).inflate(R.layout.view_card, parent, false);
            return new ItemViewHolder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ItemViewHolder) {
            populateItemRows((ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }
    }

    @Override
    public int getItemCount() {
        return scheduleList == null ? 0 : scheduleList.size();
    }

    /**
     * The following method decides the type of ViewHolder to display in the RecyclerView
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return scheduleList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView txtId, txtCompanyName, txtStartTime, txtFinishTime, txtDeliveryPlace, txtReceivedPlace, txtTimeCountDown, txtWeight, txtVehicleInfo;
        public LinearLayout itemLayout;
        public ItemViewHolder(@NonNull View itemView) {
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

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    private void populateItemRows(ItemViewHolder holder, int position) {
        Schedule schedule = scheduleList.get(position);
        holder.txtId.setText(schedule.getScheduleId().toString());
        holder.txtCompanyName.setText(schedule.getOwnerName());
        //Giờ làm
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        holder.txtStartTime.setText(format.format(schedule.getPlaces().get(0).getPlannedTime()));
        List<Place> places = schedule.getPlaces();
        places.sort(Comparator.comparing(Place::getPlannedTime));
        Place finishPlace = places.get(places.size()-1);
        holder.txtFinishTime.setText(format.format(finishPlace.getPlannedTime()));
        holder.txtReceivedPlace.setText(finishPlace.getName());
        holder.txtDeliveryPlace.setText(places.get(0).getName());
        holder.txtWeight.setText(schedule.getWeight().toString() + " kg ");
        holder.txtVehicleInfo.setText(schedule.getLicensePlates()+" | "+ schedule.getDriverName());

        // Kiểm tra còn bao nhiêu thời gian
        Long plannedTime = finishPlace.getPlannedTime().getTime();
        Long nowTime = new Date().getTime();
        long diff = plannedTime - nowTime;
        int diffDays = (int) diff / (24 * 60 * 60 * 1000);
        int diffHours = (int) diff / (60 * 60 *1000) % 24;
        int diffMinutes = (int) diff / ( 60 * 1000) % 60 % 24;

        String s = "";

        if (diffDays > 0 && diffHours > 0 && diffMinutes >= 0){
            s = "Còn ";
        }else{
            s = "Trễ ";
            holder.txtTimeCountDown.setTextColor(Color.RED);
        }

        if (diffDays < 0){
            s = s + Math.abs(diffDays) + " ngày ";
            holder.txtTimeCountDown.setText(s);
        }

        if (diffHours < 0){
            s = s + Math.abs(diffHours) + " giờ ";
            holder.txtTimeCountDown.setText(s);
        }

        if (diffMinutes <= 0){
            s = s + Math.abs(diffMinutes) + " phút";
            holder.txtTimeCountDown.setText(s);
        }

        if (diffDays > 0){
            s = s + Math.abs(diffDays) + " ngày ";
            holder.txtTimeCountDown.setText(s);
        }

        if (diffHours > 0){
            s = s + Math.abs(diffHours) + " giờ ";
            holder.txtTimeCountDown.setText(s);
        }

        if (diffMinutes >= 0){
            s = s + Math.abs(diffMinutes) + " phút";
            holder.txtTimeCountDown.setText(s);
        }

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
}
