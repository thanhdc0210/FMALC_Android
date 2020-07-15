package com.demo.fmalc_android.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.ActionBarOverlayLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.fmalc_android.R;
import com.demo.fmalc_android.activity.ConsignmentDetailActivity;
import com.demo.fmalc_android.activity.InProgressActivity;
import com.demo.fmalc_android.activity.InspectionActivity;
import com.demo.fmalc_android.activity.MaintainAndIssueActivity;
import com.demo.fmalc_android.activity.PreparingActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InspectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InspectionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String value;
    Button btnPrepare;
    Button btnInProgress;
    Button btnComplete;
    Button btnMaintainAndIssue;

    public InspectionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InspectionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InspectionFragment newInstance(String param1, String param2) {
        InspectionFragment fragment = new InspectionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inspection, container, false);
        btnPrepare = view.findViewById(R.id.btnPreparing);
        btnInProgress = view.findViewById(R.id.btnOnTheWay);
        btnComplete = view.findViewById(R.id.btnCompleted);
        btnMaintainAndIssue = view.findViewById(R.id.btnMaintainAndIssue);

        //Button xe chuẩn bị chạy
        btnPrepare.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(getContext(), PreparingActivity.class);
              Bundle bundle = new Bundle();
              bundle.putString("VEHICLE_STATUS", "0");
              intent.putExtras(bundle);
              getContext().startActivity(intent);
          }
      });

        //Button xe đã hoàn tất đơn hàng
        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PreparingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("VEHICLE_STATUS", "3");
                intent.putExtras(bundle);
                getContext().startActivity(intent);
            }
        });

        //Button xe đang chạy
        btnInProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), InProgressActivity.class);
                getContext().startActivity(intent);
            }
        });
        btnMaintainAndIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MaintainAndIssueActivity.class);
                getContext().startActivity(intent);
            }
        });



////        TextView txtTest = view.findViewById(R.id.txtTest);
//        String[] singleChoiceItems = getResources().getStringArray(R.array.dialog_single_choice_array);
//        int itemSelected = 0;
//        Bundle bundle = new Bundle();
//
//        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
//        mBuilder.setTitle("Chọn trạng thái báo cáo cho xe")
//                .setIcon(R.drawable.ic_rv_hookup_24px_rounded)
//                .setPositiveButton("OK",test("ádas"))
//                .setCancelable(false);
//        mBuilder.setSingleChoiceItems(singleChoiceItems, -1, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                value = singleChoiceItems[i];
////                txtTest.setText(value);
//                dialogInterface.dismiss();
//            }
//        });
//
//        AlertDialog mDialog = mBuilder.create();
//        mDialog.show();

        return view;
    }



    public void test( String value){
        Toast.makeText(getContext(), value, Toast.LENGTH_SHORT).show();
    }
}