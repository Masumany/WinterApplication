package com.example.winterapplication.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.winterapplication.R;


public class DirectorContentActivity extends AppCompatActivity {

    private TextView BackTextView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_director_content);


        WebView webView = findViewById(R.id.webView);

        // 从启动该活动的 Intent 中获取传递过来的新闻链接
        String link = getIntent().getStringExtra("link");


        WebSettings webSettings = webView.getSettings();
        // 启用 JavaScript
        webSettings.setJavaScriptEnabled(true);


        if (link != null) {
            // 如果链接不为空，使用 WebView 加载该链接对应的网页
            webView.loadUrl(link);
        }

        initView();

        initClick();
    }

    // 初始化点击事件的方法
    private void initClick() {

        if (BackTextView != null) {
            // 如果不为空，为 BackTextView 设置点击事件监听器
            BackTextView.setOnClickListener(view -> {
                // 点击 BackTextView 时，创建一个跳转到 DirectorActivity 的 Intent
                Intent intent = new Intent(DirectorContentActivity.this, DirectorActivity.class);
                // 启动 DirectorActivity
                startActivity(intent);
                // 结束当前活动，从任务栈中移除
                finish();
            });
        } else {

            Log.e("DirectorContentActivity", "BackTextView is null");
        }
    }


    private void initView() {

        BackTextView = findViewById(R.id.net_back);
    }
}