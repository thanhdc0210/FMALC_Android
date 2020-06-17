package com.demo.fmalc_android.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.fmalc_android.R;
import com.demo.fmalc_android.entity.Account;
import com.demo.fmalc_android.entity.Consignment;
import com.demo.fmalc_android.entity.DetailedConsignment;
import com.demo.fmalc_android.entity.StatusRequest;
import com.demo.fmalc_android.entity.Token;
import com.demo.fmalc_android.retrofit.NetworkingUtils;
import com.demo.fmalc_android.service.AccountService;
import com.demo.fmalc_android.service.ConsignmentService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText edtUsername, edtPassword;
    private MaterialButton btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = findViewById(R.id.username);
        edtPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnLogin);


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
//        String username = edtUsername.getText().toString();
//        String password = edtPassword.getText().toString();
//
//        Account account = new Account(username, password);
//
//        AccountService accountService = NetworkingUtils.getAccountApiInstance();
//
//        Call<Token> call = accountService.login(account);
//
//        call.enqueue(new Callback<Token>() {
//            @Override
//            public void onResponse(Call<Token> call, Response<Token> response) {
//                if (checkLogin(username, password)){
//
//                    if(!response.isSuccessful()){
//                        Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
//                        Log.e("FAIL", " Unsuccessfully" + response.code());
//                    }else {
////                        ((Token) getApplication()).setToken(response.body().getToken());
//                        edtUsername.setText(response.body().getToken());
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Token> call, Throwable t) {
//                Log.e("FAIL", "Server error: " + t.getMessage());
//            }
//        });

        ConsignmentService consignmentService = NetworkingUtils.getConsignmentService();

        Call<DetailedConsignment> call = consignmentService.findByConsignmentId(1);
        call.enqueue(new Callback<DetailedConsignment>() {
            @Override
            public void onResponse(Call<DetailedConsignment> call, Response<DetailedConsignment> response) {
                try{
                    Log.e("FAIL", " Successfully " + response.body().getConsignmentId());
                }catch (Throwable t){
                    Log.e("UNSUCCESSFULLY", " Unsuccessfully" + t.getMessage());
                }
            }

            @Override
            public void onFailure(Call<DetailedConsignment> call, Throwable t) {
                Log.e("FAIL", " Unsuccessfully" + t.getMessage());
            }
        });

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
}