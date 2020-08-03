package com.demo.fmalc_android.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.fmalc_android.R;
import com.demo.fmalc_android.activity.ConsignmentDetailActivity;
import com.demo.fmalc_android.entity.NotificationMobileResponse;
import com.demo.fmalc_android.entity.Place;
import com.demo.fmalc_android.entity.Schedule;
import com.demo.fmalc_android.fragment.MaintainFragment;
import com.demo.fmalc_android.enumType.NotificationTypeEnum;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class NotificationViewCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<NotificationMobileResponse> notificationMobileResponses;
    private Context context;
    private final int VIEW_TYPE_ITEM=0,VIEW_TYPE_LOADING=1;

    public NotificationViewCardAdapter(List<NotificationMobileResponse> notificationMobileResponses, Context context){
        this.notificationMobileResponses = notificationMobileResponses;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_ITEM){
            View view = LayoutInflater.from(context).inflate(R.layout.view_card_notification, parent, false);
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
        return notificationMobileResponses == null ? 0 : notificationMobileResponses.size();
    }

    public int getItemViewType(int position) {
        return notificationMobileResponses.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        public TextView txtContent, txtTime, txtTitle;
        public LinearLayout notificationItemLayout;
        public ImageView imageView;


        public ItemViewHolder(@NonNull View itemView){
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtContent = itemView.findViewById(R.id.txtContentNotification);
            txtTime = itemView.findViewById(R.id.txtTime);
            imageView = itemView.findViewById(R.id.imageView);
            notificationItemLayout = itemView.findViewById(R.id.card_view_item_notification);
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(NotificationViewCardAdapter.LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    private void populateItemRows(ItemViewHolder holder, int position) {

        NotificationMobileResponse notificationMobileResponse = notificationMobileResponses.get(position);


        if(notificationMobileResponse.isStatus()){
            holder.notificationItemLayout.setBackgroundColor(Color.parseColor("#EEEEEE"));
        }else{
            holder.notificationItemLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

        // Kiểm tra còn bao nhiêu thời gian
        Long notificationTime = notificationMobileResponse.getTime().getTime();
        Long nowTime = new Date().getTime();
        long diff = notificationTime - nowTime;
        int diffWeeks = (int) diff / (14 * 60 * 60 * 1000 * 7);
        int diffDays = (int) diff / (24 * 60 * 60 * 1000) % 7;
        int diffHours = (int) diff / (60 * 60 *1000) % 24;
        int diffMinutes = (int) diff / ( 60 * 1000) % 60 % 24;

        String s = "";

        if (diffWeeks < 0){
            s = Math.abs(diffWeeks) + " tuần trước";
            holder.txtTime.setText(s);
        }

        else if (diffDays < 0){
            s = Math.abs(diffDays) + " ngày trước ";
            holder.txtTime.setText(s);
        }

        else if (diffHours < 0){
            s = Math.abs(diffHours) + " giờ trước ";
            holder.txtTime.setText(s);
        }

        else{
            s = Math.abs(diffMinutes) + " phút trước ";
            holder.txtTime.setText(s);
        }
        holder.txtContent.setText(notificationMobileResponse.getContent());
        int type = notificationMobileResponse.getType();

        switch (type){
            // xe chạy ngoài giờ làm việc
            case 0:
                holder.txtTitle.setText("Cảnh báo chạy ngoài giờ:");
                holder.imageView.setImageResource(R.drawable.ic_warning_amber_24px);
                break;
            // dừng xe quá lâu
            case 1:
                holder.txtTitle.setText("Cảnh báo xe dừng quá lâu:");
                holder.imageView.setImageResource(R.drawable.ic_warning_amber_24px);
                break;
            case 2:
                holder.txtTitle.setText("Lịch bảo trì xe mới");
                holder.imageView.setImageResource(R.drawable.ic_new_releases_242px);
                break;
            case 3:
                holder.txtTitle.setText("Lịch chạy mới");
                holder.imageView.setImageResource(R.drawable.ic_notification_important_24px);
               break;
        }


        //TODO sửa lại khi click xem notification
        holder.notificationItemLayout.setOnClickListener(new View.OnClickListener() {
            Intent intent = new Intent(context, ConsignmentDetailActivity.class);
            Bundle bundle = new Bundle();
            @Override
            public void onClick(View view) {
                switch (type){
                    case 2:
                        intent = new Intent(context, ConsignmentDetailActivity.class);
                        bundle.putInt("notification_id", notificationMobileResponse.getId());
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(context, MaintainFragment.class);
                        bundle.putInt("notification_id", notificationMobileResponse.getId());
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                        break;
                    default: break;
                }

//                if (type == 0 || type == 1){
//
//                }else if (type == 2){
//
//                }else{
//
//                }

            }
        });

    }

}
