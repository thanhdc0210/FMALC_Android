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
import com.demo.fmalc_android.activity.MaintainAndIssueActivity;
import com.demo.fmalc_android.contract.NotificationMobileContract;
import com.demo.fmalc_android.entity.NotificationMobileResponse;
import com.demo.fmalc_android.presenter.NotificationMobilePresenter;

import java.util.Date;
import java.util.List;

public class NotificationViewCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements NotificationMobileContract.View{
    private List<NotificationMobileResponse> notificationMobileResponses;
    private Context context;
    private String auth;
    private Integer driverId;
    private final int VIEW_TYPE_ITEM=0,VIEW_TYPE_LOADING=1;
    private NotificationMobilePresenter notificationMobilePresenter;
    private Integer scheduleId;

    public NotificationViewCardAdapter(List<NotificationMobileResponse> notificationMobileResponses, Context context, String auth, Integer driverId){
        this.notificationMobileResponses = notificationMobileResponses;
        this.context = context;
        this.auth = auth;
        this.driverId = driverId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        notificationMobilePresenter = new NotificationMobilePresenter();
        notificationMobilePresenter.setView(this);

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

    @Override
    public void findNotificationByUsernameSuccess(List<NotificationMobileResponse> notificationMobileResponses) {

    }

    @Override
    public void findNotificationByUsernameFailure(String message) {

    }

    @Override
    public void updateStatusSuccess() {

    }

    @Override
    public void updateStatusFailure(String message) {

    }

    @Override
    public void takeDayOffSuccess(boolean status) {

    }

    @Override
    public void takeDayOffFailure(String message) {

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
            holder.notificationItemLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }else{
            holder.notificationItemLayout.setBackgroundColor(Color.parseColor("#D8D8D8"));
        }

        // Kiểm tra thông báo được gửi bao phút trước
        Long notificationTime = notificationMobileResponse.getTime().getTime();
        Long nowTime = new Date().getTime();
        long diff = notificationTime - nowTime;
        int diffDays = (int) diff / (24 * 60 * 60 * 1000);
        int diffHours = (int) diff / (60 * 60 *1000) % 24;
        int diffMinutes = (int) diff / ( 60 * 1000) % 60 % 24;

        String s = "";

        if (diff == 0){
            s += " 1 phút trước";
            holder.txtTime.setText(s);
        }

//        if (diffWeeks < 0){
//            s = Math.abs(diffWeeks) + " tuần trước";
//            holder.txtTime.setText(s);
//        }

        else if (diffDays < 0){
            s += Math.abs(diffDays) + " ngày trước ";
            holder.txtTime.setText(s);
        }

        else if (diffHours < 0){
            s += Math.abs(diffHours) + " giờ trước ";
            holder.txtTime.setText(s);
        }

        else if (diffMinutes < 0){
            s += Math.abs(diffMinutes) + " phút trước ";
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
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                holder.txtTitle.setText("Yêu cầu xin nghỉ được chấp nhận");
                holder.imageView.setImageResource(R.drawable.ic_check_circle_24px);
                break;
            case 8:
                holder.txtTitle.setText("Yêu cầu xin nghỉ không được chấp nhận");
                holder.imageView.setImageResource(R.drawable.ic_cancel_24px);
                break;
        }


        //TODO sửa lại khi click xem notification
        holder.notificationItemLayout.setOnClickListener(new View.OnClickListener() {
            Intent intent = new Intent(context, ConsignmentDetailActivity.class);
            Bundle bundle = new Bundle();
            @Override
            public void onClick(View view) {
                holder.notificationItemLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                notificationMobilePresenter.updateStatus(notificationMobileResponse.getNotificationId(), notificationMobileResponse.getUsername(), auth);
                switch (type){
                    case 3:
                            String subString[] = notificationMobileResponse.getContent().split("#");
                            String subStringId[] = subString[subString.length - 1].split("\\s");
                            Integer consignmentId =  Integer.valueOf(subStringId[0]);
                            intent = new Intent(context, ConsignmentDetailActivity.class);
                            bundle.putInt("schedule_id", notificationMobileResponse.getScheduleId());
                            bundle.putInt("consignment_id", consignmentId);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(context, MaintainAndIssueActivity.class);
//                        bundle.putInt("notification_id", notificationMobileResponse.getNotificationId());
//                        intent.putExtras(bundle);
                        context.startActivity(intent);
//                        MaintainAndIssueActivity activity = (MaintainAndIssueActivity) view.getContext();
//                        activity.getSupportFragmentManager()
//                                .beginTransaction()
//                                .replace(R.id.container, new MaintainFragment())
//                                .attach(new MaintainFragment())
//                                .detach(new MaintainFragment())
//                                .commit();
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
