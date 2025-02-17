package com.example.winterapplication.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.winterapplication.R;
import com.example.winterapplication.presenter.LoginPresenter;
import com.example.winterapplication.view.LoginView;

public class LoginActivity extends AppCompatActivity implements LoginView {
    private boolean isPasswordVisible = false;
    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText nameEditText = findViewById(R.id.et_main_username);
        EditText passwordEditText = findViewById(R.id.et_main_password);
        ImageView eyeImageView = findViewById(R.id.eyeImageView);
        Button mBtnLogin = findViewById(R.id.btn_main_login);
        TextView registerText = findViewById(R.id.registerText);

        SharedPreferences sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
        presenter = new LoginPresenter(this, sharedPreferences);

        registerText.setOnClickListener(v -> presenter.onRegisterTextClick());

        eyeImageView.setOnClickListener(v -> presenter.onEyeImageClick(isPasswordVisible));

        mBtnLogin.setOnClickListener(v -> {
            String username = nameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            Log.d("LoginActivity", "输入的用户名: " + username + ", 输入的密码: " + password);
            presenter.onLoginButtonClick(username, password);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showLoginSuccess() {
        Toast.makeText(this, "登陆成功喽！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoginFailure() {
        Toast.makeText(this, "登陆失败,请再试一试吧！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToRegister() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public void setPasswordVisibility(boolean visible) {
        EditText passwordEditText = findViewById(R.id.et_main_password);
        if (visible) {
            passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        isPasswordVisible = visible;
    }

    @Override
    public void setEyeImageResource(int resId) {
        ImageView eyeImageView = findViewById(R.id.eyeImageView);
        eyeImageView.setImageResource(resId);
    }

    @Override
    public void finishActivity(int resultCode, Intent resultIntent) {
        setResult(resultCode, resultIntent);
        finish();
    }
}