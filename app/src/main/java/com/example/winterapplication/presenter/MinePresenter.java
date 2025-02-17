package com.example.winterapplication.presenter;

import static android.app.Activity.RESULT_OK;

import android.content.SharedPreferences;

import com.example.winterapplication.R;
import com.example.winterapplication.view.MineView;

public class MinePresenter {

    private final MineView view;

    private final SharedPreferences sharedPreferences;


    public MinePresenter(MineView view, SharedPreferences sharedPreferences) {
        this.view = view;
        this.sharedPreferences = sharedPreferences;
    }

    public void onCreate() {
        // 从 SharedPreferences 中获取用户登录状态，默认值为 false
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        // 根据登录状态更新 UI
        updateUI(isLoggedIn);
    }


    public void onAvatarClick() {
        // 调用 MineView 的 startLoginActivity 方法启动登录页面
        view.startLoginActivity();
    }


    public void onSquareClick() {
        // 调用 MineView 的 startSquareActivity 方法启动广场页面
        view.startSquareActivity();
    }


    public void onMoreClick() {
        // 调用 MineView 的 startMoreActivity 方法启动更多页面
        view.startMoreActivity();
    }

    public void onStudyClick() {
        // 调用 MineView 的 startStudyActivity 方法启动学习页面
        view.startStudyActivity();
    }


    public void onLogoutClick() {
        // 获取 SharedPreferences 的编辑器
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // 将登录状态设置为未登录
        editor.putBoolean("isLoggedIn", false);
        // 应用编辑操作
        editor.apply();
        // 根据未登录状态更新 UI
        updateUI(false);
        // 调用 MineView 的 showLogoutToast 方法显示退出登录提示
        view.showLogoutToast();
    }


    public void onActivityResult(int requestCode, int resultCode) {
        // 检查请求码和结果码是否符合预期
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // 从 SharedPreferences 中获取用户登录状态，默认值为 false
            boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
            // 根据登录状态更新 UI
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

        }
    }


    private void updateUI(boolean isLoggedIn) {
        // 根据登录状态选择相应的头像资源 ID
        int avatarResId = isLoggedIn ? R.drawable.mine_theme : R.drawable.login_before;
        // 调用 MineView 的 updateAvatar 方法更新头像
        view.updateAvatar(avatarResId);
        // 根据登录状态生成相应的欢迎文本
        String welcomeText = isLoggedIn ? "登陆成功，欢迎！！！" : "还未登录哦！";
        // 调用 MineView 的 setWelcomeText 方法设置欢迎文本
        view.setWelcomeText(welcomeText);
    }
}