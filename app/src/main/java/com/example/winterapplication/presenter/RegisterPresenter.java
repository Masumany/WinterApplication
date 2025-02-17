package com.example.winterapplication.presenter;

import android.content.SharedPreferences;

import com.example.winterapplication.view.RegisterView;

public class RegisterPresenter {

    private final RegisterView view;

    private final SharedPreferences sharedPreferences;

    public RegisterPresenter(RegisterView view, SharedPreferences sharedPreferences) {
        this.view = view;
        this.sharedPreferences = sharedPreferences;
    }


    public void onRegisterButtonClick(String username, String password) {
        // 检查用户名和密码是否为空
        if (username.isEmpty() || password.isEmpty()) {
            // 若为空，调用 RegisterView 的 showEmptyFieldsError 方法显示错误提示
            view.showEmptyFieldsError();
            return;
        }

        // 获取 SharedPreferences 的编辑器
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // 将用户名存储到 SharedPreferences 中
        editor.putString("username", username);
        // 将密码存储到 SharedPreferences 中
        editor.putString("password", password);
        // 将登录状态设置为已登录
        editor.putBoolean("isLoggedIn", true);
        // 应用编辑操作
        editor.apply();

        // 调用 RegisterView 的 showRegistrationSuccess 方法显示注册成功提示
        view.showRegistrationSuccess();
        // 调用 RegisterView 的 finishActivityWithResultOk 方法结束当前活动并返回成功结果
        view.finishActivityWithResultOk();
    }
}