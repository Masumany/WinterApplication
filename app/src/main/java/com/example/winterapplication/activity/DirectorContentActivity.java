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
import com.example.winterapplication.activity.DirectorActivity;

import java.io.InputStream;

public class DirectorContentActivity extends AppCompatActivity {
    private TextView BackTextView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_director_content);
        WebView webView=findViewById(R.id.webView);

        String link=getIntent().getStringExtra("link");

        WebSettings webSettings= webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        if (link!=null){
            webView.loadUrl(link);
        }
        initView();
        initClick();

    }

    private void initClick() {
        if (BackTextView!=null){
            BackTextView.setOnClickListener(view -> {
                Intent intent=new Intent(DirectorContentActivity.this, DirectorActivity.class);
                startActivity(intent);
                finish();
            });
        }else {
            Log.e("DirectorContentActivity","BackTextView is null");

        }
    }

    private void initView() {
        BackTextView=findViewById(R.id.net_back);
    }
}