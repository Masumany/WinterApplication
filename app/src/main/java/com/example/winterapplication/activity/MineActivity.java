package com.example.winterapplication.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.winterapplication.R;
import com.example.winterapplication.presenter.MinePresenter;
import com.example.winterapplication.view.MineView;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MineActivity extends AppCompatActivity implements MineView {

    // 登录请求码，用于 startActivityForResult
    private static final int REQUEST_CODE_LOGIN = 1;

    private TextView textView, textView1, textView2, logoutTextView, textView3;

    private BottomNavigationView bottomNavigationView;

    private ImageView avatarImageView;

    private MinePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mine);

        // 获取名为 "user_data" 的 SharedPreferences 实例，用于存储用户相关数据
        SharedPreferences sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
        // 初始化 MinePresenter，传入当前活动实例和 SharedPreferences 实例
        presenter = new MinePresenter(this, sharedPreferences);


        initViews();

        // 调用 Presenter 的 onCreate 方法
        presenter.onCreate();
    }

    // 初始化界面视图的方法
    private void initViews() {

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // 当菜单项被选中时，调用 Presenter 的相应方法处理
                presenter.onBottomNavigationItemSelected(item.getItemId());
                return true;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.navigation_mine);


        avatarImageView = findViewById(R.id.mine_user);

        textView = findViewById(R.id.mine_text);

        logoutTextView = findViewById(R.id.logout_text);


        avatarImageView.setOnClickListener(v -> presenter.onAvatarClick());

        textView1 = findViewById(R.id.square);
        // 为 "广场" 功能入口设置点击事件监听器，点击时调用 Presenter 的相应方法
        textView1.setOnClickListener(v -> presenter.onSquareClick());


        textView2 = findViewById(R.id.more);
        // 为 "更多" 功能入口设置点击事件监听器，点击时调用 Presenter 的相应方法
        textView2.setOnClickListener(v -> presenter.onMoreClick());


        textView3 = findViewById(R.id.study);
        // 为 "学习" 功能入口设置点击事件监听器，点击时调用 Presenter 的相应方法
        textView3.setOnClickListener(v -> presenter.onStudyClick());

        // 为退出登录的 TextView 设置点击事件监听器，点击时调用 Presenter 的相应方法
        logoutTextView.setOnClickListener(v -> presenter.onLogoutClick());
    }

    @Override
    public void updateAvatar(int resId) {
        // 根据传入的资源 ID 更新用户头像的显示
        avatarImageView.setImageResource(resId);
    }

    @Override
    public void setWelcomeText(String text) {
        // 设置显示欢迎语的 TextView 的文本内容
        textView.setText(text);
    }

    @Override
    public void showLogoutToast() {
        // 显示一个短时间的 Toast 提示用户已退出登录
        Toast.makeText(this, "已退出登录", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startLoginActivity() {

        Intent intent = new Intent(MineActivity.this, LoginActivity.class);
        // 启动登录活动，并请求返回结果，请求码为 REQUEST_CODE_LOGIN
        startActivityForResult(intent, REQUEST_CODE_LOGIN);
    }

    @Override
    public void startSquareActivity() {

        Intent intent = new Intent(MineActivity.this, SquareActivity.class);
        // 启动广场活动
        startActivity(intent);
    }

    @Override
    public void startMoreActivity() {

        Intent intent = new Intent(MineActivity.this, MoreActivity.class);

        startActivity(intent);
    }

    @Override
    public void startStudyActivity() {

        Intent intent = new Intent(MineActivity.this, StudyActivity.class);

        startActivity(intent);
    }

    @Override
    public void startMainActivity() {

        Intent intent = new Intent(MineActivity.this, MainActivity.class);

        startActivity(intent);
    }

    @Override
    public void startStructActivity() {

        Intent intent = new Intent(MineActivity.this, StructActivity.class);

        startActivity(intent);
    }

    @Override
    public void startDirectorActivity() {

        Intent intent = new Intent(MineActivity.this, DirectorActivity.class);

        startActivity(intent);
    }

    @Override
    public void setBottomNavigationSelectedItem(int itemId) {
        // 设置底部导航栏当前选中的菜单项为指定的菜单项 ID
        bottomNavigationView.setSelectedItemId(itemId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // 调用父类的 onActivityResult 方法，确保 Activity 的正常回调处理
        super.onActivityResult(requestCode, resultCode, data);
        // 将 Activity 结果传递给 Presenter 进行处理
        presenter.onActivityResult(requestCode, resultCode);
    }
}