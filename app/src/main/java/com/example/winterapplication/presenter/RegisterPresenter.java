package com.example.winterapplication.presenter;

import android.content.SharedPreferences;

import com.example.winterapplication.view.RegisterView;

public class RegisterPresenter {
    private RegisterView view;
    private SharedPreferences sharedPreferences;

    public RegisterPresenter(RegisterView view, SharedPreferences sharedPreferences) {
        this.view = view;
        this.sharedPreferences = sharedPreferences;
    }

    public void onRegisterButtonClick(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            view.showEmptyFieldsError();
            return;
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putBoolean("isLoggedIn", true);
        editor.apply();

        view.showRegistrationSuccess();
        view.finishActivityWithResultOk();
    }
}