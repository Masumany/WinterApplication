package com.example.winterapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.winterapplication.R;
import com.example.winterapplication.presenter.MorePresenter;
import com.example.winterapplication.view.MoreView;

import pl.droidsonroids.gif.GifImageView;


public class MoreActivity extends AppCompatActivity implements MoreView {


    private TextView BackTextView;

    private MorePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_more);

        // 初始化 MorePresenter，传入当前活动实例
        presenter = new MorePresenter(this);


        initView();

        initClick();
    }


    private void initView() {

        BackTextView = findViewById(R.id.net_back);
    }


    private void initClick() {
        // 为返回按钮设置点击事件监听器，点击时调用 Presenter 的 onBackButtonClick 方法
        BackTextView.setOnClickListener(v -> presenter.onBackButtonClick());


        GifImageView gifImageView = findViewById(R.id.gif);

        gifImageView.setOnClickListener(v -> presenter.onGifClick());
    }

    @Override
    public void navigateToMineActivity() {

        Intent intent = new Intent(MoreActivity.this, MineActivity.class);

        startActivity(intent);
        // 结束当前活动（MoreActivity），将其从任务栈中移除
        finish();
    }

    @Override
    public void showToast(String message) {
        // 显示一个短时间的 Toast 提示信息，提示内容为传入的 message
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}