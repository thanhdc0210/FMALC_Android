package com.demo.fmalc_android.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.fmalc_android.R;
import com.demo.fmalc_android.activity.ConsignmentDetailActivity;
import com.demo.fmalc_android.activity.PreparingActivity;
import com.demo.fmalc_android.entity.Inspection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class InspectionAdapter extends RecyclerView.Adapter<InspectionAdapter.ViewHolder> implements Filterable {

    private List<Inspection> inspectionList;
    private List<Inspection> inspectionListFull;
    private Context context;
    private HashMap<String, String> listIssue = new HashMap();


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
//        holder.txtInspectionName.setText(inspection.getInspectionName());
        holder.cbInspection.setText(inspection.getInspectionName());
        holder.cbInspection.setTextSize(16);
        holder.cbInspection.setTag(position);

        holder.cbInspection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String id = inspection.getId().toString();
                if (buttonView.isChecked()) {
                    if (!listIssue.containsKey(id)) {
                        //nếu chưa có add thêm
                        listIssue.put(id, "");
                    } else {
                        //xóa khỏi list
                        listIssue.remove(id);
                    }
                    holder.edtNoteIssue.setVisibility(View.VISIBLE);
                    holder.edtNoteIssue.requestFocus();
                    holder.edtNoteIssue
                            .setOnEditorActionListener(
                                    new TextView.OnEditorActionListener() {
                                        @Override
                                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                            if (actionId == EditorInfo.IME_ACTION_DONE) {
                                                listIssue.replace(id, holder.edtNoteIssue.getText().toString());
                                                Toast.makeText(context, listIssue.get(id) + "ahahaha", Toast.LENGTH_SHORT).show();
                                                return true;
                                            }
                                            return false;

                                        }
                                    }


                            );
                }
                if(!buttonView.isChecked()){
                    listIssue.remove(id);
                    holder.edtNoteIssue.setVisibility(View.GONE);

                }
            }

        });

    }

    @Override
    public int getItemCount() {
        return inspectionList.size();
    }

    @Override
    public Filter getFilter() {
        return inspectionFilter();
    }

    private Filter inspectionFilter() {
        return new Filter() {
            @Override
            protected Filter.FilterResults performFiltering(CharSequence constraint) {
                List<Inspection> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(inspectionListFull);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Inspection item : inspectionListFull) {
                        if (item.getInspectionName().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                }
                Filter.FilterResults results = new Filter.FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
                inspectionList.clear();
                inspectionList.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public EditText edtNoteIssue;
        public CheckBox cbInspection;
        public LinearLayout itemLayout;

        ViewHolder(View itemView) {
            super(itemView);
//            txtInspectionName = itemView.findViewById(R.id.txtInspectionName);

            cbInspection = itemView.findViewById(R.id.cbInspection);
            edtNoteIssue = itemView.findViewById(R.id.edtNoteIssue);
            itemLayout = itemView.findViewById(R.id.linearLayoutInspectionItem);

        }


    }
}

