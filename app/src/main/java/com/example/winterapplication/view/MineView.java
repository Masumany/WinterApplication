package com.example.winterapplication.view;

import android.content.Intent;

public interface MineView {
    void updateAvatar(int resId);
    void setWelcomeText(String text);
    void showLogoutToast();
    void startLoginActivity();
    void startSquareActivity();
    void startMoreActivity();
    void startStudyActivity();
    void startMainActivity();
    void startStructActivity();
    void startDirectorActivity();
    void setBottomNavigationSelectedItem(int itemId);
}