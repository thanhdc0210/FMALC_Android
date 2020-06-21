package com.demo.fmalc_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.fmalc_android.R;
import com.demo.fmalc_android.entity.Place;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;

public class ConsignmentTimeStepAdapter extends RecyclerView.Adapter<ConsignmentTimeStepAdapter.ViewHolder> {
    private List<Place> placeList;
    private Context context;

    public ConsignmentTimeStepAdapter(List<Place> placeList, Context context) {
        this.placeList = placeList;
        this.context = context;
    }

    @NonNull
    @Override
    public ConsignmentTimeStepAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.time_step_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConsignmentTimeStepAdapter.ViewHolder holder, int position) {
        placeList.sort(Comparator.comparing(Place::getPriority));
        Place place = placeList.get(position);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        holder.txtPlannedTime.setText(format.format(place.getPlannedTime()));
        holder.txtAddressDetails.setText(place.getAddress());
        holder.txtTypePlace.setText(place.getType());


    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtPlannedTime, txtTypePlace, txtAddressDetails;
        public LinearLayout itemLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPlannedTime = (TextView) itemView.findViewById(R.id.txtPlannedTime);
            txtTypePlace = (TextView) itemView.findViewById(R.id.txtTypePlace);
            txtAddressDetails = (TextView) itemView.findViewById(R.id.txtAddressDetails);
            itemLayout = (LinearLayout) itemView.findViewById(R.id.card_view_item);
        }
    }
}