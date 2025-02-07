package com.example.winterapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;



public class LoginActivity extends AppCompatActivity {
    private EditText passwordEditText;
    private ImageView eyeImageView;
    private Button mBtnLogin;
    private EditText nameEditText;
    private boolean isPasswordVisible = false;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nameEditText = findViewById(R.id.et_main_username);
        passwordEditText = findViewById(R.id.et_main_password);
        eyeImageView = findViewById(R.id.eyeImageView);
        mBtnLogin = findViewById(R.id.btn_main_login);

        TextView registerText=findViewById(R.id.registerText);
        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);

            }
        });


        // 为小眼睛图标添加点击事件监听器
        eyeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    // 隐藏密码
                    passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isPasswordVisible = false;
                    eyeImageView.setImageResource(R.drawable.eye_close); // 替换为闭眼图标
                } else {
                    // 显示密码
                    passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isPasswordVisible = true;
                    eyeImageView.setImageResource(R.drawable.eye_open); // 替换睁眼图标
                }
            }
        });


        // 为登录按钮添加点击事件
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });


        SharedPreferences sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        String password = sharedPreferences.getString("password", "");
    }


    private void login() {
        String username = nameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        Log.d("LoginActivity", "输入的用户名: " + username + ", 输入的密码: " + password);
        if (username.equals("Mmy") && password.equals("123456")) {
            loginSuccess();
        } else {
            loginFail();
        }
    }

    private void loginFail() {
        Toast.makeText(this, "登陆失败,请再试一试吧！", Toast.LENGTH_SHORT).show();

    }


    private void loginSuccess() {
        Toast.makeText(this, "登陆成功喽！", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);


    }
}