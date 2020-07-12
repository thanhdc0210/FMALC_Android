package com.demo.fmalc_android.adapter;



import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amazonaws.services.s3.AmazonS3;
import com.demo.fmalc_android.contract.ReportIssueContract;
import com.demo.fmalc_android.entity.ReportIssueContentRequest;
import com.demo.fmalc_android.entity.ReportIssueRequest;
import com.demo.fmalc_android.presenter.ReportIssuePresenter;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.services.s3.AmazonS3Client;
import com.demo.fmalc_android.R;
import com.demo.fmalc_android.activity.ConsignmentDetailActivity;
import com.demo.fmalc_android.activity.PreparingActivity;
import com.demo.fmalc_android.entity.Inspection;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import lombok.SneakyThrows;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class InspectionAdapter extends RecyclerView.Adapter<InspectionAdapter.ViewHolder> implements IPickResult {

    private List<Inspection> inspectionList;
    private List<Inspection> inspectionListFull;
    private Context context;
    public HashMap<Integer, ReportIssueContentRequest> listIssue = new HashMap();

    private AmazonS3Client s3Client;
    private BasicAWSCredentials credentials;
    private AmazonS3 s3;

    private Uri fileUri;
    private Bitmap bitmap;

    private ReportIssuePresenter reportIssuePresenter;

    private Integer id;


    public HashMap<Integer, ReportIssueContentRequest> getListIssue() {
        return listIssue;
    }

    public void setListIssue(HashMap<Integer, ReportIssueContentRequest> listIssue) {
        this.listIssue = listIssue;
    }

    public Integer getId() {
        return id;
    }

    public InspectionAdapter(List<Inspection> inspectionList, Context context) {
        this.inspectionList = inspectionList;
        inspectionListFull = new ArrayList<>(inspectionList);
        this.context = context;
    }

    @NonNull
    @Override
    public InspectionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.inspection_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull InspectionAdapter.ViewHolder holder, int position) {
        Inspection inspection = inspectionList.get(position);
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//        holder.txtInspectionName.setText(inspection.getInspectionName());
        holder.cbInspection.setText(inspection.getInspectionName());
        holder.cbInspection.setTextSize(16);
        holder.cbInspection.setTag(position);

        holder.cbInspection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                id = inspection.getId();
                if (buttonView.isChecked()) {
                    if (!listIssue.containsKey(id)) {
                        //nếu chưa có add thêm
                        ReportIssueContentRequest reportIssueContentRequest = new ReportIssueContentRequest(id,"","");
                        listIssue.put(id, reportIssueContentRequest);
                    } else {
                        //xóa khỏi list
                        listIssue.remove(id);
                        holder.edtNoteIssue.setText("");
                        imm.hideSoftInputFromWindow(holder.edtNoteIssue.getWindowToken(), 0);
                        holder.hiddenLayout.setVisibility(View.GONE);
                    }
                    holder.hiddenLayout.setVisibility(View.VISIBLE);
                    holder.edtNoteIssue.requestFocus();
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    holder.edtNoteIssue
                            .setOnEditorActionListener(
                                    new TextView.OnEditorActionListener() {
                                        @Override
                                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                            if (actionId == EditorInfo.IME_ACTION_DONE) {
                                                ReportIssueContentRequest reportIssueContentRequest = new ReportIssueContentRequest(id,holder.edtNoteIssue.getText().toString(),"");
                                                listIssue.replace(id, reportIssueContentRequest );
                                            }
                                            return false;

                                        }
                                    }


                            );
                }
                if(!buttonView.isChecked()){
                    listIssue.remove(id);
                    holder.edtNoteIssue.setText("");
                    imm.hideSoftInputFromWindow(holder.edtNoteIssue.getWindowToken(), 0);
                    holder.hiddenLayout.setVisibility(View.GONE);

                }
            }

        });
