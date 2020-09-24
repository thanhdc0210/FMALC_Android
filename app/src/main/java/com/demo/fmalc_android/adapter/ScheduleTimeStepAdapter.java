package com.demo.fmalc_android.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import  android.Manifest.permission;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.fmalc_android.R;
import com.demo.fmalc_android.entity.Place;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import lombok.SneakyThrows;

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

    @SneakyThrows
    @Override
    public void onBindViewHolder(@NonNull ScheduleTimeStepAdapter.ViewHolder holder, int position) {
        placeList.sort(Comparator.comparing(Place::getPlannedTime));
        Place place = placeList.get(position);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat hour = new SimpleDateFormat("HH:mm");

        //set màu nếu đến điểm đó rồi
        if(place.getActualTime() != null) {
            Date dateFormatted = format.parse(format.format(place.getActualTime()));
            holder.txtPlannedTime.setText(date.format(dateFormatted)+"  ");
            holder.txtHour.setText(hour.format(dateFormatted)+"  ");
            holder.txtAddressDetails.setText(place.getAddress());
            holder.txtTypePlace.setText(place.getTypeStr());
            holder.txtContactName.setText(place.getContactName()+" - ");
            holder.txtPhoneNumber.setText(place.getContactPhone());
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
            Date dateFormatted = format.parse(format.format(place.getPlannedTime()));
            holder.txtContactName.setText(place.getContactName()+" - ");
            holder.txtPhoneNumber.setText(place.getContactPhone());
            holder.txtPlannedTime.setText(date.format(dateFormatted)+"  ");
            holder.txtHour.setText(hour.format(dateFormatted)+"  ");
            holder.txtAddressDetails.setText(place.getAddress());
            holder.txtTypePlace.setText(place.getTypeStr());
            if (placeList.get(placeList.size()-1) == place){
                holder.layout.setVisibility(View.GONE);
                holder.layoutIconFinish.setVisibility(View.VISIBLE);
            }
        }
        holder.txtPhoneNumber.setPaintFlags(holder.txtPhoneNumber.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.txtPhoneNumber.setClickable(true);
        holder.txtPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(context,permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + holder.txtPhoneNumber.getText()));
                    context.startActivity(callIntent);
                } else {
                    ActivityCompat.requestPermissions(
                            (Activity) context,
                            new String[]{permission.CALL_PHONE},
                            1
                    );
                }

            }
        });


    }


    @Override
    public int getItemCount() {
        return placeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtPlannedTime, txtTypePlace, txtAddressDetails, txtHour, txtContactName, txtPhoneNumber;
        public LinearLayout itemLayout, layoutIconFinish,layout;
        public ImageView iconCircle,iconFinish;
        public View view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPlannedTime = (TextView) itemView.findViewById(R.id.txtPlannedTime);
            txtHour = (TextView) itemView.findViewById(R.id.txtPlannedHour);
            txtTypePlace = (TextView) itemView.findViewById(R.id.txtTypePlace);
            txtAddressDetails = (TextView) itemView.findViewById(R.id.txtAddressDetails);
            itemLayout = (LinearLayout) itemView.findViewById(R.id.card_view_item);
            layout = (LinearLayout) itemView.findViewById(R.id.layout);
            layoutIconFinish = itemView.findViewById(R.id.layoutIconFinish);
            iconCircle = itemView.findViewById(R.id.iconCircle);
            iconFinish = itemView.findViewById(R.id.iconFinish);
            view = itemView.findViewById(R.id.view);
            txtPhoneNumber = itemView.findViewById(R.id.txtPhoneNumber);
            txtContactName = itemView.findViewById(R.id.txtContactName);

        }
    }
}