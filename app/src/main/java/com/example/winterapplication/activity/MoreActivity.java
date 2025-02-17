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

        presenter = new MorePresenter(this);

        initView();
        initClick();
    }

    private void initView() {
        BackTextView = findViewById(R.id.net_back);
    }

    private void initClick() {
        BackTextView.setOnClickListener(v -> presenter.onBackButtonClick());

        GifImageView gifImageView = findViewById(R.id.gif);
        gifImageView.setOnClickListener(v -> presenter.onGifClick());
    }

    @Override
    public void navigateToMineActivity() {
        Intent intent = new Intent(MoreActivity.this, MineActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}