package com.example.winterapplication.presenter;

import static android.app.Activity.RESULT_OK;

import android.content.SharedPreferences;

import com.example.winterapplication.R;
import com.example.winterapplication.view.MineView;

public class MinePresenter {
    private MineView view;
    private SharedPreferences sharedPreferences;

    public MinePresenter(MineView view, SharedPreferences sharedPreferences) {
        this.view = view;
        this.sharedPreferences = sharedPreferences;
    }

    public void onCreate() {
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        updateUI(isLoggedIn);
    }

    public void onAvatarClick() {
        view.startLoginActivity();
    }

    public void onSquareClick() {
        view.startSquareActivity();
    }

    public void onMoreClick() {
        view.startMoreActivity();
    }

    public void onStudyClick() {
        view.startStudyActivity();
    }

    public void onLogoutClick() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.apply();
        updateUI(false);
        view.showLogoutToast();
    }

    public void onActivityResult(int requestCode, int resultCode) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
            updateUI(isLoggedIn);
        }
    }

    public void onBottomNavigationItemSelected(int itemId) {
        if (itemId == R.id.navigation_home) {
            view.startMainActivity();
        } else if (itemId == R.id.navigation_structure) {
            view.startStructActivity();
        } else if (itemId == R.id.navigation_director) {
            view.startDirectorActivity();
        } else if (itemId == R.id.navigation_mine) {
            // 当前就是 MineActivity，无需处理
        }
    }

    private void updateUI(boolean isLoggedIn) {
        int avatarResId = isLoggedIn ? R.drawable.mine_theme : R.drawable.login_before;
        view.updateAvatar(avatarResId);
        String welcomeText = isLoggedIn ? "登陆成功，欢迎！！！" : "还未登录哦！";
        view.setWelcomeText(welcomeText);
    }
}