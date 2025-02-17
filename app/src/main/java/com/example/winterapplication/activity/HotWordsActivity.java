package com.example.winterapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.winterapplication.R;


public class HotWordsActivity extends AppCompatActivity {


    private TextView BackTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hotwords);


        initView();

        initClick();
    }


    private void initClick() {

        BackTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HotWordsActivity.this, MainActivity.class);
                // 启动 MainActivity
                startActivity(intent);
                // 结束当前活动（HotWordsActivity），将其从任务栈中移除
                finish();
            }
        });
    }


    private void initView() {

        BackTextView = findViewById(R.id.net_back);
    }
}