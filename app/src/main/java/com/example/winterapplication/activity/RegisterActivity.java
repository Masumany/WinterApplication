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

        SharedPreferences sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
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
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                presenter.onRegisterButtonClick(username, password);
            }
        });
    }

    @Override
    public void showEmptyFieldsError() {
        Toast.makeText(this, "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showRegistrationSuccess() {
        Toast.makeText(this, "注册成功喽!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finishActivityWithResultOk() {
        setResult(RESULT_OK);
        finish();
    }
}