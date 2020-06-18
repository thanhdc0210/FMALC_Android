package com.demo.fmalc_android.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.demo.fmalc_android.R;
import com.demo.fmalc_android.contract.ConsignmentContract;
import com.demo.fmalc_android.contract.LoginContract;
import com.demo.fmalc_android.entity.Account;
import com.demo.fmalc_android.entity.GlobalVariable;
import com.demo.fmalc_android.entity.Token;
import com.demo.fmalc_android.presenter.ConsignmentPresenter;
import com.demo.fmalc_android.presenter.LoginPresenter;
import com.demo.fmalc_android.retrofit.NetworkingUtils;
import com.demo.fmalc_android.service.AccountService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    private TextInputEditText edtUsername, edtPassword;
    private MaterialButton btnLogin;
    private LoginPresenter accountPresenter;
    private ConsignmentPresenter consignmentPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = findViewById(R.id.username);
        edtPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnLogin);

        init();

    }

    private void init(){
        accountPresenter = new LoginPresenter();
        accountPresenter.setView(this);
    }

    private boolean checkLogin(String name, String password) {
        boolean isValid = true;
        if (name.equals("")) {
            edtUsername.setError("Vui lòng điền tên đăng nhập!");
            isValid = false;
        }
        if (password.equals("")) {
            edtPassword.setError("Vui lòng điền mật khẩu !");
            isValid = false;
        }
        return isValid;
    }

    public void login(View view){

        String username = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();
        //validate
        if (checkLogin(username, password)) {
            try {
                accountPresenter.doLogin(username, password);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

//        ConsignmentService consignmentService = NetworkingUtils.getConsignmentService();
//
//        Call<DetailedConsignment> call = consignmentService.findByConsignmentId(1);
//        call.enqueue(new Callback<DetailedConsignment>() {
//            @Override
//            public void onResponse(Call<DetailedConsignment> call, Response<DetailedConsignment> response) {
//                try{
//                    Log.e("FAIL", " Successfully " + response.body().getConsignmentId());
//                }catch (Throwable t){
//                    Log.e("UNSUCCESSFULLY", " Unsuccessfully " + t.getMessage());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<DetailedConsignment> call, Throwable t) {
//                Log.e("FAIL", " Unsuccessfully" + t.getMessage());
//            }
//        });

//        ConsignmentService consignmentService = NetworkingUtils.getConsignmentService();
//        String username = "admin";
//        List<Integer> list = new ArrayList<>();
//        list.add(0);
//        StatusRequest statusRequest = new StatusRequest(username, list);
//        Call<List<Consignment>> call = consignmentService.findByConsignmentStatusAndUsernameForFleetManager(statusRequest);
//
//        call.enqueue(new Callback<List<Consignment>>() {
//            @Override
//            public void onResponse(Call<List<Consignment>> call, Response<List<Consignment>> response) {
//                Log.e("FAIL", " Successfully " + response.body().get(0).getConsignmentId());
//            }
//
//            @Override
//            public void onFailure(Call<List<Consignment>> call, Throwable t) {
//                Log.e("FAIL", " Unsuccessfully " + t.getMessage());
//            }
//        });
    }

    @Override
    public void loginSuccess() {
        Intent intent = new Intent(getApplicationContext(), DriverHomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void loginFailure(String message) {
        Toast.makeText(this, "Đăng nhập không thành công", Toast.LENGTH_SHORT).show();
    }
}