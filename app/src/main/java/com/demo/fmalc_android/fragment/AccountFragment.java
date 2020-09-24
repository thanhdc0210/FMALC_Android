package com.demo.fmalc_android.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.demo.fmalc_android.R;
import com.demo.fmalc_android.activity.LoginActivity;
import com.demo.fmalc_android.contract.DayOffContract;
import com.demo.fmalc_android.contract.DriverContract;
import com.demo.fmalc_android.contract.NotificationMobileContract;
import com.demo.fmalc_android.contract.TokenDeviceContract;
import com.demo.fmalc_android.entity.DayOffDriverRequestDTO;
import com.demo.fmalc_android.entity.DayOffResponseDTO;
import com.demo.fmalc_android.entity.DriverInformation;
import com.demo.fmalc_android.entity.GlobalVariable;
import com.demo.fmalc_android.entity.Notification;
import com.demo.fmalc_android.entity.NotificationMobileResponse;
import com.demo.fmalc_android.enumType.DayOffEnum;
import com.demo.fmalc_android.enumType.NotificationTypeEnum;
import com.demo.fmalc_android.presenter.DayOffPresenter;
import com.demo.fmalc_android.presenter.DriverPresenter;
import com.demo.fmalc_android.presenter.NotificationMobilePresenter;
import com.demo.fmalc_android.presenter.TokenDevicePresenter;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import lombok.SneakyThrows;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment implements DriverContract.View, DatePickerDialog.OnDateSetListener, NotificationMobileContract.View, DayOffContract.View, TokenDeviceContract.View {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView txtUsernameProfile, txtPhoneNumber, txtWorkedTime, txtIdentityNo, txtDoB, startDate, endDate;
    private ImageView imgProfile;
    private Button btnLogout, btnDatePicker, btnUnexpectedRequest;
    private LinearLayout linearLayout5;
    GlobalVariable globalVariable;
    boolean flag;
    SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
    private NotificationMobilePresenter notificationPresenter;
    private DayOffPresenter dayOffPresenter;

    private DriverInformation driverInformation;

    public DriverInformation getDriverInformation() {
        return driverInformation;
    }

    private DriverPresenter driverPresenter;

    private int statusForNoti;
    private String noteReason;
    private SweetAlertDialog sweetAlertDialogCheckDayOff;
    private SharedPreferences sp;

    private TokenDevicePresenter tokenDevicePresenter;

    public AccountFragment() {
        // Required empty public constructor
    }

    private void init() {
        driverPresenter = new DriverPresenter();
        driverPresenter.setView(this);
        notificationPresenter = new NotificationMobilePresenter();
        notificationPresenter.setView(this);
        dayOffPresenter = new DayOffPresenter();
        dayOffPresenter.setView(this);
        tokenDevicePresenter = new TokenDevicePresenter();
        tokenDevicePresenter.setView(this);
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
        sweetAlertDialogCheckDayOff = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
        txtUsernameProfile = view.findViewById(R.id.txtUsernameProfile);
        txtIdentityNo = view.findViewById(R.id.txtIdentityNo);
        txtPhoneNumber = view.findViewById(R.id.txtPhoneNumber);
        txtWorkedTime = view.findViewById(R.id.txtWorkedTime);
        txtDoB = view.findViewById(R.id.txtDoB);
        imgProfile = view.findViewById(R.id.imgProfile);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnDatePicker = view.findViewById(R.id.btnPickDate);
        startDate = view.findViewById(R.id.startDate);
        endDate = view.findViewById(R.id.endDate);
        linearLayout5 = view.findViewById(R.id.linearLayout5);
        btnUnexpectedRequest = view.findViewById(R.id.btnUnexpectedRequest);
        flag = false;
        init();
        //TODO thay id user hiện tại vào
        globalVariable = (GlobalVariable) getActivity().getApplicationContext();
        driverPresenter.getDriverInformation(globalVariable.getId());
        sp = this.getActivity().getSharedPreferences("logged", Context.MODE_PRIVATE);

        linearLayout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusForNoti = NotificationTypeEnum.DAY_OFF_BY_SCHEDULE.getValue();
                Calendar now = Calendar.getInstance();
                now.add(Calendar.DATE, 7);
                DatePickerDialog dpd = com.borax12.materialdaterangepicker.date.DatePickerDialog.newInstance(
                        AccountFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
//                dpd.setAutoHighlight(mAutoHighlight);
                dpd.setMinDate(now);
                dpd.setStartTitle("Từ ngày");
                dpd.setEndTitle("Đến ngày");
                dpd.setAccentColor(Color.DKGRAY);
                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");

            }
        });
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!flag) {
                    statusForNoti = NotificationTypeEnum.DAY_OFF_BY_SCHEDULE.getValue();
                    Calendar now = Calendar.getInstance();
                    now.add(Calendar.DATE, 7);
                    DatePickerDialog dpd = com.borax12.materialdaterangepicker.date.DatePickerDialog.newInstance(
                            AccountFragment.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    );
//                dpd.setAutoHighlight(mAutoHighlight);
                    dpd.setMinDate(now);
                    dpd.setStartTitle("Từ ngày");
                    dpd.setEndTitle("Đến ngày");
                    dpd.setAccentColor(Color.DKGRAY);
                    dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
                } else {
                    //gửi
                    DayOffDriverRequestDTO requestDTO = new DayOffDriverRequestDTO();
                    requestDTO.setContent(startDate.getText().toString() + "|" + endDate.getText().toString());
                    requestDTO.setType(NotificationTypeEnum.DAY_OFF_BY_SCHEDULE.getValue());
                    requestDTO.setDriverId(globalVariable.getId());
                    requestDTO.setStartDate(startDate.getText().toString());
                    requestDTO.setEndDate(endDate.getText().toString());
                    dayOffPresenter.checkDayOff(requestDTO, globalVariable.getToken());
//                    Notification notification = new Notification();
//                    notification.setContent(startDate.getText().toString() + "|" + endDate.getText().toString());
//                    notification.setDriver_id(globalVariable.getId());
//                    notification.setType(NotificationTypeEnum.DAY_OFF_BY_SCHEDULE.getValue());
//                    notification.setStatus(false);
//                    notificationPresenter.takeDayOff(notification);
                }
            }
        });
        btnUnexpectedRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusForNoti = NotificationTypeEnum.DAY_OFF_UNEXPECTED.getValue();
                final EditText editText = new EditText(getContext());
                editText.setTextColor(Color.BLACK);
                sweetAlertDialogCheckDayOff = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
                sweetAlertDialogCheckDayOff.setTitleText("Báo nghỉ đột xuất?");
                sweetAlertDialogCheckDayOff.setContentText("Lí do nghỉ độ xuất");
                sweetAlertDialogCheckDayOff.setConfirmText("Gửi");
                sweetAlertDialogCheckDayOff.setCustomView(editText);
                sweetAlertDialogCheckDayOff.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        noteReason = editText.getText().toString();
                        DayOffDriverRequestDTO requestDTO = new DayOffDriverRequestDTO();
                        requestDTO.setContent(noteReason);
                        requestDTO.setType(NotificationTypeEnum.DAY_OFF_UNEXPECTED.getValue());
                        requestDTO.setDriverId(globalVariable.getId());
                        Date now = new Date();
                        requestDTO.setStartDate(formatDate.format(now));
                        requestDTO.setEndDate(formatDate.format(now));
                        dayOffPresenter.checkDayOff(requestDTO, globalVariable.getToken());
                    }
                });
                sweetAlertDialogCheckDayOff.setCancelButton("Hủy", Dialog::dismiss);
                sweetAlertDialogCheckDayOff.show();
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Xác nhận đăng xuất")
                        .setContentText("Bạn có muốn đăng xuất")
                        .setConfirmText("Có")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sp.edit().putBoolean("logged", false).apply();
                                sp.edit().putString("username", "").apply();
                                sp.edit().putString("role", "").apply();
                                sp.edit().putString("token", "").apply();
                                sp.edit().putInt("id", 0).apply();
                                sDialog.dismissWithAnimation();
                                tokenDevicePresenter.updateTokenDevice(globalVariable.getId(), "", globalVariable.getToken());
                                Intent i = new Intent(getContext(), LoginActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }
                        })
                        .setCancelButton("Không", Dialog::dismiss)
                        .show();
            }
        });
        return view;
    }

    @Override
    public void getDriverInformationSuccessful(DriverInformation driverInformation) {
        // set ảnh
        if (driverInformation.getImage() != null) {
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

    @Override
    public void onResume() {
        super.onResume();
        DatePickerDialog dpd = (DatePickerDialog) getActivity().getFragmentManager().findFragmentByTag("Datepickerdialog");
        if (dpd != null) dpd.setOnDateSetListener(this);
    }

    @SneakyThrows
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        monthOfYear = monthOfYear + 1;
        monthOfYearEnd = monthOfYearEnd + 1;
        String month = monthOfYear + "";
        String monthEnd = monthOfYearEnd + "";
        String date = dayOfMonth + "";
        String dateEnd = dayOfMonthEnd + "";
        if (monthOfYear < 10) {
            month = "0" + monthOfYear;
        }
        if (monthOfYearEnd < 10) {
            monthEnd = "0" + monthOfYearEnd;
        }
        if (dayOfMonth < 10) {
            date = "0" + dayOfMonth;

        }
        if (dayOfMonthEnd < 10) {
            dateEnd = "0" + dayOfMonthEnd;
        }

        String startDateString = date + "-" + month + "-" + year;
        String endDateString = dateEnd + "-" + monthEnd + "-" + yearEnd;
        if (!checkDates(startDateString, endDateString)) {
            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Xin lỗi...")
                    .setContentText("Chọn ngày không hợp lệ, xin thử lại!")
                    .show();
        } else {
            startDate.setText(startDateString);
            endDate.setText(endDateString);
            btnDatePicker.setText("Gửi");
            flag = true;
        }
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
        new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Gửi thành công")
                .setContentText("Thông tin nghỉ phép đã được gửi, vui lòng chờ phản hồi từ quản lí")
                .show();
    }

    @Override
    public void takeDayOffFailure(String message) {
        new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Xin lỗi...")
                .setContentText("Có lỗi xảy ra, xin thử lại!")
                .show();
    }


    public boolean checkDates(String startDate, String endDate) throws ParseException {

        Date now = new Date();
        now = atStartOfDay(now);
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DATE, 7);
        now = cal.getTime();

        Date start = formatDate.parse(startDate);
        start = atStartOfDay(start);
        Date end = formatDate.parse(endDate);
        end = atEndOfDay(end);
        if (start.after(now) || start.equals(now)) {
            if (start.before(end)) {
                return true;
            } else return false;
        }

//        return calendar.getTime();
//
//        LocalDateTime s = LocalDateTime.parse(startDate, formatDate);
//        LocalDateTime e = LocalDateTime.parse(endDate, formatDate);
//        LocalDateTime now = LocalDateTime.now().with(LocalDateTime.MIN);
//        now.plusDays(7);
//        if (s.isAfter(now) || s.equals(now)) {
//            if (s.isBefore(e) || s.isEqual(e)) {
//                checkDate = true;//If start date is before end date
//            } else {
//                checkDate = false; //If start date is after the end date
//            }
//        }

        return false;
    }

    public Date atEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public Date atStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    @Override
    public void checkDayOffForDriverSuccess(DayOffResponseDTO responseDTO) {
        sweetAlertDialogCheckDayOff.hide();
        if (responseDTO.getDayOffId() == 0) {
            Notification notification = new Notification();
            notification.setDriver_id(globalVariable.getId());
            notification.setType(statusForNoti);
            notification.setStatus(false);
            if (statusForNoti == NotificationTypeEnum.DAY_OFF_BY_SCHEDULE.getValue()) {
                notification.setContent(startDate.getText().toString() + "|" + endDate.getText().toString());
            } else {
                notification.setContent(noteReason);
            }
            notificationPresenter.takeDayOff(notification);
        } else if (responseDTO.getDayOffId() > 0) {
            if (statusForNoti == NotificationTypeEnum.DAY_OFF_UNEXPECTED.getValue()) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Ngày nghỉ đã tồn tại")
                        .setContentText("Yêu cầu ngày nghỉ của bạn đang chờ phản hồi từ quản lí!")
                        .show();

            } else {
                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Xác nhận thay đổi?")
                        .setContentText("Bạn có muốn thay đổi ngày nghỉ "
                                + responseDTO.getStartDate() + " đến " + responseDTO.getEndDate() + " thành " +
                                startDate.getText().toString() + " đến " + endDate.getText().toString() + "?")
                        .setCancelButton("Hủy", Dialog::dismiss)
                        .setConfirmText("Có, thay đổi")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {

                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismiss();
                                DayOffDriverRequestDTO requestDTO = new DayOffDriverRequestDTO();
                                requestDTO.setContent(startDate.getText().toString() + "|" + endDate.getText().toString());
                                requestDTO.setType(statusForNoti);
                                requestDTO.setDriverId(globalVariable.getId());
                                requestDTO.setStartDate(startDate.getText().toString());
                                requestDTO.setEndDate(endDate.getText().toString());
                                dayOffPresenter.updateDayOff(responseDTO.getDayOffId(), requestDTO, globalVariable.getToken());
                            }
                        })
                        .show();
            }
        }
    }

    @Override
    public void checkDayOffForDriverFailure(String message) {
        new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Xin lỗi...")
                .setContentText(message)
                .show();
    }

    @Override
    public void updateDayOffForDriverSuccess(DayOffResponseDTO responseDTO) {
        new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Cập nhật ngày nghỉ thành công")
                .setContentText("Thông tin nghỉ phép đã được gửi, vui lòng chờ phản hồi từ quản lí")
                .show();

        Notification notification = new Notification();

        notification.setDriver_id(globalVariable.getId());
        notification.setType(statusForNoti);
        if (statusForNoti == NotificationTypeEnum.DAY_OFF_BY_SCHEDULE.getValue()) {
            notification.setContent(null);
        } else {
            notification.setContent(noteReason);
        }
        notification.setStatus(false);
        notificationPresenter.takeDayOff(notification);
    }

    @Override
    public void updateDayOffForDriverFailure(String message) {
        new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Xin lỗi...")
                .setContentText("Có lỗi xảy ra, xin thử lại!")
                .show();
    }

    public String reverse(String str) {
        String s[] = str.split("-");
        String ans = "";
        for (int i = s.length - 1; i >= 0; i--) {
            ans += s[i] + "-";
        }
        return (ans.substring(0, ans.length() - 1));
    }

    @Override
    public void updateTokenDeviceSuccess() {

    }

    @Override
    public void updateTokenDeviceFailure(String message) {

    }
}