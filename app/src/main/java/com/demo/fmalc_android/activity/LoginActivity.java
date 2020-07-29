package com.demo.fmalc_android.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.demo.fmalc_android.R;
import com.demo.fmalc_android.contract.LoginContract;
import com.demo.fmalc_android.entity.GlobalVariable;
import com.demo.fmalc_android.entity.LoginResponse;
import com.demo.fmalc_android.presenter.SchedulePresenter;
import com.demo.fmalc_android.presenter.LoginPresenter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    private TextInputEditText edtUsername, edtPassword;
    private MaterialButton btnLogin;
    private LoginPresenter loginPresenter;
    private SchedulePresenter schedulePresenter;
    private ProgressBar loginProgressBar;
    private int progressStatus = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = findViewById(R.id.username);
        edtPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnLogin);

        init();

        loginProgressBar = (ProgressBar) findViewById(R.id.loginProgressBar);

    }

    private void init(){
        loginPresenter = new LoginPresenter();
        loginPresenter.setView(this);
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
                loginProgressBar.setVisibility(1);
                loginProgressBar.setProgress(60);
                loginPresenter.doLogin(username, password);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void loginSuccess(LoginResponse loginResponse) {
        Intent intent = new Intent(getApplicationContext(), DriverHomeActivity.class);
        final GlobalVariable globalVariable = (GlobalVariable) getApplicationContext();
        globalVariable.setUsername(loginResponse.getUsername());
        globalVariable.setRole(loginResponse.getRole());
        globalVariable.setToken(loginResponse.getToken());
        globalVariable.setId(loginResponse.getId());
        loginProgressBar.setVisibility(-1);
        startActivity(intent);
    }

    @Override
    public void loginFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        loginProgressBar.setVisibility(-1);
    }
}