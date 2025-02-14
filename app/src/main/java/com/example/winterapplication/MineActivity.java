package com.example.winterapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MineActivity extends AppCompatActivity {

    private TextView BackTextView, textView, textView1, textView2, logoutTextView;
    private BottomNavigationView bottomNavigationView;
    private ImageView avatarImageView;
    private static final int REQUEST_CODE_LOGIN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);

        // 初始化底部导航栏
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_home) {
                    Intent intent3 = new Intent(MineActivity.this, MainActivity.class);
                    startActivity(intent3);
                    return true;
                } else if (item.getItemId() == R.id.navigation_structure) {
                    Intent intent1 = new Intent(MineActivity.this, StructActivity.class);
                    startActivity(intent1);
                    return true;
                } else if (item.getItemId() == R.id.navigation_director) {
                    Intent intent2 = new Intent(MineActivity.this, DirectorActivity.class);
                    startActivity(intent2);
                    return true;
                } else if (item.getItemId() == R.id.navigation_mine) {
                    return true;
                }
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.navigation_mine);

        avatarImageView = findViewById(R.id.mine_user);
        textView = findViewById(R.id.mine_text);
        logoutTextView = findViewById(R.id.logout_text);

        // 检查登录状态
        SharedPreferences sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        updateAvatar(isLoggedIn);
        if (isLoggedIn) {
            textView.setText("登陆成功，欢迎！！！");
        }

        avatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MineActivity.this, LoginActivity.class);
                startActivityForResult(intent, REQUEST_CODE_LOGIN);
            }
        });

        textView1 = findViewById(R.id.square);
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MineActivity.this, SquareActivity.class);
                startActivity(intent);
            }
        });

        textView2 = findViewById(R.id.more);
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MineActivity.this, MoreActivity.class);
                startActivity(intent);
            }
        });

        // 退出登录点击事件
        logoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        initView();
        initClick();
    }

    private void initClick() {
        if (BackTextView != null) {
            BackTextView.setOnClickListener(v -> {
                Intent intent = new Intent(MineActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            });
        } else {
            Log.e("MineActivity", "BackTextView is null");
        }
    }

    private void initView() {
        BackTextView = findViewById(R.id.net_back);
    }

    private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.apply();
        textView.setText("还未登录哦！");
        updateAvatar(false);
        Toast.makeText(this, "已退出登录", Toast.LENGTH_SHORT).show();
    }

    private void updateAvatar(boolean isLoggedIn) {
        if (isLoggedIn) {
            avatarImageView.setImageResource(R.drawable.mine_theme);
        } else {
            avatarImageView.setImageResource(R.drawable.login_before);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_LOGIN && resultCode == RESULT_OK) {
            // 登录成功，更新头像和文字
            SharedPreferences sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
            boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
            updateAvatar(isLoggedIn);
            textView.setText("登陆成功，欢迎！！！");
        }
    }
}