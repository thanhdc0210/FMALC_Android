package com.demo.fmalc_android.adapter;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.fmalc_android.R;
import com.demo.fmalc_android.entity.ReportIssueContentResponse;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import lombok.SneakyThrows;

public class IssueAdapter extends RecyclerView.Adapter<IssueAdapter.ViewHolder> {

    private List<ReportIssueContentResponse> issueList;
    private List<ReportIssueContentResponse> issueListFull;
    private Context context;
    private Integer id;

    private Uri fileUri;
    private Bitmap bitmap;
    private List<Integer> listIssue;


    public IssueAdapter(List<ReportIssueContentResponse> issueList, Context context) {
        this.issueList = issueList;
        this.context = context;
    }

    @NonNull
    @Override
    public IssueAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.issue_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull IssueAdapter.ViewHolder holder, int position) {
//        issueList.sort(Comparator.comparing(Place::getPriority));
        ReportIssueContentResponse reportIssueContentResponse = issueList.get(position);
        holder.cbIssue.setText(reportIssueContentResponse.getInspectionName());
            holder.btnViewIssueDetail.setOnClickListener(new View.OnClickListener() {
                @SneakyThrows
                @Override
                public void onClick(View v) {
                    showImage(reportIssueContentResponse.getImage(),reportIssueContentResponse.getContent());
                }
            });

        holder.cbIssue.setOnCheckedChangeListener((new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                id = reportIssueContentResponse.getReportIssueId();
                if(buttonView.isChecked()){
                    if(!listIssue.contains(id)){
                        listIssue.add(id);
                    }
                } else if(!buttonView.isChecked()){
                    listIssue.remove(id);
                }
            }
        }));
    }

    private void showImage(String imgUrl, String note) throws IOException {
        Dialog builder = new Dialog(context);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });
            if (imgUrl != null){
                URL url = new URL(imgUrl);
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                ImageView imageView = new ImageView(context);
                imageView.setImageBitmap(bmp);
                builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            TextView textView = new TextView(context);
            textView.setText(note);
            builder.addContentView(textView, new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            builder.show();
    }



    @Override
    public int getItemCount() {
        return issueList.size();
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


        public CheckBox cbIssue;
        public Button btnViewIssueDetail;
        public LinearLayout linearLayoutIssueItem;

        ViewHolder(View itemView) {
            super(itemView);
            cbIssue = itemView.findViewById(R.id.cbIssue);
            btnViewIssueDetail = itemView.findViewById(R.id.btnViewIssueDetail);
            linearLayoutIssueItem = itemView.findViewById(R.id.linearLayoutIssueItem);
        }


    }
}

