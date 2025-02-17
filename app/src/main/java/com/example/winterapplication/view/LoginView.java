package com.example.winterapplication.view;

import android.content.Intent;

public interface LoginView {
    void showLoginSuccess();
    void showLoginFailure();
    void navigateToRegister();
    void setPasswordVisibility(boolean visible);
    void setEyeImageResource(int resId);
    void finishActivity(int resultCode, Intent resultIntent);
}