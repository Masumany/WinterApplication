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

        TextView titleTextView=findViewById(R.id.title_text_view);
        TextView authorTextView = findViewById(R.id.author_text_view);

        WebView webView=findViewById(R.id.webView);

        String title=getIntent().getStringExtra("title");
        String author = getIntent().getStringExtra("author");
        String niceDate = getIntent().getStringExtra("niceDate");
        int zan = getIntent().getIntExtra("zan", 0);
        String link = getIntent().getStringExtra("link");

        titleTextView.setText(title);
        authorTextView.setText("作者:"+author);


        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        if(link!=null){
            webView.loadUrl(link);
        }
        initView();
        initClick();


    }

    private void initClick() {
        if (BackTextView!=null){
            BackTextView.setOnClickListener(view -> {
                Intent intent=new Intent(StructContentActivity.this, StructArticleActivity.class);
                startActivity(intent);
                finish();
            });
        }else {
            Log.e("StructContentActivity","BackTextView is null");
        }
    }

    private void initView() {
        BackTextView=findViewById(R.id.net_back);
    }

}
