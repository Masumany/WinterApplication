package com.example.winterapplication.presenter;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.content.SharedPreferences;

import com.example.winterapplication.R;
import com.example.winterapplication.view.LoginView;

public class LoginPresenter {

    private final LoginView view;

    private final SharedPreferences sharedPreferences;


    public LoginPresenter(LoginView view, SharedPreferences sharedPreferences) {
        this.view = view;
        this.sharedPreferences = sharedPreferences;
    }


    public void onRegisterTextClick() {
        view.navigateToRegister();
    }


    public void onEyeImageClick(boolean isPasswordVisible) {
        // 取反当前密码可见状态
        boolean newVisibility = !isPasswordVisible;
        // 调用 LoginView 的 setPasswordVisibility 方法，设置新的密码可见状态
        view.setPasswordVisibility(newVisibility);
        // 根据新的可见状态选择对应的眼睛图标资源
        int resId = newVisibility ? R.drawable.eye_open : R.drawable.eye_close;
        // 调用 LoginView 的 setEyeImageResource 方法，设置眼睛图标资源
        view.setEyeImageResource(resId);
    }


    public void onLoginButtonClick(String username, String password) {
        // 从 SharedPreferences 中获取保存的用户名
        String savedUsername = sharedPreferences.getString("username", "");
        // 从 SharedPreferences 中获取保存的密码
        String savedPassword = sharedPreferences.getString("password", "");

        // 比较输入的用户名和密码与保存的是否一致
        if (username.equals(savedUsername) && password.equals(savedPassword)) {
            // 获取 SharedPreferences 的编辑器
            SharedPreferences.Editor editor = sharedPreferences.edit();
            // 设置登录状态为已登录
            editor.putBoolean("isLoggedIn", true);
            // 应用编辑操作
            editor.apply();

            // 创建一个 Intent，用于返回结果
            Intent resultIntent = new Intent();
            // 调用 LoginView 的 showLoginSuccess 方法，显示登录成功提示
            view.showLoginSuccess();
            // 调用 LoginView 的 finishActivity 方法，结束当前登录界面并返回结果
            view.finishActivity(RESULT_OK, resultIntent);
        } else {
            // 调用 LoginView 的 showLoginFailure 方法，显示登录失败提示
            view.showLoginFailure();
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 检查请求码和结果码是否符合预期
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // 获取 SharedPreferences 的编辑器
            SharedPreferences.Editor editor = sharedPreferences.edit();
            // 设置登录状态为已登录
            editor.putBoolean("isLoggedIn", true);
            // 应用编辑操作
            editor.apply();

            // 创建一个 Intent，用于返回结果
            Intent resultIntent = new Intent();
            // 调用 LoginView 的 finishActivity 方法，结束当前登录界面并返回结果
            view.finishActivity(RESULT_OK, resultIntent);
        }
    }
}