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
    // 用于处理登录业务逻辑的 Presenter
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

        // 获取名为 "user_data" 的 SharedPreferences 实例，用于存储用户数据
        SharedPreferences sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
        // 初始化 LoginPresenter传入当前活动实例和 SharedPreferences 实例
        presenter = new LoginPresenter(this, sharedPreferences);
        //调用 Presenter 的 onRegisterTextClick 方法
        registerText.setOnClickListener(v -> presenter.onRegisterTextClick());

        //调用 Presenter 的 onEyeImageClick 方法，并传入当前密码可见状态
        eyeImageView.setOnClickListener(v -> presenter.onEyeImageClick(isPasswordVisible));


        mBtnLogin.setOnClickListener(v -> {
            // 获取用户输入的用户名
            String username = nameEditText.getText().toString();
            // 获取用户输入的密码
            String password = passwordEditText.getText().toString();

            Log.d("LoginActivity", "输入的用户名: " + username + ", 输入的密码: " + password);
            // 调用 Presenter 的 onLoginButtonClick 方法，传入用户名和密码进行登录验证
            presenter.onLoginButtonClick(username, password);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 调用父类的 onActivityResult 方法，确保 Activity 的正常回调处理
        super.onActivityResult(requestCode, resultCode, data);
        // 将 Activity 结果传递给 Presenter 进行处理
        presenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showLoginSuccess() {
        // 当登录成功时，显示一个短时间的 Toast 提示信息
        Toast.makeText(this, "登陆成功喽！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoginFailure() {
        // 当登录失败时，显示一个短时间的 Toast 提示信息
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
            // 如果需要显示密码，设置 EditText 的转换方法为显示明文
            passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            // 如果需要隐藏密码，设置 EditText 的转换方法为隐藏明文
            passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        // 更新密码可见状态标记
        isPasswordVisible = visible;
    }

    @Override
    public void setEyeImageResource(int resId) {
        // 从布局文件中获取用于切换密码可见性的眼睛图标 ImageView 控件
        ImageView eyeImageView = findViewById(R.id.eyeImageView);
        // 设置眼睛图标的资源为指定的资源 ID
        eyeImageView.setImageResource(resId);
    }

    @Override
    public void finishActivity(int resultCode, Intent resultIntent) {
        // 设置当前活动的返回结果码和返回意图
        setResult(resultCode, resultIntent);
        // 结束当前活动
        finish();
    }
}