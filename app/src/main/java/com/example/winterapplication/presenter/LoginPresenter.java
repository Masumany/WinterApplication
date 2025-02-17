package com.example.winterapplication.presenter;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.content.SharedPreferences;

import com.example.winterapplication.R;
import com.example.winterapplication.view.LoginView;

public class LoginPresenter {
    private LoginView view;
    private SharedPreferences sharedPreferences;

    public LoginPresenter(LoginView view, SharedPreferences sharedPreferences) {
        this.view = view;
        this.sharedPreferences = sharedPreferences;
    }

    public void onRegisterTextClick() {
        view.navigateToRegister();
    }

    public void onEyeImageClick(boolean isPasswordVisible) {
        boolean newVisibility = !isPasswordVisible;
        view.setPasswordVisibility(newVisibility);
        int resId = newVisibility ? R.drawable.eye_open : R.drawable.eye_close;
        view.setEyeImageResource(resId);
    }

    public void onLoginButtonClick(String username, String password) {
        String savedUsername = sharedPreferences.getString("username", "");
        String savedPassword = sharedPreferences.getString("password", "");

        if (username.equals(savedUsername) && password.equals(savedPassword)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLoggedIn", true);
            editor.apply();

            Intent resultIntent = new Intent();
            view.showLoginSuccess();
            view.finishActivity(RESULT_OK, resultIntent);
        } else {
            view.showLoginFailure();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLoggedIn", true);
            editor.apply();

            Intent resultIntent = new Intent();
            view.finishActivity(RESULT_OK, resultIntent);
        }
    }
}