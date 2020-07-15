package com.demo.fmalc_android.adapter;


import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;

public class IssueAdapter extends RecyclerView.Adapter<IssueAdapter.ViewHolder> {

    private List<ReportIssueContentResponse> issueList;
    private List<ReportIssueContentResponse> issueListFull;
    private Context context;
    private Integer id;

    private Uri fileUri;
    private Bitmap bitmap;
    private List<Integer> listIssue = new ArrayList<>();


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
        ReportIssueContentResponse reportIssueContentResponse = issueList.get(position);
        holder.cbIssue.setText(reportIssueContentResponse.getInspectionName());
            holder.btnViewIssueDetail.setOnClickListener(new View.OnClickListener() {
                @SneakyThrows
                @Override
                public void onClick(View v) {
                    showDetailIssueDialog(reportIssueContentResponse.getInspectionName(),reportIssueContentResponse.getImage(),reportIssueContentResponse.getContent(), v);
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

    private void showDetailIssueDialog(String issueName, String imgUrl, String note, View v) throws IOException {

        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.custom_dialog_issue, (ViewGroup)v.getParent(), false);
        Button btn = dialogView.findViewById(R.id.buttonOk);
        TextView txtIssueName = dialogView.findViewById(R.id.txtIssueName);
        txtIssueName.setText(issueName);
        TextView txtIssueNote = dialogView.findViewById(R.id.txtIssueNote);
        txtIssueNote.setText(note);
        ImageView imageView =(ImageView) dialogView.findViewById(R.id.imgIssue);
        if (imgUrl != null){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                URL url = new URL(imgUrl);
                imageView.setImageBitmap(BitmapFactory.decodeStream((InputStream) url.getContent()));
            } catch (IOException e) {
                Toast.makeText(context, "Có lỗi trong quá trình xử lí ảnh", Toast.LENGTH_SHORT).show();
            }

        }
//        URL url = new URL(imgUrl);
//        Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//        imageView.setImageBitmap(bmp);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
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

