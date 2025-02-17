package com.example.winterapplication.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.winterapplication.R;

public class StructContentActivity extends AppCompatActivity {
    private TextView BackTextView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_struct_content);

        TextView titleTextView = findViewById(R.id.title_text_view);
        TextView authorTextView = findViewById(R.id.author_text_view);
        WebView webView = findViewById(R.id.webView);

        String title = getIntent().getStringExtra("title");
        String author = getIntent().getStringExtra("author");
        String niceDate = getIntent().getStringExtra("niceDate");
        // 从启动该活动的 Intent 中获取传递过来的文章点赞数，若未传递则默认为 0
        int zan = getIntent().getIntExtra("zan", 0);
        // 从启动该活动的 Intent 中获取传递过来的文章链接
        String link = getIntent().getStringExtra("link");

        // 将获取到的文章标题设置到对应的 TextView 上
        titleTextView.setText(title);
        // 将获取到的文章作者设置到对应的 TextView 上，并添加 "作者:"
        authorTextView.setText("作者:" + author);

        // 获取 WebView 的设置对象
        WebSettings webSettings = webView.getSettings();
        // 启用 JavaScript 支持
        webSettings.setJavaScriptEnabled(true);
        // 设置 WebViewClient，使网页在当前 WebView 中加载，而不是跳转到系统浏览器
        webView.setWebViewClient(new WebViewClient());

        // 检查获取到的链接是否为空
        if (link != null) {
            // 如果链接不为空，使用 WebView 加载该链接对应的网页
            webView.loadUrl(link);
        }

        initView();
        initClick();
    }

    private void initClick() {
        // 检查 BackTextView 是否为空
        if (BackTextView != null) {
            // 如果不为空，为 BackTextView 设置点击事件监听器
            BackTextView.setOnClickListener(view -> {
                // 点击 BackTextView 时，创建一个跳转到 StructArticleActivity 的 Intent
                Intent intent = new Intent(StructContentActivity.this, StructArticleActivity.class);
                // 启动 StructArticleActivity
                startActivity(intent);
                // 结束当前活动，从任务栈中移除
                finish();
            });
        } else {
            Log.e("StructContentActivity", "BackTextView is null");
        }
    }

    private void initView() {
        BackTextView = findViewById(R.id.net_back);
    }
}