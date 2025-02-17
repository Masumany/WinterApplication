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
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_content);

        TextView titleTextView=findViewById(R.id.title_text_view);
        TextView contentTextView=findViewById(R.id.content_text_view);
        WebView webView=findViewById(R.id.webView);

        String name=getIntent().getStringExtra("name");
        String content=getIntent().getStringExtra("desc");
        String lisenseLink = getIntent().getStringExtra("link");

        titleTextView.setText(name);
        contentTextView.setText(content);



        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        if(lisenseLink!=null){
            webView.loadUrl(lisenseLink);
        }
        initView();
        initClick();


    }

    private void initClick() {
        if (BackTextView!=null){
            BackTextView.setOnClickListener(view -> {
                Intent intent=new Intent(StudyContentActivity.this,StudyActivity.class);
                startActivity(intent);
                finish();
            });
        }else {
            Log.e("StudyContentActivity","BackTextView is null");
        }
    }

    private void initView() {
        BackTextView=findViewById(R.id.net_back);
    }


}


