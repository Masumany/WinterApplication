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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.winterapplication.presenter.MinePresenter;
import com.example.winterapplication.view.MineView;

public class MineActivity extends AppCompatActivity implements MineView {

    private TextView textView, textView1, textView2, logoutTextView, textView3;
    private BottomNavigationView bottomNavigationView;
    private ImageView avatarImageView;
    private MinePresenter presenter;
    private static final int REQUEST_CODE_LOGIN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);

        SharedPreferences sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
        presenter = new MinePresenter(this, sharedPreferences);

        // 初始化视图
        initViews();

        presenter.onCreate();
    }

    private void initViews() {
        // 初始化底部导航栏
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
        textView1.setOnClickListener(v -> presenter.onSquareClick());

        textView2 = findViewById(R.id.more);
        textView2.setOnClickListener(v -> presenter.onMoreClick());

        textView3 = findViewById(R.id.study);
        textView3.setOnClickListener(v -> presenter.onStudyClick());

        // 退出登录点击事件
        logoutTextView.setOnClickListener(v -> presenter.onLogoutClick());
    }

    @Override
    public void updateAvatar(int resId) {
        avatarImageView.setImageResource(resId);
    }

    @Override
    public void setWelcomeText(String text) {
        textView.setText(text);
    }

    @Override
    public void showLogoutToast() {
        Toast.makeText(this, "已退出登录", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startLoginActivity() {
        Intent intent = new Intent(MineActivity.this, LoginActivity.class);
        startActivityForResult(intent, REQUEST_CODE_LOGIN);
    }

    @Override
    public void startSquareActivity() {
        Intent intent = new Intent(MineActivity.this, SquareActivity.class);
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
        bottomNavigationView.setSelectedItemId(itemId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode);
    }
}