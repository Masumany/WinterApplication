package com.example.winterapplication.view;

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