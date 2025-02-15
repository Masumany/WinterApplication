package com.example.winterapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AnswersContent extends AppCompatActivity {
    private TextView BackTextView;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers_content);

        TextView titleTextView=findViewById(R.id.title_text_view);
        WebView webView=findViewById(R.id.webView);

        String title=getIntent().getStringExtra("title");
        String Link = getIntent().getStringExtra("link");

        titleTextView.setText(title);
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
                Intent intent=new Intent(AnswersContent.this,Answers.class);
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


