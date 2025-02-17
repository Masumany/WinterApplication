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

public class StudyContentActivity extends AppCompatActivity {
    private TextView BackTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_content);

        TextView titleTextView = findViewById(R.id.title_text_view);
        TextView contentTextView = findViewById(R.id.content_text_view);
        WebView webView = findViewById(R.id.webView);

        // 从启动该活动的 Intent 中获取传递过来的名称
        String name = getIntent().getStringExtra("name");
        // 从启动该活动的 Intent 中获取传递过来的学习内容描述
        String content = getIntent().getStringExtra("desc");
        // 从启动该活动的 Intent 中获取传递过来的学习内容的链接
        String lisenseLink = getIntent().getStringExtra("link");

        // 将学习内容名称设置到标题的 TextView 上
        titleTextView.setText(name);
        // 将学习内容描述设置到内容描述的 TextView 上
        contentTextView.setText(content);

        WebSettings webSettings = webView.getSettings();
        // 启用 WebView 的 JavaScript 支持
        webSettings.setJavaScriptEnabled(true);
        // 设置 WebViewClient，使得网页在当前 WebView 中加载，而不是跳转到系统浏览器
        webView.setWebViewClient(new WebViewClient());

        // 检查获取到的链接是否为空
        if (lisenseLink != null) {
            // 若链接不为空，使用 WebView 加载该链接对应的网页
            webView.loadUrl(lisenseLink);
        }


        initView();

        initClick();
    }


    private void initClick() {
        // 检查返回按钮的 TextView 是否为空
        if (BackTextView != null) {
            // 若不为空，为返回按钮设置点击事件监听器
            BackTextView.setOnClickListener(view -> {
                // 创建一个从当前活动跳转到 StudyActivity 的 Intent
                Intent intent = new Intent(StudyContentActivity.this, StudyActivity.class);
                // 启动 StudyActivity
                startActivity(intent);
                // 结束当前活动，将其从任务栈中移除
                finish();
            });
        } else {
            // 若返回按钮的 TextView 为空，打印错误日志
            Log.e("StudyContentActivity", "BackTextView is null");
        }
    }


    private void initView() {
        // 从布局文件中查找返回按钮对应的 TextView 控件
        BackTextView = findViewById(R.id.net_back);
    }
}