package com.example.winterapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.winterapplication.R;

public class UsefulWebsiteContentActivity extends AppCompatActivity {
    private TextView BackTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usefulwebsite_content);

        TextView titleTextView = findViewById(R.id.title_text_view);
        TextView contentTextView = findViewById(R.id.content_text_view);
        WebView webView = findViewById(R.id.webView);

        String name = getIntent().getStringExtra("name");
        String Link = getIntent().getStringExtra("link");

        // 将网站名称设置到标题的 TextView 上，用于显示网站标题
        titleTextView.setText(name);

        // 获取 WebView 的设置对象，用于对 WebView 进行配置
        WebSettings webSettings = webView.getSettings();
        // 启用 WebView 的 JavaScript 支持
        webSettings.setJavaScriptEnabled(true);
        // 设置 WebViewClient，使得网页在当前 WebView 中加载，而不是跳转到系统默认浏览器
        webView.setWebViewClient(new WebViewClient());


        if (Link != null) {
            // 如果链接不为空，使用 WebView 加载该链接对应的网站页面
            webView.loadUrl(Link);
        }


        initView();

        initClick();
    }


    private void initClick() {
        // 检查返回按钮的 TextView 控件是否为空
        if (BackTextView != null) {
            // 如果不为空，为返回按钮设置点击事件监听器
            BackTextView.setOnClickListener(view -> {
                // 当点击返回按钮时，创建一个跳转到 UsefulWebsiteActivity 的 Intent
                Intent intent = new Intent(UsefulWebsiteContentActivity.this, UsefulWebsiteActivity.class);
                // 启动 UsefulWebsiteActivity 活动
                startActivity(intent);
                // 结束当前的 UsefulWebsiteContentActivity 活动，从任务栈中移除
                finish();
            });
        } else {

            Log.e("UsefulWebsiteContent", "BackTextView is null");
        }
    }


    private void initView() {
        BackTextView = findViewById(R.id.net_back);
    }
}