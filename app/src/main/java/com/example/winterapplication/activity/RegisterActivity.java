package com.example.winterapplication.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.winterapplication.R;
import com.example.winterapplication.presenter.RegisterPresenter;
import com.example.winterapplication.view.RegisterView;


public class RegisterActivity extends AppCompatActivity implements RegisterView {

    private EditText usernameEditText;

    private EditText passwordEditText;

    private Button registerButton;

    private RegisterPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        // 获取名为 "user_data" 的 SharedPreferences 实例，用于存储用户数据
        SharedPreferences sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
        // 初始化 RegisterPresenter，传入当前活动实例和 SharedPreferences 实例
        presenter = new RegisterPresenter(this, sharedPreferences);


        initViews();

        setupClickListener();
    }


    private void initViews() {

        usernameEditText = findViewById(R.id.et_re_username);

        passwordEditText = findViewById(R.id.et_re_password);

        registerButton = findViewById(R.id.btn_re_login);
    }


    private void setupClickListener() {

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取用户输入的用户名
                String username = usernameEditText.getText().toString();
                // 获取用户输入的密码
                String password = passwordEditText.getText().toString();
                // 调用 Presenter 的 onRegisterButtonClick 方法，传入用户名和密码进行注册操作
                presenter.onRegisterButtonClick(username, password);
            }
        });
    }

    @Override
    public void showEmptyFieldsError() {
        // 当用户名或密码为空时，显示一个短时间的 Toast 提示信息
        Toast.makeText(this, "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showRegistrationSuccess() {
        // 当注册成功时，显示一个短时间的 Toast 提示信息
        Toast.makeText(this, "注册成功喽!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finishActivityWithResultOk() {
        // 设置当前活动的返回结果码为 RESULT_OK
        setResult(RESULT_OK);

        finish();
    }
}