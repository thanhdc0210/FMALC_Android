package com.demo.fmalc_android.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.demo.fmalc_android.R;
import com.demo.fmalc_android.contract.AlertContract;
import com.demo.fmalc_android.contract.VehicleContract;
import com.demo.fmalc_android.entity.AlertRequestDTO;
import com.demo.fmalc_android.entity.GlobalVariable;
import com.demo.fmalc_android.entity.Notification;
import com.demo.fmalc_android.entity.VehicleDetail;
import com.demo.fmalc_android.entity.VehicleInspection;
import com.demo.fmalc_android.enumType.LevelInAlertEnum;
import com.demo.fmalc_android.enumType.NotificationTypeEnum;
import com.demo.fmalc_android.presenter.AlertPresenter;
import com.demo.fmalc_android.presenter.VehiclePresenter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;


public class InProgressActivity extends AppCompatActivity implements View.OnClickListener, VehicleContract.View, AlertContract.View {

    private Button btnLv1, btnLv2, btnLv3, btnLv4;
    private int vehicleId = 0;
    private GlobalVariable globalVariable;
    private VehiclePresenter vehiclePresenter;
    private AlertPresenter alertPresenter;
    private AlertDialog.Builder alert;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Báo cáo khi đang chạy");
        setContentView(R.layout.activity_in_progress);
        btnLv1 = findViewById(R.id.btnLevel1);
        btnLv1.setOnClickListener(this);
        btnLv2 = findViewById(R.id.btnLevel2);
        btnLv2.setOnClickListener(this);
        btnLv3 = findViewById(R.id.btnLevel3);
        btnLv3.setOnClickListener(this);
        btnLv4 = findViewById(R.id.btnLevel4);
        btnLv4.setOnClickListener(this);
        init();
        globalVariable = (GlobalVariable) getApplicationContext();

        vehiclePresenter = new VehiclePresenter();
        vehiclePresenter.setView(this);

        vehiclePresenter.getVehicleRunning(globalVariable.getUsername(), globalVariable.getToken());


    }

    private void init(){
        alertPresenter = new AlertPresenter();
        alertPresenter.setView(this);
    }
    @Override
    public void onClick(View v) {
        final int[] level = new int[1];
        EditText edittext = new EditText(getApplicationContext());
        edittext.setPadding(20,0,20,0);
//        edittext.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        final String[] content = new String[1];
        alert = new AlertDialog.Builder (this)
                .setTitle("Báo cáo sự cố")
                .setMessage("Ghi chú lí do")
                .setView(edittext)
                .setPositiveButton("Gửi ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        content[0] = edittext.getText().toString();
                        AlertRequestDTO requestDTO =
                                new AlertRequestDTO(content[0].toString(), level[0], globalVariable.getId(), vehicleId);
//                                vehiclePresenter = new VehiclePresenter();

                        alertPresenter.sendRequestWhileRunning(requestDTO, globalVariable.getToken());
                        //OR
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });


        switch (v.getId()) {
            case R.id.btnLevel1:
                content[0] = "";
                level[0] = LevelInAlertEnum.WEAK.getValue();
                alert.show();
                break;
            case R.id.btnLevel2:
                content[0] = "";
                level[0] = LevelInAlertEnum.MEDIUM.getValue();
                alert.show();
                break;
            case R.id.btnLevel3:
                content[0] = "";
                level[0] = LevelInAlertEnum.HIGH.getValue();
                alert.show();
                break;
            case R.id.btnLevel4:
                level[0] = LevelInAlertEnum.SUPER_HIGH.getValue();
                 alertDialog = new AlertDialog.Builder(this)
                        .setTitle("Yêu cầu đổi xe")
                        .setMessage("Bạn có chắc chắn yêu cầu đổi xe?")
                        .setPositiveButton("Yêu cầu",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
//                                        System.out.println(content[0].toString()+level[0]+ vehicleId);

                                        if(content[0] == null){
                                            content[0]="";
                                        }

                                        AlertRequestDTO requestDTO =
                                                new AlertRequestDTO(content[0], level[0], globalVariable.getId(), vehicleId);
//                                        alertPresenter = new AlertPresenter();
                                        alertPresenter.sendRequestWhileRunning(requestDTO, globalVariable.getToken());
                                    }
                                })
                        .setNegativeButton(
                                "Hủy",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.cancel();
                                    }
                                }).show();
                break;
            default:
                break;
        }
    }


    @Override
    public void findVehicleLicensePlatesAndInspectionForReportInspectionBeforeDeliverySuccess(VehicleInspection vehicleInspection) {

    }

    @Override
    public void findVehicleLicensePlatesAndInspectionForReportInspectionBeforeDeliveryFailure(String message) {

    }

    @Override
    public void getVehicleRunningSuccess(int vehicleId) {
        this.vehicleId = vehicleId;
        btnLv1.setEnabled(true);
        btnLv2.setEnabled(true);
        btnLv3.setEnabled(true);
        btnLv4.setEnabled(true);
    }

    @Override
    public void getVehicleRunningFailure(String message) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Opps")
                .setContentText("Tính năng chỉ hữu dụng khi bạn đang chạy xe")
                .setConfirmButton("Trở về", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        onBackPressed();
                    }
                })
                .show();
    }
    private AlertRequestDTO alertRequestDTO;
    @Override
    public void sendRequestWhileRunningSuccess(AlertRequestDTO alertRequestDTO) {
//        if(alertRequestDTO!=null)
        this.alertRequestDTO = alertRequestDTO;
//        alertPresenter = new AlertPresenter();
        alertPresenter.getDetailVehicle(alertRequestDTO.getVehicleId());

    }

    @Override
    public void sendRequestWhileRunningFailure(String message) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Có lỗi xảy ra")
                .setContentText(message)
                .show();
    }

    @Override
    public void sendNotificationSuccess(String success) {
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Gửi báo cáo")
                .setContentText("Đã gửi thành công")
                .show();
    }

    @Override
    public void sendNotificationFailed(String message) {

    }

    @Override
    public void getDetailVehicleSuccess(VehicleDetail vehicleDetail) {
        Notification notification = new Notification();
        notification.setType(NotificationTypeEnum.ALERT.getValue());
        String message=" báo trễ "+LevelInAlertEnum.getValueEnumToShow(alertRequestDTO.getLevel())+ " với lí do "+alertRequestDTO.getContent();
        if(alertRequestDTO.getLevel()== LevelInAlertEnum.SUPER_HIGH.getValue()){
            message = " yêu cầu đổi xe ";
        }else{

        }
        notification.setContent(message);
        notification.setVehicle_id(alertRequestDTO.getVehicleId());
        notification.setDriver_id(alertRequestDTO.getDriverId());
        notification.setStatus(false);
//        alertPresenter = new AlertPresenter();
        alertPresenter.sendNotification(notification);

    }

    @Override
    public void getDetailVehicleFailed(String message) {

    }

//    @Override
//    public void sendRequestWhileRunningSuccess(String s) {
//        Toast.makeText(this, "AAAAA", Toast.LENGTH_SHORT).show();
////        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
////                .setTitleText("Gửi báo cáo")
////                .setContentText("Đã gửi thành công")
////                .show();
//
//    }
//
//    @Override
//    public void sendRequestWhileRunningFailure(String message) {
//        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
//                .setTitleText("Có lỗi xảy ra")
//                .setContentText(message)
//                .show();
//    }
}