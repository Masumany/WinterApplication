package com.example.winterapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainContentActivity extends AppCompatActivity {
    private TextView BackTextView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_content);

        // 找到布局中的各个 TextView
        TextView titleTextView = findViewById(R.id.title_text_view);
        TextView authorTextView = findViewById(R.id.author_text_view);
        TextView niceDateTextView = findViewById(R.id.nice_date_text_view);

        // 找到 WebView
        WebView webView = findViewById(R.id.webView);

        // 获取从 MainActivity 传递过来的数据
        String title = getIntent().getStringExtra("title");
        String author = getIntent().getStringExtra("author");
        String niceDate = getIntent().getStringExtra("niceDate");
        int zan = getIntent().getIntExtra("zan", 0);
        String link = getIntent().getStringExtra("link");

        // 将数据显示在对应的 TextView 上
        titleTextView.setText(title);
        authorTextView.setText("作者: " + author);
        niceDateTextView.setText("日期: " + niceDate);


        // 配置 WebView
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // 启用 JavaScript
        webView.setWebViewClient(new WebViewClient()); // 设置 WebViewClient，使链接在 WebView 中打开

        // 加载链接内容
        if (link != null) {
            webView.loadUrl(link);
        }
        initView();
        initClick();


    }



    private void initClick() {
        if (BackTextView!=null){
            BackTextView.setOnClickListener(view -> {
                Intent intent=new Intent(MainContentActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            });
        } else {
        Log.e("MainContentActivity", "BackTextView is null");
    }
    }
    private void initView() {
        BackTextView=findViewById(R.id.net_back);
    }
}