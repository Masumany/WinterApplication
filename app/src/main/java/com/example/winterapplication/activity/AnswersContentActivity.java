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

public class AnswersContentActivity extends AppCompatActivity {
    private TextView BackTextView;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers_content);
        TextView titleTextView=findViewById(R.id.title_text_view);
        WebView webView=findViewById(R.id.webView);
        String title=getIntent().getStringExtra("title");
        String Link = getIntent().getStringExtra("link");//获取传过来的数据并传给title和Link
        titleTextView.setText(title);//把 title 的文本内容显示在titleTextView上
        WebSettings webSettings=webView.getSettings();//获取 WebView 的设置对象
        webSettings.setJavaScriptEnabled(true);//将 JavaScript 功能设置为启用状态
        webView.setWebViewClient(new WebViewClient());
        if(Link!=null){
            webView.loadUrl(Link);//使用 WebView 加载 Link 所代表的网址
        }
        initView();
        initClick();
    }
    private void initClick() {
        if (BackTextView!=null){
            BackTextView.setOnClickListener(view -> {
                Intent intent=new Intent(AnswersContentActivity.this, com.example.winterapplication.view.AnswersView.class);
                startActivity(intent);
                finish();
            });
        }else {
            Log.e("AnswersContent","BackTextView is null");
        }
    }
    private void initView() {
        BackTextView=findViewById(R.id.net_back);
    }
}


