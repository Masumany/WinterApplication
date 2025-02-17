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

public class SquareContentActivity extends AppCompatActivity {
    private TextView BackTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square_content);

        TextView titleTextView = findViewById(R.id.title_text_view);
        WebView webView = findViewById(R.id.webView);

        String title = getIntent().getStringExtra("title");
        String link = getIntent().getStringExtra("link");

        // 将获取到的新闻标题设置到对应的 TextView 上
        titleTextView.setText(title);

        // 获取 WebView 的设置对象
        WebSettings webSettings = webView.getSettings();
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
                Intent intent = new Intent(SquareContentActivity.this, SquareActivity.class);
                startActivity(intent);
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