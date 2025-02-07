package com.example.winterapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class MineActivity extends AppCompatActivity {

    private TextView BackTextView,textView,textView1,textView2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        ImageView imageView=findViewById(R.id.struct);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MineActivity.this, StructActivity.class);
                startActivity(intent);
            }
        });
        ImageView imageView1=findViewById(R.id.first);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MineActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        ImageView imageView2=findViewById(R.id.director);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MineActivity.this, DirectorActivity.class);
                startActivity(intent);
            }
        });
        ImageView imageView3=findViewById(R.id.mine_user);
        textView=findViewById(R.id.mine_text);
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent intent=new Intent(MineActivity.this, LoginActivity.class);
                    startActivity(intent);
                    textView.setText("登陆成功，欢迎！！！");
            }
        });
        textView1=findViewById(R.id.square);
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MineActivity.this,SquareActivity.class);
                startActivity(intent);
            }
        });
        textView2=findViewById(R.id.more);
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MineActivity.this,MoreActivity.class);
                startActivity(intent);
            }
        });
        initView();
        initClick();
    }


    private void initClick() {
        if (BackTextView!= null) {
            BackTextView.setOnClickListener(v -> {
                Intent intent = new Intent(MineActivity.this, MainActivity.class);
                // 启动该意图，实现页面跳转到首页面
                startActivity(intent);
                finish();
            });
        } else {
            Log.e("MineActivity", "BackTextView is null");
        }
    }


    private void initView() {
        BackTextView = findViewById(R.id.net_back);
    }
}