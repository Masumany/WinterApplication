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
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usefulwebsite_content);

        TextView titleTextView=findViewById(R.id.title_text_view);
        TextView contentTextView=findViewById(R.id.content_text_view);
        WebView webView=findViewById(R.id.webView);

        String name=getIntent().getStringExtra("name");
        String Link = getIntent().getStringExtra("link");

        titleTextView.setText(name);
        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        if(Link!=null){
            webView.loadUrl(Link);
        }
        initView();
        initClick();


    }

    private void initClick() {
        if (BackTextView!=null){
            BackTextView.setOnClickListener(view -> {
                Intent intent=new Intent(UsefulWebsiteContentActivity.this, UsefulWebsiteActivity.class);
                startActivity(intent);
                finish();
            });
        }else {
            Log.e("UsefulWebsiteContent","BackTextView is null");
        }
    }

    private void initView() {
        BackTextView=findViewById(R.id.net_back);
    }


}


