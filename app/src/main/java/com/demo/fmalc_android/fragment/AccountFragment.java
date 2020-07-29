package com.demo.fmalc_android.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.demo.fmalc_android.R;
import com.demo.fmalc_android.activity.LoginActivity;
import com.demo.fmalc_android.contract.DriverContract;
import com.demo.fmalc_android.entity.DriverInformation;
import com.demo.fmalc_android.presenter.DriverPresenter;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment implements DriverContract.View{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView txtUsernameProfile,txtPhoneNumber,txtWorkedTime,txtIdentityNo, txtDoB;
    ImageView imgProfile;
    Button btnLogout;

    private  DriverInformation driverInformation;

    public DriverInformation getDriverInformation() {
        return driverInformation;
    }

    private DriverPresenter driverPresenter;


    public AccountFragment() {
        // Required empty public constructor
    }
    private void init(){
        driverPresenter = new DriverPresenter();
        driverPresenter.setView(this);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
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
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        txtUsernameProfile = view.findViewById(R.id.txtUsernameProfile);
        txtIdentityNo = view.findViewById(R.id.txtIdentityNo);
        txtPhoneNumber = view.findViewById(R.id.txtPhoneNumber);
        txtWorkedTime = view.findViewById(R.id.txtWorkedTime);
        txtDoB = view.findViewById(R.id.txtDoB);
        imgProfile = view.findViewById(R.id.imgProfile);
        btnLogout = view.findViewById(R.id.btnLogout);
       init();
        //TODO thay id user hiện tại vào
        driverPresenter.getDriverInformation(1);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Xác nhận đăng xuất").
                        setMessage("Bạn có chắc muốn đăng xuất?");
                builder.setPositiveButton("Có",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i = new Intent(getContext(),LoginActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);

                            }
                        });
                builder.setNegativeButton("Không",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        return view;
    }

    @Override
    public void getDriverInformationSuccessful(DriverInformation driverInformation) {
        // set ảnh
        if (driverInformation.getImage() != null){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                URL url = new URL(driverInformation.getImage());
                imgProfile.setImageBitmap(BitmapFactory.decodeStream((InputStream) url.getContent()));
            } catch (IOException e) {
                Toast.makeText(getContext(), "Có lỗi trong quá trình xử lí ảnh", Toast.LENGTH_SHORT).show();
            }

        }
        txtUsernameProfile.setText(driverInformation.getName());
        // parse date dd-mm-yyy
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        txtDoB.setText(formatter.format(driverInformation.getDateOfBirth()));
        //-------
        txtWorkedTime.setText(driverInformation.getWorkingHour().toString() + " giờ");
        txtPhoneNumber.setText(driverInformation.getPhoneNumber());
        txtIdentityNo.setText(driverInformation.getIdentityNo());
    }

    @Override
    public void getDriverInformationFailure(String message) {
        Toast.makeText(getContext(), "Có lỗi trong quá trình lấy thông tin " + message, Toast.LENGTH_SHORT).show();
    }
}