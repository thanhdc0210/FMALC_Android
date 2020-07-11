package com.demo.fmalc_android.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.fmalc_android.R;
import com.demo.fmalc_android.contract.ReportIssueContract;
import com.demo.fmalc_android.entity.Inspection;
import com.demo.fmalc_android.entity.ReportIssue;
import com.demo.fmalc_android.presenter.ConsignmentDetailPresenter;
import com.demo.fmalc_android.presenter.ReportIssuePresenter;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.SneakyThrows;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class InspectionAdapter extends RecyclerView.Adapter<InspectionAdapter.ViewHolder> implements ReportIssueContract.View {

    private List<Inspection> inspectionList;
    private List<Inspection> inspectionListFull;
    private Context context;
    public HashMap<Integer, ReportIssue> listIssue = new HashMap();


    private Uri fileUri;
    private Bitmap bitmap;

    private ReportIssuePresenter reportIssuePresenter;

    Integer id;


    public HashMap<Integer, ReportIssue> getListIssue() {
        return listIssue;
    }

    public void setListIssue(HashMap<Integer, ReportIssue> listIssue) {
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

    private void init() {
        reportIssuePresenter = new ReportIssuePresenter();
        reportIssuePresenter.setView(this);

    }

    @NonNull
    @Override
    public InspectionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.inspection_item, parent, false);
        init();
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
                Toast.makeText(context, id + "-------", Toast.LENGTH_SHORT).show();
                if (buttonView.isChecked()) {
                    if (!listIssue.containsKey(id)) {
                        //nếu chưa có add thêm
                        ReportIssue reportIssue = new ReportIssue(id, "", "");
                        listIssue.put(id, reportIssue);
                    }
//                    else if(listIssue.containsKey(id)) {
//                        ReportIssue reportIssue = new ReportIssue(id,holder.edtNoteIssue.getText().toString(),"");
//                        listIssue.replace(id, reportIssue );
                    //xóa khỏi list
//                        listIssue.remove(id);
//                        holder.edtNoteIssue.setText("");
//                        imm.hideSoftInputFromWindow(holder.edtNoteIssue.getWindowToken(), 0);
//                        holder.hiddenLayout.setVisibility(View.GONE);
//                    }
                    holder.hiddenLayout.setVisibility(View.VISIBLE);
                    holder.edtNoteIssue.requestFocus();
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

                    holder.edtNoteIssue.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void afterTextChanged(Editable mEdit) {
                            ReportIssue reportIssue = new ReportIssue(id, holder.edtNoteIssue.getText().toString(), "");
                                   listIssue.replace(id, reportIssue);
                        }

                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }
                    });
//                       holder.edtNoteIssue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                           @Override
//                           public void onFocusChange(View v, boolean hasFocus) {
//                               if (!hasFocus) {
//                                   ReportIssue reportIssue = new ReportIssue(id, holder.edtNoteIssue.getText().toString(), "");
//                                   listIssue.replace(id, reportIssue);
//                               }
//                           }
//                       });
//                    holder.edtNoteIssue.setFocusable(false);

                }
                if (!buttonView.isChecked()) {
                    listIssue.remove(id);
                    holder.edtNoteIssue.setText("");
                    imm.hideSoftInputFromWindow(holder.edtNoteIssue.getWindowToken(), 0);
                    holder.hiddenLayout.setVisibility(View.GONE);

                }
            }

        });
        holder.btnLoadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
                PickImageDialog.build(new PickSetup()).setOnPickResult(new IPickResult() {
                    @SneakyThrows
                    @Override
                    public void onPickResult(PickResult r) {
                        if (r.getError() == null) {
                            r.getPath();
                            Toast.makeText(context, r.getPath(), Toast.LENGTH_SHORT).show();
                            Uri uri = Uri.fromFile(new File(r.getPath()));
                            if (uri != null) {
                                String path = uri.getPath();
                                File file = new File(path);
                                String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                                        .toString());
                                String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                                        fileExtension.toLowerCase());
                                RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), file);
                                //
                                MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
                                init();
                                reportIssuePresenter.getLinkImage(body);
//
                            } else {

                            }
                        } else {
                            Toast.makeText(context, "Có lỗi xảy ra trong quá trình xử lí ảnh " + r.getError().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                })
                        .setOnPickCancel(new IPickCancel() {
                            @Override
                            public void onCancelClick() {
                                //TODO: do what you have to if user clicked cancel
                            }
                        }).show(((AppCompatActivity) context).getSupportFragmentManager());

            }
        });

    }

    @Override
    public int getItemCount() {
        return inspectionList.size();
    }


    @Override
    public void getLinkImageAfterUploadS3(String url) {
        Toast.makeText(context, "Đăng tải ảnh thành công " + url, Toast.LENGTH_SHORT).show();
        ReportIssue currentIssue = listIssue.get(id);
        currentIssue.setImage(url);
        listIssue.replace(id, currentIssue);
    }

    @Override
    public void getLinkImageAfterUploadS3Failure(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

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
            btnLoadImg = itemView.findViewById(R.id.btnLoadImage);

        }


    }


}

