package com.example.winterapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.winterapplication.R;

import pl.droidsonroids.gif.GifImageView;



public class MoreActivity extends AppCompatActivity {

    TextView BackTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        initView();
        initClick();
        GifImageView gifImageView = findViewById(R.id.gif);
        gifImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initClick() {
        BackTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MoreActivity.this,MineActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void initView() {
        BackTextView = findViewById(R.id.net_back);
    }
}