//        holder.btnLoadImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
//                PickImageDialog.build(new PickSetup()).setOnPickResult(new IPickResult() {
//                    @SneakyThrows
//                    @Override
//                    public void onPickResult(PickResult r) {
//                        if (r.getError() == null) {
//                            r.getPath();
//                            Toast.makeText(context, r.getPath(), Toast.LENGTH_SHORT).show();
//                            Uri uri = Uri.fromFile(new File(r.getPath()));
////                            reportIssuePresenter.getLinkImage(uri, context);
//
//                        } else {
//                            Toast.makeText(context, "Có lỗi xảy ra trong quá trình xử lí ảnh "+ r.getError().getMessage(), Toast.LENGTH_LONG).show();
//                        }
//                    }
//                })
//                        .setOnPickCancel(new IPickCancel() {
//                            @Override
//                            public void onCancelClick() {
//                                //TODO: do what you have to if user clicked cancel
//                            }
//                        }).show(((AppCompatActivity) context).getSupportFragmentManager());
//
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return inspectionList.size();
    }



    @SneakyThrows
    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {
            r.getPath();
            Toast.makeText(context, r.getPath(), Toast.LENGTH_SHORT).show();
            Uri uri = Uri.fromFile(new File(r.getPath()));
//            reportIssuePresenter.getLinkImage(uri, context);

        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
            Toast.makeText(context, "Có lỗi xảy ra trong quá trình xử lí ảnh "+ r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }

//    @Override
//    public void getLinkImageAfterUploadS3(String url) {
//        ReportIssue currentIssue = listIssue.get(id);
//        currentIssue.setImage(url);
//        listIssue.replace(id,currentIssue);
//    }
//
//    @Override
//    public void getLinkImageAfterUploadS3Failure(String message) {
//        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public Filter getFilter() {
//        return null;
//    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public EditText edtNoteIssue;
        public CheckBox cbInspection;
        public LinearLayout itemLayout;
        public LinearLayout hiddenLayout;
        public ImageButton btnLoadImg;

        ViewHolder(View itemView) {
            super(itemView);
//            txtInspectionName = itemView.findViewById(R.id.txtInspectionName);
            cbInspection = itemView.findViewById(R.id.cbInspection);
            edtNoteIssue = itemView.findViewById(R.id.edtNoteIssue);

            itemLayout = itemView.findViewById(R.id.linearLayoutInspectionItem);
            hiddenLayout = itemView.findViewById(R.id.hiddenLinear);
//            btnLoadImg = itemView.findViewById(R.id.btnLoadImage);

//            AWSMobileClient.getInstance().initialize(context).execute();
//            credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
//            s3Client = new AmazonS3Client(credentials);
        }


    }
//    private void uploadFile() {
//
//        if (fileUri != null) {
//            final String fileName = edtFileName.getText().toString();
//
//            if (!validateInputFileName(fileName)) {
//                return;
//            }
//
//            final File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
//                    "/" + fileName);
//
//            createFile(getApplicationContext(), fileUri, file);
//
//            TransferUtility transferUtility =
//                    TransferUtility.builder()
//                            .context(getApplicationContext())
//                            .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
//                            .s3Client(s3Client)
//                            .build();
//
//            TransferObserver uploadObserver =
//                    transferUtility.upload("jsaS3/" + fileName + "." + getFileExtension(fileUri), file);
//
//            uploadObserver.setTransferListener(new TransferListener() {
//
//                @Override
//                public void onStateChanged(int id, TransferState state) {
//                    if (TransferState.COMPLETED == state) {
//                        Toast.makeText(getApplicationContext(), "Upload Completed!", Toast.LENGTH_SHORT).show();
//
//                        file.delete();
//                    } else if (TransferState.FAILED == state) {
//                        file.delete();
//                    }
//                }
//
//                @Override
//                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
//                    float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
//                    int percentDone = (int) percentDonef;
//
//                    tvFileName.setText("ID:" + id + "|bytesCurrent: " + bytesCurrent + "|bytesTotal: " + bytesTotal + "|" + percentDone + "%");
//                }
//
//                @Override
//                public void onError(int id, Exception ex) {
//                    ex.printStackTrace();
//                }
//
//            });
//        }
//    }
}

