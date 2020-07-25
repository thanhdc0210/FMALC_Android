package com.demo.fmalc_android.adapter;


import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.amazonaws.Response;
import com.demo.fmalc_android.R;
import com.demo.fmalc_android.contract.MaintenanceContract;
import com.demo.fmalc_android.entity.MaintainResponse;
import com.demo.fmalc_android.entity.ReportIssueContentResponse;
import com.demo.fmalc_android.presenter.MaintenancePresenter;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MaintainAdapter extends RecyclerView.Adapter<MaintainAdapter.ViewHolder> implements MaintenanceContract.View {

    private List<MaintainResponse> maintainList;
    private Context context;
    private Integer id;

    private Uri fileUri;
    private Bitmap bitmap;
    private AlertDialog alertDialog;

    private MaintenancePresenter maintenancePresenter;

    public MaintainAdapter(List<MaintainResponse> maintainResponseList, Context context) {
        this.maintainList = maintainResponseList;
        this.context = context;
    }


    @NonNull
    @Override
    public MaintainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.maintain_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MaintainAdapter.ViewHolder holder, int position) {
        MaintainResponse maintainResponse = maintainList.get(position);
        id = maintainResponse.getMaintainId();
        holder.txtLicensePlateMaintain.setText(maintainResponse.getLicensePlates());
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        holder.txtMaintainDate.setText(formatter.format(maintainResponse.getMaintainDate()));
        holder.txtMaintainTypeName.setText(maintainResponse.getMaintainTypeName());
        holder.maintainItem.setOnClickListener(new View.OnClickListener() {
            @SneakyThrows
            @Override
            public void onClick(View v) {
                showDetailIssueDialog(maintainResponse, v);
            }
        });

    }

    private void showDetailIssueDialog(MaintainResponse maintainResponse, View v) throws IOException, URISyntaxException {
        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.custom_dialog_maintain, (ViewGroup) v.getParent(), false);

        Button btnSave = dialogView.findViewById(R.id.btnSave);
        Button btnTakeImage = dialogView.findViewById(R.id.btnAddImgMaintain);

        TextView txtLicensePlate = dialogView.findViewById(R.id.txtLicensePlate);
        txtLicensePlate.setText(maintainResponse.getLicensePlates());

        TextView txtMaintainName = dialogView.findViewById(R.id.txtMaintainName);
        txtMaintainName.setText(maintainResponse.getMaintainTypeName());

        TextView txtContent = dialogView.findViewById(R.id.txtContent);
        txtContent.setText(maintainResponse.getContent());

        EditText edtCurrentKm = dialogView.findViewById(R.id.edtCurrentKm);
        edtCurrentKm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    edtCurrentKm.setError("Không được để trống");
                } else {
                    edtCurrentKm.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        TextView txtMaintainDate = dialogView.findViewById(R.id.txtMaintainDate);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        txtMaintainDate.setText(formatter.format(maintainResponse.getMaintainDate()));

        ImageView imgMaintain = dialogView.findViewById(R.id.imgMaintain);


        final String[] pathImg = new String[1];
        btnTakeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImageDialog.build(new PickSetup())
                        .setOnPickResult(new IPickResult() {
                            @Override
                            public void onPickResult(PickResult r) {
                                if (r.getError() == null) {
                                    {
                                        imgMaintain.setImageBitmap(r.getBitmap());
                                        pathImg[0] = r.getPath().toString();
                                    }
                                }
                            }
                        }).setOnPickCancel(new IPickCancel() {
                    @Override
                    public void onCancelClick() {

                    }
                }).show(((AppCompatActivity) context).getSupportFragmentManager());

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        alertDialog = builder.create();
        alertDialog.show();

        maintenancePresenter = new MaintenancePresenter();
        maintenancePresenter.setView(this);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @SneakyThrows
            @Override
            public void onClick(View v) {
                if (pathImg[0] != null) {
                    Uri uri = Uri.fromFile(new File(pathImg[0]));
                    if (uri != null) {
                        String path = uri.getPath();
                        File file = new File(path);
                        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                                .toString());
                        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                                fileExtension.toLowerCase());
                        RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), file);
                        //
                        MultipartBody.Part image = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
                        maintenancePresenter.updateMaintenance(id, Integer.valueOf(edtCurrentKm.getText().toString()), image);
                    }
                }
            };
        });
    }


    @Override
    public int getItemCount() {
        return maintainList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void getMaintenanceListSuccessful(List<MaintainResponse> maintainResponseList) {

    }

    @Override
    public void getMaintenanceListFailure(String message) {

    }

    @Override
    public void updateMaintenanceSuccessful(boolean isSuccessful) {
        if (isSuccessful){
            Toast.makeText(context, "Lưu thành công", Toast.LENGTH_SHORT).show();
            alertDialog.dismiss();
        }
    }

    @Override
    public void updateMaintenanceFailure(String message) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public TextView txtLicensePlateMaintain, txtMaintainDate, txtMaintainTypeName;
        public LinearLayout maintainItem;

        ViewHolder(View itemView) {
            super(itemView);
            txtLicensePlateMaintain = itemView.findViewById(R.id.txtLicensePlateMaintain);
            txtMaintainDate = itemView.findViewById(R.id.txtMaintainDate);
            txtMaintainTypeName = itemView.findViewById(R.id.txtMaintainTypeName);
            maintainItem = itemView.findViewById(R.id.maintain_item);
        }


    }
}

