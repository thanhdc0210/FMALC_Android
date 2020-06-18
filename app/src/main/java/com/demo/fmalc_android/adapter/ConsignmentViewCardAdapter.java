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
import com.demo.fmalc_android.entity.Consignment;
import com.demo.fmalc_android.entity.Place;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;


@AllArgsConstructor
class ConsignmentViewCardAdapter extends  RecyclerView.Adapter<ConsignmentViewCardAdapter.ViewHolder> {
    private List<Consignment> consignmentList;
    private Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_card , parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConsignmentViewCardAdapter.ViewHolder holder, int position) {
        final Consignment consignment = consignmentList.get(position);
        holder.txtId.setText(consignment.getConsignmentId().toString());
        holder.txtCompanyName.setText(consignment.getOwnerName());
        holder.txtStartTime.setText(consignment.getPlaces().get(0).getPlannedTime().toString());
        Place finishPlace = consignment.getPlaces().get(consignment.getPlaces().size()-1);
        holder.txtFinishTime.setText(finishPlace.getPlannedTime().toString());
        holder.txtReceivedPlace.setText(finishPlace.getName());
        holder.txtDeliveryPlace.setText(consignment.getPlaces().get(0).getName());
        holder.txtWeight.setText(consignment.getWeight().toString());
        holder.txtVehicleInfo.setText(consignment.getLicensePlates()+"|"+consignment.getDriverName());
        Date now = new Date();
        String remaining = DateUtils.formatElapsedTime ((finishPlace.getPlannedTime().getTime() - now.getTime())/1000); // Remaining time to seconds
        int time = new Date(remaining).getHours();
        if(time <= 0){
            holder.txtTimeCountDown.setText("Trễ "+time+ "giờ");
        } else if (time >0){
            holder.txtTimeCountDown.setText("Còn "+time+ "giờ");
        }

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ConsignmentDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("consignment_id", consignment.getConsignmentId());
                intent.putExtra("data", bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
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
