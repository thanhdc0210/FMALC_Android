package com.demo.fmalc_android.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.fmalc_android.R;
import com.demo.fmalc_android.entity.Place;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;

public class ScheduleTimeStepAdapter extends RecyclerView.Adapter<ScheduleTimeStepAdapter.ViewHolder> {
    private List<Place> placeList;
    private Context context;

    public ScheduleTimeStepAdapter(List<Place> placeList, Context context) {
        this.placeList = placeList;
        this.context = context;
    }

    @NonNull
    @Override
    public ScheduleTimeStepAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.time_step_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleTimeStepAdapter.ViewHolder holder, int position) {
        placeList.sort(Comparator.comparing(Place::getPlannedTime));
        Place place = placeList.get(position);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        //set màu nếu đến điểm đó rồi
        if(place.getActualTime() != null) {

            holder.txtPlannedTime.setText(format.format(place.getPlannedTime())+"  ");
            holder.txtAddressDetails.setText(place.getAddress());
            holder.txtTypePlace.setText(place.getTypeStr());
            //set màu
            holder.txtTypePlace.setTextColor(context.getResources().getColor(R.color.colorBlue));
            holder.txtAddressDetails.setTextColor(R.color.colorBlack);
            holder.txtPlannedTime.setTextColor(R.color.colorBlack);
            //đổi icon
            holder.iconCircle.setColorFilter(context.getResources().getColor(R.color.colorGreen));
            holder.view.setBackgroundColor(context.getResources().getColor(R.color.colorGreen));
            if (placeList.get(placeList.size()-1) == place){
                holder.layout.setVisibility(View.GONE);
                holder.layoutIconFinish.setVisibility(View.VISIBLE);
                holder.iconFinish.setColorFilter(context.getResources().getColor(R.color.colorGreen));
            }
        } else {
            holder.txtPlannedTime.setText(format.format(place.getPlannedTime())+"  ");
            holder.txtAddressDetails.setText(place.getAddress());
            holder.txtTypePlace.setText(place.getTypeStr());
            if (placeList.get(placeList.size()-1) == place){
                holder.layout.setVisibility(View.GONE);
                holder.layoutIconFinish.setVisibility(View.VISIBLE);
            }
        }


    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtPlannedTime, txtTypePlace, txtAddressDetails;
        public LinearLayout itemLayout, layoutIconFinish,layout;
        public ImageView iconCircle,iconFinish;
        public View view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPlannedTime = (TextView) itemView.findViewById(R.id.txtPlannedTime);
            txtTypePlace = (TextView) itemView.findViewById(R.id.txtTypePlace);
            txtAddressDetails = (TextView) itemView.findViewById(R.id.txtAddressDetails);
            itemLayout = (LinearLayout) itemView.findViewById(R.id.card_view_item);
            layout = (LinearLayout) itemView.findViewById(R.id.layout);
            layoutIconFinish = itemView.findViewById(R.id.layoutIconFinish);
            iconCircle = itemView.findViewById(R.id.iconCircle);
            iconFinish = itemView.findViewById(R.id.iconFinish);
            view = itemView.findViewById(R.id.view);
        }
    }
}